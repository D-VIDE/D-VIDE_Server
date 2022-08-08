package com.divide.utils;

import com.divide.exception.RestApiException;
import com.divide.exception.code.FileIOErrorCode;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class OCIUtil {
    private static final String CONFIGURATION_FILE_PATH = "src/main/resources/config/ociConfig";
    private static final String PROFILE = "DEFAULT";
    private static final Region REGION = Region.AP_CHUNCHEON_1;
    private static final String BUCKET_NAME = "DivideBucket";

    public enum FolderName {
        PROFILE, POST, ORDER
    }

    static public String uploadFile(MultipartFile multipartFile, FolderName foldername, String filename) {
        try {
            InputStream is = multipartFile.getInputStream();
            return upload(is, (long) is.available(), foldername, filename);
        } catch (IOException e) {
            throw new RestApiException(FileIOErrorCode.FILE_IO_ERROR);
        }
    }
    static public String uploadProfileImgFromUrl(String profileImgUrl, String filename) {
        URL url;
        BufferedImage bufferedImage;
        try {
            url = new URL(profileImgUrl);
            bufferedImage = ImageIO.read(url);
        } catch (IOException e) {
            throw new RestApiException(FileIOErrorCode.KAKAO_ERROR);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "jpeg", os);
        } catch (IOException e) {
            throw new RestApiException(FileIOErrorCode.FILE_IO_ERROR);
        }

        InputStream is = new ByteArrayInputStream(os.toByteArray());

        return upload(is, Long.valueOf(os.size()), FolderName.PROFILE, filename);
    }

    static private String upload(InputStream is, Long fileSize, FolderName foldername, String filename) {
        ConfigFileReader.ConfigFile config;
        try {
            config = ConfigFileReader.parse(CONFIGURATION_FILE_PATH, PROFILE);
        } catch (IOException e) {
            throw new RestApiException(FileIOErrorCode.OCI_ERROR);
        }

        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(REGION);

        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);
        GetNamespaceResponse namespaceResponse = client.getNamespace(GetNamespaceRequest.builder().build());
        String namespaceName = namespaceResponse.getValue();

        final String bucketName = BUCKET_NAME;
        final String objectName = foldername + "/" + filename;
        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
                        .build();

        UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(is, fileSize).allowOverwrite(true).build(request);

        uploadManager.upload(uploadDetails);

        try {
            client.close();
        } catch (Exception e) {
            throw new RestApiException(FileIOErrorCode.OCI_ERROR);
        }

        return String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                REGION.getRegionId(),
                namespaceName,
                BUCKET_NAME,
                objectName
        );
    }
}

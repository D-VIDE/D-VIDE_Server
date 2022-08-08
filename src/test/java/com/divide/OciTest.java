package com.divide;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.*;
import com.oracle.bmc.objectstorage.responses.GetBucketResponse;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OciTest {
    @Test
    public void initTest() throws IOException {
        final String bucket = "DivideBucket";
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse("src/main/resources/config/ociConfig", "DEFAULT");

        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_CHUNCHEON_1);

        GetNamespaceResponse namespaceResponse = client.getNamespace(GetNamespaceRequest.builder().build());

        String namespaceName = namespaceResponse.getValue();

        List<GetBucketRequest.Fields> fieldsList = new ArrayList<>(2);
        fieldsList.add(GetBucketRequest.Fields.ApproximateCount);
        fieldsList.add(GetBucketRequest.Fields.ApproximateSize);
        GetBucketRequest request =
                GetBucketRequest.builder()
                        .namespaceName(namespaceName)
                        .bucketName(bucket)
                        .fields(fieldsList)
                        .build();

        GetBucketResponse response = client.getBucket(request);
        System.out.println(response.getBucket().getName());
    }

    @Test
    public void uploadObjectTest() throws Exception {
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse("src/main/resources/config/ociConfig", "DEFAULT");
        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_CHUNCHEON_1);

        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);
        GetNamespaceResponse namespaceResponse = client.getNamespace(GetNamespaceRequest.builder().build());
        final String namespaceName = namespaceResponse.getValue();
        final String bucketName = "DivideBucket";
        final String objectName = "tech.jpg";
        final String contentType = "image/jpeg";
        final File body = new File("src/main/resources/static/sample.jpg");

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
                        .contentType(contentType)
                        .build();

        UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(body).allowOverwrite(true).build(request);

        UploadManager.UploadResponse response = uploadManager.upload(uploadDetails);
        System.out.println(response);

        client.close();
    }
}

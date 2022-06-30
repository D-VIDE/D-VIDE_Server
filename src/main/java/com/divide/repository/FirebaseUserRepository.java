package com.divide.repository;

import com.divide.dto.request.SignupRequest;
import com.divide.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class FirebaseUserRepository implements UserRepository {
    public static final String COLLECTION_NAME = "users";

    @Override
    public List<User> getUsers() throws ExecutionException, InterruptedException {
        List<User> list = new ArrayList<>();
        // db 가져오기
        Firestore db = FirestoreClient.getFirestore();
        // collection 가져오기
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        // document 가져오기
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(User.class));
        }
        return list;
    }

    @Override
    public String signup(User user) throws ExecutionException, InterruptedException {
        // db 가져오기
        Firestore db = FirestoreClient.getFirestore();
        // collection에 저장
        DocumentReference saveResult = db.collection(COLLECTION_NAME).add(user).get();
        // id 가져오기
        String savedId = saveResult.getId();

        // id를 table에 update
        Map<String, String> map = new HashMap<>();
        map.put("id", savedId);
        saveResult.update("id", savedId);

        return savedId;
    }

    @Override
    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        // db 가져오기
        Firestore db = FirestoreClient.getFirestore();
        // collection 가져오기
        List<QueryDocumentSnapshot> foundUsers = db.collection(COLLECTION_NAME).whereEqualTo("email", email).get().get().getDocuments();
        if (foundUsers.isEmpty()) {
            return Optional.empty();
        }
        User user = foundUsers.get(0).toObject(User.class);
        return Optional.of(user);
    }
}

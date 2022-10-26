package com.lwt.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lwt.go4lunch.model.ChatMessageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChatMessageRepository {



    public static final String COLLECTION_CHAT = "chat";

    private final FirebaseFirestore db;
    private final String userId;

    public ChatMessageRepository(FirebaseFirestore db, FirebaseAuth auth){
        this.db = db;
        this.userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<List<ChatMessageModel>> getChatMessages(String workmateId) {

        MutableLiveData<List<ChatMessageModel>> chatMessagesMutableLiveData = new MutableLiveData<>();

        // CREATE A LIST OF USER TO SORT THEM,
        // IT WILL GIVE THE INDEX FOR DOCUMENT AND COLLECTION
        List<String> ids = new ArrayList<>();
        ids.add(userId);
        ids.add(workmateId);
        Collections.sort(ids);

        db.collection(COLLECTION_CHAT)
                .document(ids.get(0) + "_" + ids.get(1))
                .collection(ids.get(0) + "_" + ids.get(1))
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("messages error", error.getMessage());
                        return;
                    }
                    assert value != null;
                    setChatMessages(value, chatMessagesMutableLiveData);
                });
        return chatMessagesMutableLiveData;
    }

    public void setChatMessages(QuerySnapshot value, MutableLiveData<List<ChatMessageModel>> liveData){
        Set<ChatMessageModel> chatMessages = new HashSet<>();
        for (DocumentChange document : value.getDocumentChanges()) {
            if (document.getType() == DocumentChange.Type.ADDED) {
                chatMessages.add(document.getDocument().toObject(ChatMessageModel.class));
            }
        }
        List<ChatMessageModel> chatMessageModels = new ArrayList<>(chatMessages);
        liveData.setValue(chatMessageModels);
    }
}

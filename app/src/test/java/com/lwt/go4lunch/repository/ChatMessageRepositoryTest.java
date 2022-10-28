package com.lwt.go4lunch.repository;

import static com.lwt.go4lunch.repository.ChatMessageRepository.COLLECTION_CHAT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.model.ChatMessageModel;
import com.lwt.go4lunch.pojo.NearbySearchResults;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ChatMessageRepositoryTest {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock public FirebaseFirestore db;
    @Mock  public FirebaseAuth auth;
    @Mock  public FirebaseUser user;

    @Mock CollectionReference mainCollectionReference;
    @Mock CollectionReference collectionReference;
    @Mock DocumentReference documentReference;
    @Mock DocumentChange document1;
    @Mock QueryDocumentSnapshot document1Snapshot;
    @Mock QuerySnapshot querySnapshot;


    @Captor
    ArgumentCaptor<EventListener<QuerySnapshot>> eventSnapshotListenerCaptor;

    private ChatMessageRepository chatMessageRepository;

    @Before
    public void setUp() throws Exception {
        when(auth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn("0");
        chatMessageRepository = new ChatMessageRepository(db, auth);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldReturnChatMessages() throws InterruptedException {
        when(db.collection(COLLECTION_CHAT)).thenReturn(mainCollectionReference);
        when(mainCollectionReference.document("0_1")).thenReturn(documentReference);
        when(documentReference.collection("0_1")).thenReturn(collectionReference);

        ChatMessageModel message1 = new ChatMessageModel("message", "sender", "date", 0L);
        when(document1.getDocument()).thenReturn(document1Snapshot);
        when(document1.getType()).thenReturn(DocumentChange.Type.ADDED);
        when(document1Snapshot.toObject(ChatMessageModel.class)).thenReturn(message1);
        List<DocumentChange> list = new ArrayList<>();
        list.add(document1);
        when(querySnapshot.getDocumentChanges()).thenReturn(list);

        LiveData<List<ChatMessageModel>> messages = chatMessageRepository.getChatMessages("1");

        verify(collectionReference).addSnapshotListener(eventSnapshotListenerCaptor.capture());
        eventSnapshotListenerCaptor.getValue().onEvent(querySnapshot, null);

        List<ChatMessageModel> chatMessageModels = LiveDataTestUtil.getOrAwaitValue(messages);
        assertEquals(1, chatMessageModels.size());
    }
}
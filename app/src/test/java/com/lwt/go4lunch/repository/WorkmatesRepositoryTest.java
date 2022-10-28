package com.lwt.go4lunch.repository;

import static com.lwt.go4lunch.repository.FavoriteRestaurantsRepository.COLLECTION_FAVORITE_RESTAURANTS;
import static com.lwt.go4lunch.repository.FavoriteRestaurantsRepository.COLLECTION_USERS;
import static com.lwt.go4lunch.repository.WorkmatesRepository.USERS;
import static com.lwt.go4lunch.repository.WorkmatesRepository.USER_NAME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.model.UserModel;
import com.lwt.go4lunch.pojo.FavoriteRestaurant;
import com.lwt.go4lunch.ui.workmates.WorkMatesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesRepositoryTest {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    public FirebaseFirestore db;
    @Mock  public FirebaseAuth auth;
    @Mock  public FirebaseUser user;

    @Mock
    CollectionReference mainCollectionReference;
    @Mock
    DocumentChange document1;
    @Mock
    QueryDocumentSnapshot document1Snapshot;
    @Mock
    QuerySnapshot querySnapshot;
    @Mock
    Query query;

    @Captor
    ArgumentCaptor<EventListener<QuerySnapshot>> eventSnapshotListenerCaptor;

    WorkmatesRepository repository;

    @Before
    public void setUp() throws Exception {
        when(auth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn("0");
        repository = new WorkmatesRepository(db, auth);
    }

    @Test
    public void getWorkmates() throws InterruptedException {
        when(db.collection(USERS)).thenReturn(mainCollectionReference);
        when(mainCollectionReference.orderBy(USER_NAME)).thenReturn(query);

        UserModel user = new UserModel("1", "user", "avatar", "email");
        when(document1.getDocument()).thenReturn(document1Snapshot);
        when(document1.getType()).thenReturn(DocumentChange.Type.ADDED);
        when(document1Snapshot.toObject(UserModel.class)).thenReturn(user);
        List<DocumentChange> list = new ArrayList<>();
        list.add(document1);
        when(querySnapshot.getDocumentChanges()).thenReturn(list);

        LiveData<List<UserModel>> restaurants = repository.getWorkmates();

        verify(query).addSnapshotListener(eventSnapshotListenerCaptor.capture());
        eventSnapshotListenerCaptor.getValue().onEvent(querySnapshot, null);

        List<UserModel> userModels = LiveDataTestUtil.getOrAwaitValue(restaurants);
        assertEquals(1, userModels.size());
    }
}
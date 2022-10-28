package com.lwt.go4lunch.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.model.UserWhoMadeRestaurantChoice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UsersWhoMadeRestaurantChoiceRepositoryTest {

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

    @Captor
    ArgumentCaptor<EventListener<QuerySnapshot>> eventSnapshotListenerCaptor;

    Clock clock;
    UsersWhoMadeRestaurantChoiceRepository repository;

    @Before
    public void setUp() throws Exception {
//        when(auth.getCurrentUser()).thenReturn(user);
//        when(user.getUid()).thenReturn("0");
        clock = Clock.systemDefaultZone();
        repository = new UsersWhoMadeRestaurantChoiceRepository(db, clock);
    }

    @Test
    public void getWorkmatesWhoMadeRestaurantChoice() throws InterruptedException {
        LocalDate today = LocalDate.now(clock);

        when(db.collection(today.toString())).thenReturn(mainCollectionReference);

        UserWhoMadeRestaurantChoice restaurant = new UserWhoMadeRestaurantChoice("0", "restaurant", "0", "user", "address");
        when(document1.getDocument()).thenReturn(document1Snapshot);
        when(document1.getType()).thenReturn(DocumentChange.Type.ADDED);
        when(document1Snapshot.toObject(UserWhoMadeRestaurantChoice.class)).thenReturn(restaurant);
        List<DocumentChange> list = new ArrayList<>();
        list.add(document1);
        when(querySnapshot.getDocumentChanges()).thenReturn(list);

        LiveData<List<UserWhoMadeRestaurantChoice>> restaurants = repository.getWorkmatesWhoMadeRestaurantChoice();

        verify(mainCollectionReference).addSnapshotListener(eventSnapshotListenerCaptor.capture());
        eventSnapshotListenerCaptor.getValue().onEvent(querySnapshot, null);

        List<UserWhoMadeRestaurantChoice> restaurantList = LiveDataTestUtil.getOrAwaitValue(restaurants);
        assertEquals(1, restaurantList.size());
    }
}
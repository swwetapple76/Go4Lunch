package com.lwt.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lwt.go4lunch.model.UserWhoMadeRestaurantChoice;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersWhoMadeRestaurantChoiceRepository {

    private final FirebaseFirestore db;
    private final Clock clock;

    public UsersWhoMadeRestaurantChoiceRepository(FirebaseFirestore db, Clock clock) {
        this.clock = clock;
        this.db = db;
    }

    // GET WORKMATES WHO DECIDED WHERE THEY WOULD EAT
    public LiveData<List<UserWhoMadeRestaurantChoice>> getWorkmatesWhoMadeRestaurantChoice() {
        MutableLiveData<List<UserWhoMadeRestaurantChoice>> userModelMutableLiveData = new MutableLiveData<>();

        LocalDate today = LocalDate.now(clock);

        List<UserWhoMadeRestaurantChoice> usersWithRestaurant = new ArrayList<>();

        db.collection(today.toString())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
//                        Log.e("restaurant choice error", error.getMessage());
                        return;
                    }
                    //  List<WorkmateWhoMadeRestaurantChoice> userWithRestaurant = new ArrayList<>();

                    assert value != null;
                    for (DocumentChange document : value.getDocumentChanges()) {
//                        Log.d("pipo", "onEvent() called with: value = [" + document.getDocument().toObject(UserWhoMadeRestaurantChoice.class) + "], error = [" + null + "]");
                        if (document.getType() == DocumentChange.Type.ADDED) {

                            usersWithRestaurant.add(document.getDocument().toObject(UserWhoMadeRestaurantChoice.class));

                        } else if (document.getType() == DocumentChange.Type.MODIFIED) {

                            for (int i = 0; i < usersWithRestaurant.size(); i++) {
                                if (usersWithRestaurant.get(i).getUserId().equals(document.getDocument().toObject(UserWhoMadeRestaurantChoice.class).getUserId())) {
                                    usersWithRestaurant.remove(usersWithRestaurant.get(i));
                                }

                            }

                            usersWithRestaurant.add(document.getDocument().toObject(UserWhoMadeRestaurantChoice.class));

                        } else if (document.getType() == DocumentChange.Type.REMOVED) {


                            usersWithRestaurant.remove(document.getDocument().toObject(UserWhoMadeRestaurantChoice.class));

                        }
                    }
                    userModelMutableLiveData.setValue(usersWithRestaurant);

                });
        return userModelMutableLiveData;

    }
}

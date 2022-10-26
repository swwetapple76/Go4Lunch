package com.lwt.go4lunch.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lwt.go4lunch.pojo.FavoriteRestaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteRestaurantsRepository {

    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_FAVORITE_RESTAURANTS = "favorite restaurants";

    private final FirebaseFirestore db;
    private final String userId;

    public FavoriteRestaurantsRepository(FirebaseFirestore db, FirebaseAuth auth){
        this.db = db;
        this.userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    // GET THE FAVORITE RESTAURANTS FOR CURRENT USER
    public LiveData<List<FavoriteRestaurant>> getFavoriteRestaurants() {

        MutableLiveData<List<FavoriteRestaurant>> favoriteRestaurantsLiveData = new MutableLiveData<>();

        db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(COLLECTION_FAVORITE_RESTAURANTS)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("no favorite", error.getMessage());
                        return;
                    }
                    List<FavoriteRestaurant> favoriteRestaurants = new ArrayList<>();

                    assert value != null;
                    for (DocumentChange document : value.getDocumentChanges()) {
                        if (document.getType() == DocumentChange.Type.ADDED) {

                            favoriteRestaurants.add(document.getDocument().toObject(FavoriteRestaurant.class));

                        } else if (document.getType() == DocumentChange.Type.REMOVED) {

                            favoriteRestaurants.remove(document.getDocument().toObject(FavoriteRestaurant.class));

                        }
                    }
                    favoriteRestaurantsLiveData.setValue(favoriteRestaurants);

                });
        return favoriteRestaurantsLiveData;

    }
}

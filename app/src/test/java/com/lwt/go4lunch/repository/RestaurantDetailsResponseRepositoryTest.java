package com.lwt.go4lunch.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lwt.go4lunch.GoogleMapApiMock;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.pojo.RestaurantDetailsResult;
import com.lwt.go4lunch.retrofit.GoogleMapsApi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantDetailsResponseRepositoryTest {

    @Mock
    public Application application;

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    public GoogleMapsApi googleMapsApi;
    RestaurantDetailsResponseRepository repository;

    @Before
    public void setUp() throws Exception {
        googleMapsApi = new GoogleMapApiMock();
        repository = new RestaurantDetailsResponseRepository(googleMapsApi, application);
    }

    @Test
    public void getRestaurantDetailsLiveData() throws InterruptedException {
        when(application.getString(R.string.restaurant_details_fields)).thenReturn("formatted_phone_number,opening_hours,website,place_id");
        String restaurantId = "restaurant_id";
        LiveData<RestaurantDetailsResult> liveData = repository.getRestaurantDetailsLiveData(restaurantId);
        RestaurantDetailsResult result = LiveDataTestUtil.getOrAwaitValue(liveData);
        assertEquals(restaurantId, result.getDetailsResult().getPlaceId());
    }
}
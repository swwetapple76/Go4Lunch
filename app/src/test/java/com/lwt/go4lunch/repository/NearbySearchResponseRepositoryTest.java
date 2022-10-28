package com.lwt.go4lunch.repository;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lwt.go4lunch.GoogleMapApiMock;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.pojo.NearbySearchResults;
import com.lwt.go4lunch.pojo.Predictions;
import com.lwt.go4lunch.retrofit.GoogleMapsApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NearbySearchResponseRepositoryTest {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    public GoogleMapsApi googleMapsApi;
    public NearbySearchResponseRepository nearbySearchResponseRepository;

    @Before
    public void setUp() throws Exception {
        googleMapsApi = new GoogleMapApiMock();
        nearbySearchResponseRepository = new NearbySearchResponseRepository(googleMapsApi);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRestaurantListLiveData() throws InterruptedException {
        LiveData<NearbySearchResults> predictionsLiveData = nearbySearchResponseRepository.getRestaurantListLiveData("", "", "");
        NearbySearchResults nearbySearchResults = LiveDataTestUtil.getOrAwaitValue(predictionsLiveData);
        assertEquals(1, nearbySearchResults.getResults().size());
    }
}
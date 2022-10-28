package com.lwt.go4lunch.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lwt.go4lunch.GoogleMapApiMock;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.pojo.Predictions;
import com.lwt.go4lunch.retrofit.GoogleMapsApi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AutocompleteRepositoryTest {

    @Mock
    public Application application;

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    public GoogleMapsApi googleMapsApi;
    public AutocompleteRepository autocompleteRepository;

    @Before
    public void setUp() throws Exception {
        googleMapsApi = new GoogleMapApiMock();
        autocompleteRepository = new AutocompleteRepository(googleMapsApi, application);
    }

    @Test
    public void shouldReturnRestaurantListWhenNameIsEntered() throws InterruptedException {
        when(application.getString(R.string.autocomplete_type)).thenReturn("restaurant");
        when(application.getString(R.string.radius)).thenReturn("1000");

        LiveData<Predictions> predictionsLiveData = autocompleteRepository.getAutocompleteResultListLiveData("", "");
        Predictions predictions = LiveDataTestUtil.getOrAwaitValue(predictionsLiveData);
        assertEquals(1, predictions.getPredictions().size());
    }
}
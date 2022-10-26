package com.lwt.go4lunch.ui.mapview;

import static junit.framework.TestCase.assertEquals;

import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.type.LatLng;
import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.model.UserWhoMadeRestaurantChoice;
import com.lwt.go4lunch.pojo.NearbySearchResults;
import com.lwt.go4lunch.pojo.Restaurant;
import com.lwt.go4lunch.repository.LocationRepository;
import com.lwt.go4lunch.repository.UserSearchRepository;
import com.lwt.go4lunch.repository.UsersWhoMadeRestaurantChoiceRepository;
import com.lwt.go4lunch.usecase.GetNearbySearchResultsUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    Location location;
    @Mock
    LocationRepository locationRepository;
    @Mock
    GetNearbySearchResultsUseCase nearbySearchResultsUseCase;
    @Mock
    UsersWhoMadeRestaurantChoiceRepository usersWhoMadeRestaurantChoiceRepository;
    @Mock
    UserSearchRepository userSearchRepository;


    @Test
    public void getMapViewStateLiveData() throws InterruptedException {
        when(location.getLatitude()).thenReturn(0.0);
        when(location.getAltitude()).thenReturn(0.0);
        when(locationRepository.getLocationLiveData()).thenReturn(new MutableLiveData<>(location));

        List<Restaurant> restaurants = new ArrayList<>();
        when(nearbySearchResultsUseCase.invoke()).thenReturn(new MutableLiveData<>(new NearbySearchResults(restaurants)));

        List<UserWhoMadeRestaurantChoice> users = new ArrayList<>();
        when(usersWhoMadeRestaurantChoiceRepository.getWorkmatesWhoMadeRestaurantChoice()).thenReturn(new MutableLiveData<>(users));

        String user = "";
        when(userSearchRepository.getUsersSearchLiveData()).thenReturn(new MutableLiveData<>(user));

        MapViewModel mapViewModel = new MapViewModel(locationRepository, nearbySearchResultsUseCase, usersWhoMadeRestaurantChoiceRepository, userSearchRepository);


        LiveData<MapViewState> liveData = mapViewModel.getMapViewStateLiveData();
        MapViewState state = LiveDataTestUtil.getOrAwaitValue(liveData);
        assertEquals(15f, state.getZoom());
        assertEquals(0.0, state.getLatLng().latitude);
    }
}
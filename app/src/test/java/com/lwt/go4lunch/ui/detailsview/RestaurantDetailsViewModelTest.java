package com.lwt.go4lunch.ui.detailsview;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.model.UserModel;
import com.lwt.go4lunch.model.UserWhoMadeRestaurantChoice;
import com.lwt.go4lunch.pojo.Close;
import com.lwt.go4lunch.pojo.FavoriteRestaurant;
import com.lwt.go4lunch.pojo.Geometry;
import com.lwt.go4lunch.pojo.Open;
import com.lwt.go4lunch.pojo.OpeningHours;
import com.lwt.go4lunch.pojo.Periods;
import com.lwt.go4lunch.pojo.Photo;
import com.lwt.go4lunch.pojo.Restaurant;
import com.lwt.go4lunch.pojo.RestaurantDetails;
import com.lwt.go4lunch.pojo.RestaurantDetailsResult;
import com.lwt.go4lunch.pojo.RestaurantLatLngLiteral;
import com.lwt.go4lunch.repository.FavoriteRestaurantsRepository;
import com.lwt.go4lunch.repository.UsersWhoMadeRestaurantChoiceRepository;
import com.lwt.go4lunch.repository.WorkmatesRepository;
import com.lwt.go4lunch.usecase.ClickOnChoseRestaurantButtonUseCase;
import com.lwt.go4lunch.usecase.ClickOnFavoriteRestaurantUseCase;
import com.lwt.go4lunch.usecase.GetCurrentUserIdUseCase;
import com.lwt.go4lunch.usecase.GetNearbySearchResultsByIdUseCase;
import com.lwt.go4lunch.usecase.GetRestaurantDetailsResultsByIdUseCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantDetailsViewModelTest {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock Application application;
    @Mock GetNearbySearchResultsByIdUseCase getNearbySearchResultsByIdUseCase;
    @Mock GetRestaurantDetailsResultsByIdUseCase getRestaurantDetailsResultsByIdUseCase;
    @Mock WorkmatesRepository workmatesRepository;
    @Mock FavoriteRestaurantsRepository favoriteRestaurantsRepository;
    @Mock UsersWhoMadeRestaurantChoiceRepository mUsersWhoMadeRestaurantChoiceRepository;
    @Mock GetCurrentUserIdUseCase getCurrentUserIdUseCase;
    @Mock ClickOnChoseRestaurantButtonUseCase clickOnChoseRestaurantButtonUseCase;
    @Mock ClickOnFavoriteRestaurantUseCase clickOnFavoriteRestaurantUseCase;

    MockedStatic<Color> color;

    RestaurantDetailsViewModel viewModel;



    @Before
    public void setUp() throws Exception {
        viewModel = new RestaurantDetailsViewModel(application, getNearbySearchResultsByIdUseCase, getRestaurantDetailsResultsByIdUseCase,
                mUsersWhoMadeRestaurantChoiceRepository, workmatesRepository, favoriteRestaurantsRepository, getCurrentUserIdUseCase,
                clickOnChoseRestaurantButtonUseCase, clickOnFavoriteRestaurantUseCase);

        color = Mockito.mockStatic(Color.class);
        color.when(() -> Color.parseColor("#000000")).thenReturn(0);
        color.when(() -> Color.parseColor("#69F0AE")).thenReturn(0);
    }
    @After
    public void tearDown(){
        color.close();
    }

    public void init() {
        when(application.getString(R.string.background_black)).thenReturn("#000000");
//        when(application.getString(R.string.background_green)).thenReturn("#69F0AE");
        when(application.getString(R.string.api_url)).thenReturn("https://maps.googleapis.com/maps/api/place");
        when(application.getString(R.string.and_key)).thenReturn("&key=");
//        when(application.getString(R.string.phone_number_unavailable)).thenReturn("Phone number unavailable");
//        when(application.getString(R.string.website_unavailable)).thenReturn("https://www.google.com/");
        when(application.getString(R.string.photo_reference)).thenReturn("photo?maxwidth=300&photo_reference=");
        when(application.getString(R.string.is_joining)).thenReturn("is joining !");

        String placeId = "0";

        Restaurant restaurant = new Restaurant("restaurantId", "restaurantName", "restaurantAddress",
                new ArrayList<Photo>(), new Geometry(new RestaurantLatLngLiteral(0.0, 0.0)),
                new OpeningHours(false, new ArrayList<Periods>()),
                0.0, 0, false, "", "");
        when(getNearbySearchResultsByIdUseCase.invoke(placeId)).thenReturn(new MutableLiveData<>(restaurant));

        Open open = new Open(0, "8");
        Close close = new Close(0, "18");
        List<Periods> periods = new ArrayList<>();
        periods.add(new Periods(close, open));
        OpeningHours openingHours = new OpeningHours(true, periods);
        RestaurantDetailsResult result = new RestaurantDetailsResult(new RestaurantDetails(placeId, openingHours, "", ""));
        when(getRestaurantDetailsResultsByIdUseCase.invoke("0")).thenReturn(new MutableLiveData<>(result));

        List<UserWhoMadeRestaurantChoice> userWhoMadeRestaurantChoiceList = new ArrayList<>();
        userWhoMadeRestaurantChoiceList.add(new UserWhoMadeRestaurantChoice("restaurantId", "restaurantName", "userId", "username", "restaurantAddress"));
        LiveData<List<UserWhoMadeRestaurantChoice>> userWhoMadeRestaurantChoiceLiveData = new MutableLiveData<>(userWhoMadeRestaurantChoiceList);
        when(mUsersWhoMadeRestaurantChoiceRepository.getWorkmatesWhoMadeRestaurantChoice()).thenReturn(userWhoMadeRestaurantChoiceLiveData);

        List<UserModel> userModelList = new ArrayList<>();
        userModelList.add(new UserModel("userId", "username", "avatar", "email"));
        LiveData<List<UserModel>> workMatesLiveData = new MutableLiveData<>(userModelList);
        when(workmatesRepository.getWorkmates()).thenReturn(workMatesLiveData);

        List<FavoriteRestaurant> favoriteRestaurantList = new ArrayList<>();
        favoriteRestaurantList.add(new FavoriteRestaurant("restaurantId", "restaurantName"));
        LiveData<List<FavoriteRestaurant>> favoriteRestaurantLiveData = new MutableLiveData<>(favoriteRestaurantList);
        when(favoriteRestaurantsRepository.getFavoriteRestaurants()).thenReturn(favoriteRestaurantLiveData);

        viewModel.init(placeId);

    }

    @Test
    public void getRestaurantDetailsViewStateLiveData() throws InterruptedException {
        init();

        RestaurantDetailsViewState restaurantDetailsViewStateLiveData = LiveDataTestUtil.getOrAwaitValue(viewModel.getRestaurantDetailsViewStateLiveData());
        assertEquals("restaurantName", restaurantDetailsViewStateLiveData.getDetailsRestaurantName());
        assertEquals("restaurantId", restaurantDetailsViewStateLiveData.getDetailsRestaurantId());
    }

    @Test
    public void getWorkmatesWhoChoseThisRestaurant() throws InterruptedException {
        init();
        List<RestaurantDetailsWorkmatesViewState> restaurantDetailsWorkmatesViewStateList = LiveDataTestUtil.getOrAwaitValue(viewModel.getWorkmatesWhoChoseThisRestaurant());
        assertEquals(1, restaurantDetailsWorkmatesViewStateList.size());
    }
}
package com.lwt.go4lunch.di;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lwt.go4lunch.MainActivityViewModel;
import com.lwt.go4lunch.MainApplication;
import com.lwt.go4lunch.repository.AutocompleteRepository;
import com.lwt.go4lunch.repository.ChatMessageRepository;
import com.lwt.go4lunch.repository.FavoriteRestaurantsRepository;
import com.lwt.go4lunch.repository.LocationRepository;
import com.lwt.go4lunch.repository.NearbySearchResponseRepository;
import com.lwt.go4lunch.repository.NotificationsRepository;
import com.lwt.go4lunch.repository.RestaurantDetailsResponseRepository;
import com.lwt.go4lunch.repository.UserSearchRepository;
import com.lwt.go4lunch.repository.UsersWhoMadeRestaurantChoiceRepository;
import com.lwt.go4lunch.repository.WorkmatesRepository;
import com.lwt.go4lunch.retrofit.GoogleMapsApi;
import com.lwt.go4lunch.ui.chat.ChatViewModel;
import com.lwt.go4lunch.ui.detailsview.RestaurantDetailsViewModel;
import com.lwt.go4lunch.ui.mapview.MapViewModel;
import com.lwt.go4lunch.ui.restaurants.RestaurantsViewModel;
import com.lwt.go4lunch.ui.setting.SettingViewModel;
import com.lwt.go4lunch.ui.workmates.WorkMatesViewModel;
import com.lwt.go4lunch.usecase.AddChatMessageToFirestoreUseCase;
import com.lwt.go4lunch.usecase.ClickOnChoseRestaurantButtonUseCase;
import com.lwt.go4lunch.usecase.ClickOnFavoriteRestaurantUseCase;
import com.lwt.go4lunch.usecase.GetCurrentUserIdUseCase;
import com.lwt.go4lunch.usecase.GetNearbySearchResultsByIdUseCase;
import com.lwt.go4lunch.usecase.GetNearbySearchResultsUseCase;
import com.lwt.go4lunch.usecase.GetPredictionsUseCase;
import com.lwt.go4lunch.usecase.GetRestaurantDetailsResultsByIdUseCase;
import com.lwt.go4lunch.usecase.GetRestaurantDetailsResultsUseCase;

import java.time.Clock;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
    private final Application application;
    private final Context context;

    private final LocationRepository locationRepository;
    private final WorkmatesRepository workmatesRepository;
    private final UserSearchRepository mUserSearchRepository;
    private final FavoriteRestaurantsRepository favoriteRestaurantsRepository;
    private final UsersWhoMadeRestaurantChoiceRepository mUsersWhoMadeRestaurantChoiceRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationsRepository notificationsRepository;

    private final GetNearbySearchResultsUseCase getNearbySearchResultsUseCase;
    private final GetNearbySearchResultsByIdUseCase getNearbySearchResultsByIdUseCase;
    private final GetRestaurantDetailsResultsUseCase getRestaurantDetailsResultsUseCase;
    private final GetRestaurantDetailsResultsByIdUseCase getRestaurantDetailsResultsByIdUseCase;
    private final GetPredictionsUseCase getPredictionsUseCase;
    private final GetCurrentUserIdUseCase getCurrentUserIdUseCase;
    private final AddChatMessageToFirestoreUseCase addChatMessageToFirestoreUseCase;
    private final ClickOnChoseRestaurantButtonUseCase clickOnChoseRestaurantButtonUseCase;
    private final ClickOnFavoriteRestaurantUseCase clickOnFavoriteRestaurantUseCase;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();

                }
            }
        }
        return factory;

    }

    public ViewModelFactory() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleMapsApi googleMapsApi = retrofit.create(GoogleMapsApi.class);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        this.application = MainApplication.getApplication();
        this.context = application.getApplicationContext();

        NearbySearchResponseRepository nearbySearchResponseRepository =
                new NearbySearchResponseRepository(
                        googleMapsApi
                );
        RestaurantDetailsResponseRepository restaurantDetailsResponseRepository =
                new RestaurantDetailsResponseRepository(
                        googleMapsApi,
                        application);
        AutocompleteRepository autocompleteRepository =
                new AutocompleteRepository(
                        googleMapsApi,
                        application);
        this.locationRepository =
                new LocationRepository();
        this.workmatesRepository =
                new WorkmatesRepository();
        this.mUserSearchRepository =
                new UserSearchRepository();
        this.favoriteRestaurantsRepository =
                new FavoriteRestaurantsRepository();
        this.mUsersWhoMadeRestaurantChoiceRepository =
                new UsersWhoMadeRestaurantChoiceRepository(Clock.systemDefaultZone());
        this.chatMessageRepository =
                new ChatMessageRepository();
        this.notificationsRepository =
                new NotificationsRepository(context);

        this.getNearbySearchResultsUseCase =
                new GetNearbySearchResultsUseCase(
                        locationRepository,
                        nearbySearchResponseRepository);
        this.getNearbySearchResultsByIdUseCase =
                new GetNearbySearchResultsByIdUseCase(
                        locationRepository,
                        nearbySearchResponseRepository);
        this.getRestaurantDetailsResultsUseCase =
                new GetRestaurantDetailsResultsUseCase(
                        locationRepository,
                        nearbySearchResponseRepository,
                        restaurantDetailsResponseRepository);
        this.getRestaurantDetailsResultsByIdUseCase =
                new GetRestaurantDetailsResultsByIdUseCase(
                        restaurantDetailsResponseRepository
                );
        this.getPredictionsUseCase =
                new GetPredictionsUseCase(
                        locationRepository,
                        autocompleteRepository);
        this.getCurrentUserIdUseCase =
                new GetCurrentUserIdUseCase();
        this.addChatMessageToFirestoreUseCase =
                new AddChatMessageToFirestoreUseCase(
                        firebaseFirestore,
                        firebaseAuth,
                        Clock.systemDefaultZone());
        this.clickOnChoseRestaurantButtonUseCase =
                new ClickOnChoseRestaurantButtonUseCase(
                        firebaseFirestore,
                        firebaseAuth,
                        Clock.systemDefaultZone());
        this.clickOnFavoriteRestaurantUseCase =
                new ClickOnFavoriteRestaurantUseCase(firebaseFirestore);
    }


    // CREATE INSTANCE FOR EACH VIEWMODEL
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            return (T) new RestaurantsViewModel(
                    application,
                    locationRepository,
                    getNearbySearchResultsUseCase,
                    getRestaurantDetailsResultsUseCase,
                    mUsersWhoMadeRestaurantChoiceRepository,
                    mUserSearchRepository,
                    Clock.systemDefaultZone());
        } else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(
                    locationRepository,
                    getNearbySearchResultsUseCase,
                    mUsersWhoMadeRestaurantChoiceRepository,
                    mUserSearchRepository);
        } else if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(
                    application,
                    locationRepository,
                    getPredictionsUseCase,
                    mUserSearchRepository,
                    mUsersWhoMadeRestaurantChoiceRepository,
                    getCurrentUserIdUseCase
            );
        } else if (modelClass.isAssignableFrom(RestaurantDetailsViewModel.class)) {
            return (T) new RestaurantDetailsViewModel(
                    application,
                    getNearbySearchResultsByIdUseCase,
                    getRestaurantDetailsResultsByIdUseCase,
                    mUsersWhoMadeRestaurantChoiceRepository,
                    workmatesRepository,
                    favoriteRestaurantsRepository,
                    getCurrentUserIdUseCase,
                    clickOnChoseRestaurantButtonUseCase,
                    clickOnFavoriteRestaurantUseCase);
        } else if (modelClass.isAssignableFrom(WorkMatesViewModel.class)) {
            return (T) new WorkMatesViewModel(
                    application,
                    workmatesRepository,
                    mUsersWhoMadeRestaurantChoiceRepository
            );
        } else if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(
                    chatMessageRepository,
                    getCurrentUserIdUseCase,
                    addChatMessageToFirestoreUseCase
            );
        } else if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(
                    notificationsRepository,
                    context,
                    Clock.systemDefaultZone()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");

    }
}

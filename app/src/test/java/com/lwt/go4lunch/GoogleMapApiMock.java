package com.lwt.go4lunch;

import com.lwt.go4lunch.pojo.Close;
import com.lwt.go4lunch.pojo.NearbySearchResults;
import com.lwt.go4lunch.pojo.Open;
import com.lwt.go4lunch.pojo.OpeningHours;
import com.lwt.go4lunch.pojo.Periods;
import com.lwt.go4lunch.pojo.PlaceAutocompleteStructuredFormat;
import com.lwt.go4lunch.pojo.Prediction;
import com.lwt.go4lunch.pojo.Predictions;
import com.lwt.go4lunch.pojo.Restaurant;
import com.lwt.go4lunch.pojo.RestaurantDetails;
import com.lwt.go4lunch.pojo.RestaurantDetailsResult;
import com.lwt.go4lunch.retrofit.GoogleMapsApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapApiMock implements GoogleMapsApi {
    @Override
    public Call<NearbySearchResults> searchRestaurant(String key, String type, String location, String radius) {
        return new Call<NearbySearchResults>() {
            @Override
            public Response<NearbySearchResults> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<NearbySearchResults> callback) {
                List<Restaurant> restaurants = new ArrayList<>();
                restaurants.add(new Restaurant());
                callback.onResponse(this, Response.success(new NearbySearchResults(restaurants)));
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<NearbySearchResults> clone() {
                return this;
            }

            @Override
            public Request request() {
                return null;
            }

            @Override
            public Timeout timeout() {
                return null;
            }
        };
    }

    @Override
    public Call<RestaurantDetailsResult> searchRestaurantDetails(String key, String place_id, String fields) {
        return new Call<RestaurantDetailsResult>() {
            @Override
            public Response<RestaurantDetailsResult> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<RestaurantDetailsResult> callback) {
                Open open = new Open(0, "8");
                Close close = new Close(0, "18");
                List<Periods> periods = new ArrayList<>();
                periods.add(new Periods(close, open));
                OpeningHours openingHours = new OpeningHours(true, periods);
                RestaurantDetailsResult result = new RestaurantDetailsResult(new RestaurantDetails(place_id, openingHours, "", ""));
                callback.onResponse(this, Response.success(result));
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<RestaurantDetailsResult> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

            @Override
            public Timeout timeout() {
                return null;
            }
        };
    }

    @Override
    public Call<Predictions> autocompleteResult(String key, String type, String location, String radius, String input) {
        return new Call<Predictions>() {
            @Override
            public Response<Predictions> execute() throws IOException { return null; }

            @Override
            public void enqueue(Callback<Predictions> callback) {
                if(type.equalsIgnoreCase("restaurant")) {
                    List<Prediction> predictionsList = new ArrayList<>();
                    predictionsList.add(new Prediction("", "", new PlaceAutocompleteStructuredFormat("")));
                    Predictions predictions = new Predictions(predictionsList, "");
                    callback.onResponse(this, Response.success(predictions));
                }
                else {
                    callback.onFailure(this, new Exception("bad place type"));
                }
            }
            @Override
            public boolean isExecuted() { return false; }

            @Override
            public void cancel() {}

            @Override
            public boolean isCanceled() { return false; }

            @Override
            public Call<Predictions> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

            @Override
            public Timeout timeout() {
                return null;
            }
        };
    }
}

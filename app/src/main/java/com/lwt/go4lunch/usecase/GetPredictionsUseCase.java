package com.lwt.go4lunch.usecase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.lwt.go4lunch.pojo.Predictions;
import com.lwt.go4lunch.repository.AutocompleteRepository;
import com.lwt.go4lunch.repository.LocationRepository;

public class GetPredictionsUseCase {

    private final LocationRepository locationRepository;
    private final AutocompleteRepository autocompleteRepository;

    // RETRIEVE AUTOCOMPLETE PREDICTIONS RESULTS FROM LOCATION
    public GetPredictionsUseCase(
            LocationRepository locationRepository,
            AutocompleteRepository autocompleteRepository) {

        this.locationRepository = locationRepository;
        this.autocompleteRepository = autocompleteRepository;

    }

    public LiveData<Predictions> invoke(String text) {
        return Transformations.switchMap(locationRepository.getLocationLiveData(), input -> {
            String locationAsText = input.getLatitude() + "," + input.getLongitude();
            Log.i("go4lunch", "location: " + locationAsText);
            if(text.length() < 2){

            }
            return Transformations.map(autocompleteRepository.getAutocompleteResultListLiveData(
                    locationAsText,
                    text),
                    input1 -> input1);

        });
    }
}

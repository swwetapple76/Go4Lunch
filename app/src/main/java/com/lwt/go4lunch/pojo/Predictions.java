package com.lwt.go4lunch.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Predictions {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions;
    @SerializedName("status")
    @Expose
    private String status;

    public Predictions(List<Prediction> prediction, String status) {
        this.predictions = prediction;
        this.status = status;
    }


    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

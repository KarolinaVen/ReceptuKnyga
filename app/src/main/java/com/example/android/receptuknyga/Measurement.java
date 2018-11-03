package com.example.android.receptuknyga;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Measurement {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int measurementId;
    String name;
    int measurementSystemId;


    @NonNull
    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(@NonNull int measurementId){
        this.measurementId = measurementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMeasurementSystemId() {
        return measurementSystemId;
    }

    public void setMeasurementSystemId(int measurementSystemId) {
        this.measurementSystemId = measurementSystemId;
    }

}


package com.example.android.receptuknyga;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MeasurementSystem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int measurementSystemId;
    String measurementSystemName;

    public static final int ID_METRIC = 1;
    public static final int ID_US = 2;

    @NonNull
    public int getMeasurementSystemId() {
        return measurementSystemId;
    }

    public void setMeasurementSystemId(@NonNull int measurementSystemId) {
        this.measurementSystemId = measurementSystemId;
    }

    public String getMeasurementSystemName() {
        return measurementSystemName;
    }

    public void setMeasurementSystemName(String measurementSystemName) {
        this.measurementSystemName = measurementSystemName;
    }
}

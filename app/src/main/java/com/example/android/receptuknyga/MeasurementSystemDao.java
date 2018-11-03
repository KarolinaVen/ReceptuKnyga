package com.example.android.receptuknyga;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

@Dao
public interface MeasurementSystemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasurementSystem(MeasurementSystem measurementSystem);
}

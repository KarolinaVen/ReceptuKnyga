package com.example.android.receptuknyga;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasurement(Measurement measurement);

    @Query("SELECT name FROM Measurement WHERE measurementSystemId = :systemId")
    List<String> measurementListBySystemId(int systemId);

    @Query("SELECT measurementId FROM Measurement where name = :name")
    int nameToId(String name);

    @Query("SELECT name FROM MEASUREMENT WHERE measurementId = :measurementId")
    String idToName(int measurementId);

    @Query("SELECT measurementSystemId FROM Measurement WHERE measurementId = :measurementId")
    int getMeasurementSystemId(int measurementId);
}


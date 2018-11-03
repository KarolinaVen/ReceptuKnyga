package com.example.android.receptuknyga;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import static com.example.android.receptuknyga.MeasurementSystem.ID_US;
import static com.example.android.receptuknyga.MeasurementSystem.ID_METRIC;

@Database(entities = {Recipe.class, RecipeIngredient.class, RecipeDirections.class,
        Measurement.class, MeasurementSystem.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract RecipeDao recipeDao();

    public abstract RecipeIngredientsDao recipeIngredientsDao();

    public abstract RecipeDirectionsDao recipeDirectionsDao();

    public abstract MeasurementDao measurementDao();

    public abstract MeasurementSystemDao measurementSystemDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, "MyDB")
                    .allowMainThreadQueries()
                    .build();
            populateData();
        }
        return INSTANCE;
    }

    private static void populateData() {
        Measurement gram = new Measurement();
        gram.setMeasurementId(1);
        gram.setName("g");
        gram.setMeasurementSystemId(ID_METRIC);
        INSTANCE.measurementDao().insertMeasurement(gram);

        Measurement kilogram = new Measurement();
        kilogram.setMeasurementId(2);
        kilogram.setName("kg");
        kilogram.setMeasurementSystemId(ID_METRIC);
        INSTANCE.measurementDao().insertMeasurement(kilogram);

        Measurement milliliter = new Measurement();
        milliliter.setMeasurementId(3);
        milliliter.setName("ml");
        milliliter.setMeasurementSystemId(ID_METRIC);
        INSTANCE.measurementDao().insertMeasurement(milliliter);

        Measurement liter = new Measurement();
        liter.setMeasurementId(4);
        liter.setName("l");
        liter.setMeasurementSystemId(ID_METRIC);
        INSTANCE.measurementDao().insertMeasurement(liter);

        Measurement centimeter = new Measurement();
        centimeter.setMeasurementId(5);
        centimeter.setName("cm");
        centimeter.setMeasurementSystemId(ID_METRIC);
        INSTANCE.measurementDao().insertMeasurement(centimeter);

        Measurement pieceMetric = new Measurement();
        pieceMetric.setMeasurementId(6);
        pieceMetric.setName("vnt.");
        pieceMetric.setMeasurementSystemId(ID_METRIC);
        INSTANCE.measurementDao().insertMeasurement(pieceMetric);

        Measurement ounceUs = new Measurement();
        ounceUs.setMeasurementId(7);
        ounceUs.setName("oz.");
        ounceUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(ounceUs);

        Measurement poundUs = new Measurement();
        poundUs.setMeasurementId(8);
        poundUs.setName("lb");
        poundUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(poundUs);

        Measurement fluidOunceUs = new Measurement();
        fluidOunceUs.setMeasurementId(9);
        fluidOunceUs.setName("fl. oz.");
        fluidOunceUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(fluidOunceUs);

        Measurement cupUs = new Measurement();
        cupUs.setMeasurementId(10);
        cupUs.setName("st.");
        cupUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(cupUs);

        Measurement tablespoonUs = new Measurement();
        tablespoonUs.setMeasurementId(11);
        tablespoonUs.setName("v. š.");
        tablespoonUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(tablespoonUs);

        Measurement teaspoonUs = new Measurement();
        teaspoonUs.setMeasurementId(12);
        teaspoonUs.setName("a. š.");
        teaspoonUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(teaspoonUs);

        Measurement inchUs = new Measurement();
        inchUs.setMeasurementId(13);
        inchUs.setName("in.");
        inchUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(inchUs);

        Measurement pieceUs = new Measurement();
        pieceUs.setMeasurementId(14);
        pieceUs.setName("vnt.");
        pieceUs.setMeasurementSystemId(ID_US);
        INSTANCE.measurementDao().insertMeasurement(pieceUs);
    }
}
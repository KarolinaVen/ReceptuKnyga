package com.example.android.receptuknyga;

import java.util.ArrayList;
import java.util.List;

class Converters {

    private static List<Converter> converterList = null;

    static RecipeIngredient convertIngredient(RecipeIngredient ingredient) {
        if (converterList == null) {
            loadConverters();
        }

        int from = ingredient.getMeasurementId();

        RecipeIngredient recipeIngredientCopy = new RecipeIngredient();
        recipeIngredientCopy.setIngredientsId(ingredient.getIngredientsId());
        recipeIngredientCopy.setRecipeId(ingredient.getRecipeId());
        recipeIngredientCopy.setIngredientName(ingredient.getIngredientName());
        recipeIngredientCopy.setIngredientAmount(ingredient.getIngredientAmount());
        recipeIngredientCopy.setMeasurementId(ingredient.getMeasurementId());
        recipeIngredientCopy.setNumber(ingredient.getNumber());

        for (Converter converter : converterList) {
            if (converter.fromMeasurement == from) {
                return converter.convert(recipeIngredientCopy);
            }
        }
        return null;
    }

    private static void loadConverters() {
        converterList = new ArrayList<>();

        Converter gramConverter = new Converter(1) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                double gramAmountToOunce = ingredient.getIngredientAmount() / 28.35;
                if (gramAmountToOunce > 16) {
                    ingredient.setMeasurementId(8);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 454);
                    return ingredient;
                } else {
                    ingredient.setMeasurementId(7);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 28.35);
                    return ingredient;
                }
            }
        };
        converterList.add(gramConverter);

        Converter ounceConverter = new Converter(7) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                double ounceAmountToGram = ingredient.getIngredientAmount() * 28.35;
                if (ounceAmountToGram > 1000) {
                    ingredient.setMeasurementId(2);
                    ingredient.setIngredientAmount((ingredient.getIngredientAmount() * 28.35) / 1000);
                    return ingredient;
                } else {
                    ingredient.setMeasurementId(1);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 28.35);
                    return ingredient;
                }
            }
        };
        converterList.add(ounceConverter);

        Converter kilogramConverter = new Converter(2) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(8);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 0.45359237);
                return ingredient;
            }
        };
        converterList.add(kilogramConverter);

        Converter poundConverter = new Converter(8) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(2);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 0.45359237);
                return ingredient;
            }
        };
        converterList.add(poundConverter);

        Converter milliliterConverter = new Converter(3) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                double milliliterAmountToFluidOunce = ingredient.getIngredientAmount() / 29.57; //fluidOunce
                if (milliliterAmountToFluidOunce < 1) {
                    double milliliterToTablespoon = ingredient.getIngredientAmount() / 14.79; //milliliterToTablespoon
                    if (milliliterToTablespoon < 1) {
                        ingredient.setMeasurementId(12);
                        ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 4.93); //toTeaspoon
                        return ingredient;
                    } else if (milliliterToTablespoon > 16) {
                        ingredient.setMeasurementId(10);
                        ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 236.59); //toCup
                        return ingredient;
                    } else {
                        ingredient.setMeasurementId(11);
                        ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 14.79); //toTablespoon
                        return ingredient;
                    }
                }
                if (milliliterAmountToFluidOunce >= 1 && milliliterAmountToFluidOunce < 8) {
                    ingredient.setMeasurementId(11);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 14.79); //toTablespoon
                    return ingredient;
                } else if (milliliterAmountToFluidOunce >= 8) {
                    ingredient.setMeasurementId(10);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 236.59); //toCup
                    return ingredient;
                } else {
                    ingredient.setMeasurementId(9);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 29.57); //toFluidOunce
                    return ingredient;
                }
            }
        };

        converterList.add(milliliterConverter);

        Converter fluidOunceConverter = new Converter(9) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                double ingredientMilliliters = ingredient.getIngredientAmount() * 29.57;
                if (ingredientMilliliters > 1000) {
                    ingredient.setMeasurementId(4);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 33.814);
                    return ingredient;
                } else {
                    ingredient.setMeasurementId(3);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 29.57);
                    return ingredient;
                }
            }
        };
        converterList.add(fluidOunceConverter);

        Converter literConverter = new Converter(4) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(10);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 0.236);
                return ingredient;
            }
        };
        converterList.add(literConverter);

        Converter centimeterConverter = new Converter(5) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(13);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() / 2.54);
                return ingredient;
            }
        };
        converterList.add(centimeterConverter);

        Converter inchConverter = new Converter(13) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(5);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 2.54);
                return ingredient;
            }
        };
        converterList.add(inchConverter);

        Converter metricCountConverter = new Converter(6) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(14);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount());
                return ingredient;
            }
        };
        converterList.add(metricCountConverter);

        Converter usCountConverter = new Converter(14) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(6);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount());
                return ingredient;
            }
        };
        converterList.add(usCountConverter);

        Converter cupConverter = new Converter(10) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                double cupAmount = ingredient.getIngredientAmount() * 236.59;
                if (cupAmount > 1000) {
                    ingredient.setMeasurementId(4);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 0.236);
                    return ingredient;
                } else {
                    ingredient.setMeasurementId(3);
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 236.59);
                    return ingredient;
                }
            }
        };
        converterList.add(cupConverter);

        Converter tablespoonConverter = new Converter(11) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(3);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 14.79);
                return ingredient;
            }
        };
        converterList.add(tablespoonConverter);

        Converter teaspoonConverter = new Converter(12) {
            @Override
            RecipeIngredient convert(RecipeIngredient ingredient) {
                ingredient.setMeasurementId(3);
                ingredient.setIngredientAmount(ingredient.getIngredientAmount() * 4.93);
                return ingredient;
            }
        };
        converterList.add(teaspoonConverter);
    }

    abstract private static class Converter {
        int fromMeasurement;

        public Converter(int fromMeasurement) {
            this.fromMeasurement = fromMeasurement;
        }

        abstract RecipeIngredient convert(RecipeIngredient ingredient);
    }
}

package com.example.recipesapp.Objects;

import com.example.recipesapp.Constants.NutritionFactsNames;

public class NutritionFacts {

//    private String idNutrition;
    private NutritionFactsNames nameNutrition;
    private String imgNutrition;

    public NutritionFacts() { }

    public NutritionFacts( NutritionFactsNames nameNutrition, String imgNutrition) {
//        this.idNutrition = idNutrition;
        this.nameNutrition = nameNutrition;
        this.imgNutrition = imgNutrition;
    }

//    public String getIdNutrition() {
//        return idNutrition;
//    }

//    public NutritionFacts setIdNutrition(String idNutrition) {
//        this.idNutrition = idNutrition;
//        return this;
//    }

// ------- Builder --------

    public NutritionFactsNames getNameNutrition() {
        return nameNutrition;
    }

    public NutritionFacts setNameNutrition(NutritionFactsNames nameNutrition) {
        this.nameNutrition = nameNutrition;
        return this;
    }

    public String getImgNutrition() {
        return imgNutrition;
    }

    public NutritionFacts setImgNutrition(String imgNutrition) {
        this.imgNutrition = imgNutrition;
        return this;
    }

    @Override
    public String toString() {
        return "NutritionFacts{" +
//                "idNutrition='" + idNutrition + '\'' +
                ", nameNutrition=" + nameNutrition +
                ", imgNutrition='" + imgNutrition + '\'' +
                '}';
    }
}

package com.example.recipesapp.Objects;

import com.example.recipesapp.Constants.UnitOfMeasure;

public class Ingredient {

//    private String idIngredient;
    private String nameIngredient;
    private double amount;
    private UnitOfMeasure unitOfMeasure;

    public Ingredient() {}

    public Ingredient( String nameIngredient, double amount, UnitOfMeasure unitOfMeasure) {
//        this.idIngredient = idIngredient;
        this.nameIngredient = nameIngredient;
        this.amount = amount;
        this.unitOfMeasure = unitOfMeasure;
    }


//    public String getIdIngredient() {
//        return idIngredient;
//    }

//    public Ingredient setIdIngredient(String idIngredient) {
//        this.idIngredient = idIngredient;
//        return this;
//    }

    public String getNameIngredient() {
        return nameIngredient;
    }

    public Ingredient setNameIngredient(String nameIngredient) {
        this.nameIngredient = nameIngredient;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Ingredient setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Ingredient setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    // plus amount
    public void addAmount(double amountToAdd) {
        if (this.amount + amountToAdd >= 99.9) {
            this.amount = 99.9;
            return;
        } else
            this.amount += amountToAdd;
    }

    // minus amount
    public void subtractAmount(double amountToSub) {
        if (this.amount == 0.0)
            return;
        else if (this.amount - amountToSub <= 0.0) {
            this.amount = 0.0;
            return;
        } else
            this.amount -= amountToSub;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
//                "idIngredient='" + idIngredient + '\'' +
                ", nameIngredient='" + nameIngredient + '\'' +
                ", amount=" + amount +
                ", unitOfMeasure=" + unitOfMeasure +
                '}';
    }
}

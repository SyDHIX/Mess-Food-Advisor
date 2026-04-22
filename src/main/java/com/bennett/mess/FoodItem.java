package com.bennett.mess;

public class FoodItem {
    private String name;
    private int calories;
    private String category;
    private double rating;
    private boolean staple;

    public FoodItem(String name, int calories, String category, double rating, boolean staple) {
        this.name = name;
        this.calories = calories;
        this.category = category;
        this.rating = rating;
        this.staple = staple;
    }

    public String getName()     { return name; }
    public int getCalories()    { return calories; }
    public String getCategory() { return category; }
    public double getRating()   { return rating; }
    public boolean isStaple()   { return staple; }

    @Override
    public String toString() {
        return name + " (" + calories + " Kcal)";
    }
}

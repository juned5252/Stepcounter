package com.qc.stepcounter.fitnessRecyclerView;

/**
 * Created by mohammadnaz on 12/6/17.
 */

public class FitnessData {
    private int imageView;
    private int steps;
    private float calories;
    private int distance;



    public FitnessData(int steps, float calories, int distance) {
        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.imageView=imageView;
    }

    public int getSteps() {
        return steps;
    }

    public float getCalories() {
        return calories;
    }

    public int getDistance() {
        return distance;
    }

}

package com.qc.stepcounter.RestAPI;

/**
 * Created by mohammadnaz on 12/16/17.
 */

public class Currently {


    private int time;
    private String summary;
    private String icon;
    private double temperature;
    private double windSpeed;
    private double windGust;

    public int getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindGust() {
        return windGust;
    }
}



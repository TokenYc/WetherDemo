package com.example.a24706.wetherdemo.WeatherView;

/**
 * Created by 24706 on 2017/3/21.
 */

public class WeatherHourEntity {

    private int temperature;
    private String weather_code;
    private String time;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(String weather_code) {
        this.weather_code = weather_code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

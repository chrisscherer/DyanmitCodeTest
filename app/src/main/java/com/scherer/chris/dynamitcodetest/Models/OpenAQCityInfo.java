package com.scherer.chris.dynamitcodetest.Models;

/**
 * Created by christopherscherer on 9/7/17.
 *
 * City information provided by the OpenAQ.org cities endpoint
 */

public class OpenAQCityInfo {

    //region Fields
    private String city;

    private String country;

    private Integer count;

    private Integer locations;
    //endregion

    //region Getters

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getLocations() {
        return locations;
    }

    //endregion

    //region Setters

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setLocations(Integer locations) {
        this.locations = locations;
    }

    //endregion
}

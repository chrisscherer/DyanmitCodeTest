package com.scherer.chris.dynamitcodetest.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by christopherscherer on 9/7/17.
 *
 * Response object expected from the OpenAQ API - Cities endpoint
 */

public class OpenAQResponse {
    //region Fields
    @SerializedName("meta")
    private OpenAQMetaInfo metaInfo;

    @SerializedName("results")
    private OpenAQCityInfo[] cityResults;
    //endregion

    //region Getters

    public OpenAQMetaInfo getMetaInfo() {
        return metaInfo;
    }

    public OpenAQCityInfo[] getCityResults() {
        return cityResults;
    }

    //endregion

    //region Setters

    public void setMetaInfo(OpenAQMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public void setCityResults(OpenAQCityInfo[] cityResults) {
        this.cityResults = cityResults;
    }

    //endregion
}

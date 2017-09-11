package com.scherer.chris.dynamitcodetest.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by christopherscherer on 9/7/17.
 *
 * Meta info that is part of the OpenAQ Cities response
 */

public class OpenAQMetaInfo {
    //region Fields
    @SerializedName("name")
    private String Name;

    @SerializedName("license")
    private String License;

    @SerializedName("website")
    private String Website;

    @SerializedName("page")
    private Integer Page;

    @SerializedName("limit")
    private Integer Limit;

    @SerializedName("found")
    private Integer Found;
    //endregion

    //region Getters

    public String getName() {
        return Name;
    }

    public String getLicense() {
        return License;
    }

    public String getWebsite() {
        return Website;
    }

    public Integer getPage() {
        return Page;
    }

    public Integer getLimit() {
        return Limit;
    }

    public Integer getFound() {
        return Found;
    }

    //endregion

    //region Setters

    public void setName(String name) {
        Name = name;
    }

    public void setLicense(String license) {
        License = license;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public void setPage(Integer page) {
        Page = page;
    }

    public void setLimit(Integer limit) {
        Limit = limit;
    }

    public void setFound(Integer found) {
        Found = found;
    }

    //endregion
}

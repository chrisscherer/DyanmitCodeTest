package com.scherer.chris.dynamitcodetest.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by christopherscherer on 9/7/17.
 *
 * Request object to get a list of cities and the number of air quality measurements they have on OpenAQ.org
 */

public class OpenAQRequest {

    //region Fields
    @SerializedName("country")
    private String Country;

    @SerializedName("order_by")
    private String[] OrderBy;

    @SerializedName("sort")
    private String[] Sort;

    @SerializedName("limit")
    private Integer Limit;

    @SerializedName("page")
    private Integer Page;
    //endregion

    //region Getters

    public String getCountry() {
        return Country;
    }

    public String[] getOrderBy() {
        return OrderBy;
    }

    public String[] getSort() {
        return Sort;
    }

    public Integer getLimit() {
        return Limit;
    }

    public Integer getPage() {
        return Page;
    }

    //endregion

    //region Setters

    public void setCountry(String country) {
        Country = country;
    }

    public void setOrderBy(String[] orderBy) {
        OrderBy = orderBy;
    }

    public void setSort(String[] sort) {
        Sort = sort;
    }

    public void setLimit(Integer limit) {
        Limit = limit;
    }

    public void setPage(Integer page) {
        Page = page;
    }

    //endregion
}

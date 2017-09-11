package com.scherer.chris.dynamitcodetest;

import android.content.Context;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.scherer.chris.dynamitcodetest.DataAdapters.AQCityDataAdapter;
import com.scherer.chris.dynamitcodetest.DataAdapters.EndlessRecyclerViewScrollListener;
import com.scherer.chris.dynamitcodetest.Fragments.GlobalErrorDialogFragment;
import com.scherer.chris.dynamitcodetest.Models.OpenAQCityInfo;
import com.scherer.chris.dynamitcodetest.Models.OpenAQRequest;
import com.scherer.chris.dynamitcodetest.Models.OpenAQResponse;
import com.scherer.chris.dynamitcodetest.Utilities.Constants;
import com.scherer.chris.dynamitcodetest.Utilities.GsonRequest;
import com.scherer.chris.dynamitcodetest.Utilities.RequestHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    /**
     * A list to keep our filtered response info from OpenAQ's API
     */
    List<OpenAQCityInfo> cityInfo = new ArrayList<>();

    /**
     * The RecyclerView used to display our info
     */
    RecyclerView cityInfoRecyclerView;

    /**
     * DataAdapter for formatting our data for display
     */
    AQCityDataAdapter dataAdapter;

    /**
     * A reference to this activity for passing into functions that have a different 'this' context
     */
    Context activityContext = this;

    /**
     * A dialog fragment for displaying any errors we encounter to the user.
     */
    GlobalErrorDialogFragment errorDialogFragment = new GlobalErrorDialogFragment();

    /**
     * Current page index to make sure we're paging correctly
     */
    int currentPageIndex = 1;

    /**
     * Limit on the number of data entries we want to keep in memory
     */
    int dataLimit = 300;

    /**
     * Tie in to the OnCreate function of the activity lifecycle
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInfoRecyclerView = findViewById(R.id.AQCityDataRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        cityInfoRecyclerView.setLayoutManager(linearLayoutManager);
        cityInfoRecyclerView.addOnScrollListener(setupScrollListener(linearLayoutManager));

        makeRequest(currentPageIndex);
    }

    /**
     * Make a HTTP Get request to the OpenAQ API. It will return data from their cities API which we will then format for display
     *
     * The API will be doing the sort for us. We are taking advantage of their pagination as well. Default page size is 100 but can be
     * defined as a parameter in our request string
     *
     * @param newPageIndex The current page we're requesting. The OpenAQ API has built in pagination which we will take advantage of
     */
    protected void makeRequest(Integer newPageIndex) {
        String uri = Constants.OPEN_AQ_CITIES_ENDPOINT + "?sort=desc&order_by=count&page=" + newPageIndex;

        GsonRequest<OpenAQRequest, OpenAQResponse> request = new GsonRequest<>(
                Request.Method.GET,
                uri,
                null,
                OpenAQResponse.class,
                null,
                response -> {
                    //My phone is sdk version 23 so I can't use Java 8 features, but ideally I'd be able to use the stream solution below
//                    List<OpenAQCityInfo> filteredResponse = Arrays.stream(response.getCityResults()).filter(ci -> ci.getCount() > 10000).collect(Collectors.toList());

                    List<OpenAQCityInfo> filteredResponse = filterOpenAQCityInfo(response.getCityResults());

                    cityInfo.addAll(filteredResponse);

                    //This code was an attempt to get infinite scrolling to use proper paging and only keep 300 records in memory at a time
                    //to avoid issues on older phones. I couldn't quite get it to work as the data adapter seemed to be retaining any data added
                    //and not properly removing data.
//                    if(cityInfo.size() >= dataLimit){
//                        if(newPageIndex > currentPageIndex) {
//                            cityInfo = cityInfo.subList(100, 300);
//                            dataAdapter.notifyItemRangeRemoved(0, 100);
//
//                            cityInfo.addAll(filteredResponse);
//                            dataAdapter.notifyItemRangeInserted(200, 100);
//                        }
//                        else {
//                            cityInfo = cityInfo.subList(0, 200);
//                            dataAdapter.notifyItemRangeRemoved(200, 100);
//                            filteredResponse.addAll(cityInfo);
//                            cityInfo = filteredResponse;
//                            dataAdapter.notifyItemRangeInserted(0, 100);
//                        }
//                    }
                    if(dataAdapter == null){
                        dataAdapter = new AQCityDataAdapter(activityContext, cityInfo);
                        cityInfoRecyclerView.setAdapter(dataAdapter);
                    }
                    else {
                        dataAdapter.notifyItemRangeInserted(cityInfo.size() - filteredResponse.size(), filteredResponse.size());
                    }

                    currentPageIndex  = newPageIndex;
                },
                e -> {
                    if(getFragmentManager().findFragmentByTag("getRequestFailure") == null) {
                        errorDialogFragment.setErrorMessage("Something went wrong, please try again later!");
                        errorDialogFragment.show(getFragmentManager(), "getRequestFailure");
                    }
                }
        );

        RequestHandler.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Setup a scroll listener for our RecyclerView so we can infinite scroll as close to seamlessly as possible
     *
     * @param linearLayoutManager The layout manager shared with our RecyclerView
     * @return A scroll listener to attach to our RecyclerView
     */
    protected EndlessRecyclerViewScrollListener setupScrollListener(LinearLayoutManager linearLayoutManager) {
        return new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                makeRequest(page);
            }
        };
    }

    //Ideally, this would take a field to filter on and a criteria to meet. In java 8 I could provide a lambda.
    //While it would also probably be possible in Java 7 using reflection, it's a messy process and given the problem statement,
    //Wouldn't be required without a change in project requirements

    /**
     * Filters our API response data for all entries that have more than 10,000 measurements
     *
     * @param data An array of domain objects (OpenAQCityInfo) that we want to filter
     *
     * @return Returns a list of the data that has been filtered
     */
    public static ArrayList<OpenAQCityInfo> filterOpenAQCityInfo(OpenAQCityInfo[] data) {
        Iterator<OpenAQCityInfo> itr = Arrays.asList(data).iterator();
        ArrayList<OpenAQCityInfo> filteredData = new ArrayList<>();

        while(itr.hasNext()) {
            OpenAQCityInfo ci = itr.next();
            if(ci.getCount() >= 10000) {
                filteredData.add(ci);
            }
        }

        return filteredData;
    }
}

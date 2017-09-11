package com.scherer.chris.dynamitcodetest.DataAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scherer.chris.dynamitcodetest.Models.OpenAQCityInfo;
import com.scherer.chris.dynamitcodetest.R;

import java.util.List;

/**
 * Created by christopherscherer on 9/7/17.
 *
 * Data adapter that processes our API response data for display using a recycler view
 */

public class AQCityDataAdapter extends RecyclerView.Adapter<AQCityDataAdapter.ViewHolder> {

    private List<OpenAQCityInfo> cityInfo;

    private Context context;

    protected int serverListSize = -1;

    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;

    public AQCityDataAdapter(Context context, List<OpenAQCityInfo> cityInfo) {
        this.context = context;
        this.cityInfo = cityInfo;
    }

    public void setServerListSize(int serverListSize) {
        this.serverListSize = serverListSize;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityInfoView = inflater.inflate(R.layout.aq_city_datarow, parent, false);

        return new ViewHolder(cityInfoView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position >= cityInfo.size()){
            TextView cityNameTextView = holder.cityNameTextView;
            cityNameTextView.setText("LOADING");

            TextView measurementCountTextView = holder.measurementCountTextView;
            measurementCountTextView.setText("LOADING");
        }
        else{
            OpenAQCityInfo info = cityInfo.get(position);

            TextView cityNameTextView = holder.cityNameTextView;
            cityNameTextView.setText(info.getCity());

            TextView measurementCountTextView = holder.measurementCountTextView;
            measurementCountTextView.setText(info.getCount().toString());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= cityInfo.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position : -1;
    }

    @Override
    public int getItemCount() {
        return cityInfo.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityNameTextView;
        public TextView measurementCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            cityNameTextView = itemView.findViewById(R.id.CityName);
            measurementCountTextView = itemView.findViewById(R.id.MeasurementCount);
        }
    }
}

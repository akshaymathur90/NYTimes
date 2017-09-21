package com.codepath.com.nytimes.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.com.nytimes.R;
import com.codepath.com.nytimes.databinding.NewsItemLayoutBinding;
import com.codepath.com.nytimes.models.Doc;

import java.util.List;

/**
 * Created by akshaymathur on 9/20/17.
 */

public class NewsItemRecyclerViewAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Doc> mData;
    public final static String TAG = "NewsItemAdapter";

    public NewsItemRecyclerViewAdapter(Context context, List<Doc> data){
        mContext = context;
        mData = data;
    }

    public void addData(List<Doc> data){
        int initalSize = mData.size()-1;
        mData.addAll(data);
        int finalSize = mData.size()-1;
        notifyItemRangeChanged(initalSize,finalSize);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v  = inflater.inflate(R.layout.news_item_layout,parent,false);

        return new ThumbnailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ThumbnailViewHolder viewHolder = (ThumbnailViewHolder) holder;
        final Doc doc = mData.get(position);
        viewHolder.mNewsItemLayoutBinding.tvHeadline.setText(doc.getHeadline().getMain());
        viewHolder.mNewsItemLayoutBinding.tvNewsSynopsis.setText(doc.getSnippet());
        Log.d(TAG,"news desk-->" +doc.getNewsDesk());
        viewHolder.mNewsItemLayoutBinding.tvNewsDesk.setText(doc.getNewsDesk());

        String baseURL = mContext.getString(R.string.image_base_url);
        String imageURL = baseURL + doc.getThumbnailURL();

        Log.d(TAG,"image url-->" +imageURL);

        Glide.with(mContext).load(imageURL).into(viewHolder.mNewsItemLayoutBinding.ivNewsThumbnail);

        viewHolder.mNewsItemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
                String url = doc.getWebUrl();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                // set toolbar color and/or setting custom actions before invoking build()
                builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                CustomTabsIntent customTabsIntent = builder.build();
                builder.addDefaultShareMenuItem();
                // and launch the desired Url with CustomTabsIntent.launchUrl()
                customTabsIntent.launchUrl(mContext, Uri.parse(url));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder{
        final NewsItemLayoutBinding mNewsItemLayoutBinding;
        public ThumbnailViewHolder(View view){
            super(view);
            mNewsItemLayoutBinding = NewsItemLayoutBinding.bind(view);
        }
    }
}

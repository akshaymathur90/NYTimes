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
import com.codepath.com.nytimes.databinding.NoThumbnailNewsItemBinding;
import com.codepath.com.nytimes.models.Doc;

import java.util.List;

/**
 * Created by akshaymathur on 9/20/17.
 */

public class NewsItemRecyclerViewAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Doc> mData;
    public final static String TAG = "NewsItemAdapter";
    private final static int THUMBNAILVIEW = 0;
    private final static int NOTHUMBNAILVIEW = 1;

    public NewsItemRecyclerViewAdapter(Context context, List<Doc> data){
        mContext = context;
        mData = data;
    }

    public void addData(List<Doc> data){
        int initalSize = mData.size();
        mData.addAll(data);
        int finalSize = mData.size();
        notifyItemRangeChanged(initalSize,finalSize);
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType){
            case THUMBNAILVIEW:
                View v  = inflater.inflate(R.layout.news_item_layout,parent,false);
                return new ThumbnailViewHolder(v);
            case NOTHUMBNAILVIEW:
                View v2  = inflater.inflate(R.layout.no_thumbnail_news_item,parent,false);
                return new NoThumbnailViewHolder(v2);
            default:
                View v3  = inflater.inflate(R.layout.no_thumbnail_news_item,parent,false);
                return new NoThumbnailViewHolder(v3);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Doc doc = mData.get(position);
        switch (holder.getItemViewType()){
            case THUMBNAILVIEW:
                ((ThumbnailViewHolder) holder).bind(doc);
                break;
            case NOTHUMBNAILVIEW:
                ((NoThumbnailViewHolder) holder).bind(doc);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Doc doc = mData.get(position);

        if(doc.getThumbnailURL()==null){
            return NOTHUMBNAILVIEW;
        }
        return THUMBNAILVIEW;

    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder{
        final NewsItemLayoutBinding mNewsItemLayoutBinding;
        public ThumbnailViewHolder(View view){
            super(view);
            mNewsItemLayoutBinding = NewsItemLayoutBinding.bind(view);
        }

        public void bind(final Doc doc){
            mNewsItemLayoutBinding.tvHeadline.setText(doc.getHeadline().getMain());
            mNewsItemLayoutBinding.tvNewsSynopsis.setText(doc.getSnippet());
            String newsDesk = doc.getNewsDesk();
            Log.d(TAG,"news desk-->" +newsDesk);
            if(newsDesk==null || newsDesk.equalsIgnoreCase("none")){
                mNewsItemLayoutBinding.tvNewsDesk.setVisibility(View.GONE);
            }else{
                mNewsItemLayoutBinding.tvNewsDesk.setText(doc.getNewsDesk());
            }


            String baseURL = mContext.getString(R.string.image_base_url);
            String imageURL = baseURL + doc.getThumbnailURL();

            Log.d(TAG,"image url-->" +imageURL);

            Glide.with(mContext).load(imageURL).into(mNewsItemLayoutBinding.ivNewsThumbnail);

            mNewsItemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
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
    }

    public class NoThumbnailViewHolder extends RecyclerView.ViewHolder{
        final NoThumbnailNewsItemBinding mNoThumbnailNewsItemBinding;
        public NoThumbnailViewHolder(View view){
            super(view);
            mNoThumbnailNewsItemBinding = NoThumbnailNewsItemBinding.bind(view);
        }
        public void bind(final Doc doc){
            mNoThumbnailNewsItemBinding.tvHeadline.setText(doc.getHeadline().getMain());
            mNoThumbnailNewsItemBinding.tvNewsSynopsis.setText(doc.getSnippet());
            String newsDesk = doc.getNewsDesk();
            Log.d(TAG,"news desk-->" +newsDesk);
            if(newsDesk==null || newsDesk.equalsIgnoreCase("none")){
                mNoThumbnailNewsItemBinding.tvNewsDesk.setVisibility(View.GONE);
            }else{
                mNoThumbnailNewsItemBinding.tvNewsDesk.setText(doc.getNewsDesk());
            }
            mNoThumbnailNewsItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
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
    }
}

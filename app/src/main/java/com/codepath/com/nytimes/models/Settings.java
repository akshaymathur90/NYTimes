package com.codepath.com.nytimes.models;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by akshaymathur on 9/19/17.
 */

public class Settings {

    private String mBeginDate;
    private String mSortOrder;
    private boolean mIsArtsChecked;
    private boolean mIsFashionChecked;
    private boolean mIsSportsChecked;

    public String getBeginDate() {
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyyMMdd");
        toFormat.setLenient(false);
        DateFormat fromFormat = SimpleDateFormat.getDateInstance();
        fromFormat.setLenient(false);
        try {
            return toFormat.format(fromFormat.parse(mBeginDate));
        } catch (ParseException e) {
            Log.d("Settings","Failed to convert Date");
            e.printStackTrace();
        }
        return null;
    }

    public void setBeginDate(String beginDate) {
        mBeginDate = beginDate;
    }

    public String getSortOrder() {
        return mSortOrder.toLowerCase();
    }

    public void setSortOrder(String sortOrder) {
        mSortOrder = sortOrder;
    }

    public boolean isArtsChecked() {
        return mIsArtsChecked;
    }

    public void setArtsChecked(boolean artsChecked) {
        mIsArtsChecked = artsChecked;
    }

    public boolean isFashionChecked() {
        return mIsFashionChecked;
    }

    public void setFashionChecked(boolean fashionChecked) {
        mIsFashionChecked = fashionChecked;
    }

    public boolean isSportsChecked() {
        return mIsSportsChecked;
    }

    public void setSportsChecked(boolean sportsChecked) {
        mIsSportsChecked = sportsChecked;
    }

    public String getNewsDesks(){

        if(isSportsChecked()||isFashionChecked()||isArtsChecked()) {
            String baseString = "news_desk:(";
            StringBuilder stringBuilder = new StringBuilder(baseString);
            if (isArtsChecked()) {
                stringBuilder.append("\"Arts\" ");
            }
            if (isFashionChecked()) {
                stringBuilder.append("\"Fashion & Style\" ");
            }
            if (isSportsChecked()) {
                stringBuilder.append("\"Sports\" ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        return null;
    }
}

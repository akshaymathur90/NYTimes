package com.codepath.com.nytimes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.codepath.com.nytimes.R;
import com.codepath.com.nytimes.models.Settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by akshaymathur on 9/19/17.
 */

public class SharedPreferenceUtils {

    public static SharedPreferences getSharePreferences(Context context){
        return context.getSharedPreferences(
                context.getString(R.string.sharedpref_file_name), Context.MODE_PRIVATE);
    }

    public static Settings getAllSettings(Context context){

        SharedPreferences sharedPreferences = getSharePreferences(context);
        Settings settings = new Settings();
        settings.setArtsChecked(sharedPreferences.getBoolean(context.getString(R.string.key_checkbox_arts),false));
        settings.setFashionChecked(sharedPreferences.getBoolean(context.getString(R.string.key_checkbox_fashion),false));
        settings.setSportsChecked(sharedPreferences.getBoolean(context.getString(R.string.key_checkbox_sports),false));
        settings.setBeginDate(sharedPreferences.getString(context.getString(R.string.key_begin_date),getDefaultDate()));
        settings.setSortOrder(sharedPreferences.getString(context.getString(R.string.key_sort_order),context.getString(R.string.label_newest)));
        settings.setChromeTab(sharedPreferences.getBoolean(context.getString(R.string.key_chrome_tab),true));

        return settings;

    }

    private static String getDefaultDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000,0,1);
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        return dateFormat.format(calendar.getTime());
    }

    public static boolean openInChrome(Context context){
        return getSharePreferences(context)
                .getBoolean(context.getString(R.string.key_chrome_tab),true);
    }
}

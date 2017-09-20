package com.codepath.com.nytimes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by akshaymathur on 9/19/17.
 */

public class SharedPreferenceUtils {

    public static SharedPreferences getSharePreferences(Context context){
        return context.getSharedPreferences(
                context.getString(R.string.sharedpref_file_name), Context.MODE_PRIVATE);
    }
}

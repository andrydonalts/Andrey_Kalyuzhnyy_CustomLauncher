package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by andry on 11.05.2016.
 */
public class Util {

    public static boolean isPortraitOrientation(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}

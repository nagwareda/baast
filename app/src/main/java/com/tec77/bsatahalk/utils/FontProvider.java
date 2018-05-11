package com.tec77.bsatahalk.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.Locale;

/**
 * Created by Mahmoud Shaeer on 26/05/2017.
 */

/**
 * used to get used font
 */
public class FontProvider {
    private static Typeface typeface;

    public static Typeface getFont(Context mContext){
        AssetManager am = mContext.getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/Cocon® Next Arabic-Light.otf"));

        return typeface;
    }
}

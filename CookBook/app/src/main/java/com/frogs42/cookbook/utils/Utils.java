package com.frogs42.cookbook.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Utils {
    public final static String LOG_TAG = "CookBook";

    public static Bitmap loadFromFile(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) { return null; }
            return BitmapFactory.decodeFile(filename);
        } catch (Exception e) {
            return null;
        }
    }
}

package com.benlewis.vitamintrackerapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ben on 13/04/2016.
 */
public class VitaminContract {

    interface VitaminColumns {
        String VITAMIN_NAME = "vitamin_name";
        String VITAMIN_DOSE_AMOUNT = "vitamin_dose_amount";
        String VITAMIN_DOSE_MULTIPLE = "vitamin_dose_multiple";
    }

    public static final String CONTENT_AUTHORITY = "com.benlewis.vitamintrackerapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_VITAMIN = "vitamin";

    public static final String[] TOP_LEVEL_PATHS = {
            PATH_VITAMIN
    };

    public static class Vitamin implements VitaminColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_VITAMIN).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vdn." + CONTENT_AUTHORITY + ".vitamin";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vdn." + CONTENT_AUTHORITY + ".vitamin";

        public static Uri buildVitaminUri(String vitaminId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(vitaminId).build();
        }

        public static String getVitaminId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}

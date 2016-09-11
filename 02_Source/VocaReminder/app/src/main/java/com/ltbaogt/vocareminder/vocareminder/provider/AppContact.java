package com.ltbaogt.vocareminder.vocareminder.provider;

import android.net.Uri;

/**
 * Created by MyPC on 10/09/2016.
 */
public class AppContact {

    public static final String AUTHORITY =
            "com.ltbaogt.vocareminder.app";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);
}

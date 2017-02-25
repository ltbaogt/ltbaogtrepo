package com.ryutb.myexample.util;

import android.os.Environment;

/**
 * Created by MyPC on 25/02/2017.
 */
public class CommonUtil {

    public static String getPodcastFolderPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + PodcastConstant.PODCAST_FOLDER;
    }

}

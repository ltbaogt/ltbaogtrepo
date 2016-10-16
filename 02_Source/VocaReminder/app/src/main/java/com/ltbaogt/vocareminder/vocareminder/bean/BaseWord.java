package com.ltbaogt.vocareminder.vocareminder.bean;

import android.content.Context;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.DownloadFileAsynTask;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.Mp3UrlForName;
import com.ltbaogt.vocareminder.vocareminder.utils.Utils;

import java.io.File;

/**
 * Created by MyPC on 16/10/2016.
 */
public abstract class BaseWord {

    public void playMp3IfNeed(Context ctx) {
        String wordName = Utils.mp3ForWordName(getWordName());
        if (Utils.checkMp3FileExisted(ctx, wordName)) {
            Utils.playMp3InLocal(ctx, wordName);
        } else {
            if (Utils.isOnline(ctx)) {
                if (Utils.isStringNullOrEmpty(getMp3Url())) {
                    Mp3UrlForName mp3Downloader = new Mp3UrlForName(ctx);
                    mp3Downloader.execute(getWordName());
                } else {
                    DownloadFileAsynTask downloader = new DownloadFileAsynTask(ctx);
                    downloader.execute(getMp3Url(), wordName);
                }
            } else {
                Utils.showToastAtBottom(ctx, R.string.dont_download_yet);
            }
        }
    }

    public abstract String getWordName();
    public abstract String getMp3Url();

    public void deleteMp3File(Context context) {
        String wordName = Utils.mp3ForWordName(getWordName());
        if (Utils.checkMp3FileExisted(context, wordName)) {
            File fi = Utils.mp3FileForName(context, wordName);
            fi.delete();
        }

    }
}

package com.ryutb.myexample.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryutb.myexample.R;
import com.ryutb.myexample.modal.Podcast;
import com.ryutb.myexample.util.PodcastConstant;

import java.util.ArrayList;

/**
 * Created by MyPC on 25/02/2017.
 */
public class PodcastAdapter extends BaseAdapter {
    ArrayList<Podcast> mPodcastList;
    MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();

    public PodcastAdapter(ArrayList<Podcast> list) {
        mPodcastList = list;
    }

    @Override
    public int getCount() {
        return mPodcastList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPodcastList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mPodcastList.indexOf(getItem(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            convertView = inflater.inflate(R.layout.podcast_list_item, viewGroup, false);
        }
        TextView podcastIndex = (TextView) convertView.findViewById(R.id.podcast_index);
        TextView podcastName = (TextView) convertView.findViewById(R.id.podcast_name);
        ImageView podcastImage = (ImageView) convertView.findViewById(R.id.podcast_image_art);

        Podcast currentPodcast = mPodcastList.get(position);

        myRetriever.setDataSource(currentPodcast.getPodcastPath());
        setArtwork(myRetriever, podcastImage);
        podcastIndex.setText(String.valueOf(currentPodcast.getIndex()) + PodcastConstant.LINE_MARK);
        podcastName.setText(currentPodcast.getPodcastName());
        return convertView;
    }

    public boolean setArtwork(MediaMetadataRetriever myRetriever, ImageView podcastImage) {
        byte[] artwork;

        artwork = myRetriever.getEmbeddedPicture();

        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            podcastImage.setImageBitmap(bMap);

            return true;
        } else {
            podcastImage.setImageBitmap(null);

            return false;
        }
    }

}

package com.ryutb.myexample.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ryutb.myexample.R;
import com.ryutb.myexample.adapter.PodcastAdapter;
import com.ryutb.myexample.modal.Podcast;
import com.ryutb.myexample.util.CommonUtil;
import com.ryutb.myexample.util.PodcastConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static String TAG = "MainActivity";
    private ArrayList<Podcast> mPodcastArrayList;
    private ListView mPodcastListView;
    private PodcastAdapter mPodcastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyAssets();
        mPodcastListView = (ListView) findViewById(R.id.podcast_list);
        mPodcastArrayList = new ArrayList<>();
        loadPodcastFromStorage();
        mPodcastAdapter = new PodcastAdapter(mPodcastArrayList);
        mPodcastListView.setAdapter(mPodcastAdapter);
        mPodcastListView.setOnItemClickListener(this);
    }

    /**
     * create podcast folder if need
     */
    private File createPodcastFolderExisting() {
        Log.d(TAG, ">>>createPodcastFolderExisting START");
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String podcastFolder = CommonUtil.getPodcastFolderPath();
            File pdcFolder = new File(podcastFolder);
            if (!pdcFolder.exists()) {
                try {
                    pdcFolder.mkdir();
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            } else {
                Log.d(TAG, ">>>createPodcastFolderExisting podcast folder created");
            }
            return pdcFolder;
        }
        Log.d(TAG, ">>>createPodcastFolderExisting END");
        return null;

    }

    private void loadPodcastFromStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String podcastFolder = CommonUtil.getPodcastFolderPath();
            File pdFolderObject = new File(podcastFolder);
            if (pdFolderObject.exists() && mPodcastArrayList != null) {
                //Folder podcast existed
                mPodcastArrayList.clear();
                File[] podcastFiles = pdFolderObject.listFiles();
                int index = 1;
                for (File file : podcastFiles) {
                    Podcast podcast = new Podcast();
                    podcast.setIndex(index);
                    podcast.setPodcastName(file.getName());
                    podcast.setPodcastPath(file.getAbsolutePath());
                    mPodcastArrayList.add(podcast);
                    index++;
                }
            } else {
                Log.d(TAG, ">>>loadPodcastFromStorage podcast folder doesn't exist");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Podcast podcast = (Podcast) adapterView.getItemAtPosition(i);

        Intent playerIntent = new Intent(this, PodcastPlayerActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(PodcastConstant.EXTRA_PODCAST_OBJECT, podcast);
        playerIntent.putExtras(b);
        startActivity(playerIntent);
    }

    private void copyAssets() {
        File podcastFolder = createPodcastFolderExisting();
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            //Log.i("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(podcastFolder, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                //Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}

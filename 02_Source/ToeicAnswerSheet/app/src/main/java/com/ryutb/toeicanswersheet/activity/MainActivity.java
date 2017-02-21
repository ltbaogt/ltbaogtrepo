package com.ryutb.toeicanswersheet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.ryutb.toeicanswersheet.R;
import com.ryutb.toeicanswersheet.adapter.SentenceAdapter;
import com.ryutb.toeicanswersheet.customview.MyPDFView;
import com.ryutb.toeicanswersheet.model.Answer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyPDFView.OnLongPressPDFView {

    private static final String TAG = "MainActivity";
    private static final int REQ_CODE = 1;
    GridView mListeningSectionListView;
    ArrayList<Answer> mArrayList = new ArrayList<>();
    MyPDFView mPdfViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 200; i++) {
            Answer a = new Answer();
            a.setSentenceNumber(i);
            a.setChoiceLetter(-1);
            mArrayList.add(a);
        }
        mListeningSectionListView = (GridView) findViewById(R.id.answer_sheet_listening_section);
        mListeningSectionListView.setAdapter(new SentenceAdapter(mArrayList));

        mPdfViewer = (MyPDFView) findViewById(R.id.pdf_viewer);
        mPdfViewer.useBestQuality(true);
        mPdfViewer.setOnLongPressPDFView(this);
    }

    @Override
    public void onLongPressPDF() {
        Intent fileChooser = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooser.setType("application/pdf");
        startActivityForResult(fileChooser, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    String path = uri.toString();
                    Log.d(TAG, ">>>onActivityResult path=" + path);
                    mPdfViewer.fromUri(uri).defaultPage(0).load();
                } catch (Exception e) {

                }
            }
        }

    }
}

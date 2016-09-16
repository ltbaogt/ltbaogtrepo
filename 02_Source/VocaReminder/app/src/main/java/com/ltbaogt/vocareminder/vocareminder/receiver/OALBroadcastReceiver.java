package com.ltbaogt.vocareminder.vocareminder.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.handler.ReceiverHandler;
import com.ltbaogt.vocareminder.vocareminder.listener.OALGestureListener;
import com.ltbaogt.vocareminder.vocareminder.listener.OnClickStartApp;
import com.ltbaogt.vocareminder.vocareminder.listener.OnTouchDismissListener;
import com.ltbaogt.vocareminder.vocareminder.listener.OpenPanelListener;
import com.ltbaogt.vocareminder.vocareminder.listener.ShowMeaningCheckChanged;
import com.ltbaogt.vocareminder.vocareminder.provider.ProviderWrapper;

import java.lang.ref.WeakReference;

/**
 * Created by My PC on 04/08/2016.
 */

public class OALBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = Define.TAG + "OALBroadcastReceiver";
    private WeakReference<View> mReminderLayout;

    Context mContext;
    private Typeface mBoldtypeface;
    private Typeface mRegulartypeface;
    private  ReceiverHandler mHandler;

    public OALBroadcastReceiver(Context ctx) {
        Log.d(TAG, ">>>OALBroadcastReceiver init");
        mContext = ctx;
        AssetManager assetManager = mContext.getApplicationContext().getAssets();
        mBoldtypeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_BOLD);
        mRegulartypeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_REGILAR);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>>onReceive START");
        String action = intent.getAction();
        int callState = getTelephoneService().getCallState();
        Log.d(TAG, ">>>onReceive callState= " + callState);
        if (Intent.ACTION_SCREEN_ON.equals(action) && (callState == TelephonyManager.CALL_STATE_IDLE)
                || Define.VOCA_ACTION_OPEN_VOCA_REMINDER.equals(action)) {
            //if (mReminderLayout != null) return;
            //Setup Gesture action when reminder layout inflated
            setupContentView();
            setupReminderEvent();
            ProviderWrapper providerWrapper = new ProviderWrapper(mContext.getApplicationContext());
            int dismissTime = providerWrapper.getDismissTime();
            if (dismissTime > 0) {
                Log.d(TAG, ">>>onReceive sendMessage to dismiss reminder after " + dismissTime);
                mHandler = new ReceiverHandler(mContext.getApplicationContext());
                mHandler.sendEmptyMessageDelayed(Define.HANDLER_WHAT_AUTO_DISMISS,dismissTime);
            }

        } else if (Define.VOCA_ACTION_CLOSE_VOCA_REMINDER.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
            try {
                Log.d(TAG, ">>>onReceive call dismissReminderLayout");
                dismissReminderLayout();
            } catch (NullPointerException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        Log.d(TAG, ">>>onReceive END");
    }

    private void dismissReminderLayout() {
        Log.d(TAG, ">>>dismissReminderLayout");
        try {
            if (getWeakPreferenceMainLayout() != null) {
                getWindowManager().removeViewImmediate(getWeakPreferenceMainLayout());
                getWeakPreferenceMainLayout().setOnTouchListener(null);
                mReminderLayout = null;
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler = null;
                }
            }
        } catch (IllegalArgumentException e) {

        }
    }

    private WindowManager getWindowManager() {
        return ((WindowManager) mContext.getApplicationContext().getSystemService(Service.WINDOW_SERVICE));
    }

    private TelephonyManager getTelephoneService() {
        return ((TelephonyManager) mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
    }

    private LayoutInflater getInflaterService() {
        return ((LayoutInflater) mContext.getApplicationContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE));
    }


    private void setupContentView() {
        mReminderLayout = new WeakReference<>(getInflaterService().inflate(R.layout.main_reminder_layout, null, false));
        TextView mTvWord = (TextView) getWeakPreferenceMainLayout().findViewById(R.id.tv_vocabulary);
        TextView mTvPronun = (TextView) getWeakPreferenceMainLayout().findViewById(R.id.tv_pronunciation);
        TextView mTvSentence = (TextView) getWeakPreferenceMainLayout().findViewById(R.id.tv_sentence);
        ToggleButton mToggleButton = (ToggleButton) getWeakPreferenceMainLayout().findViewById(R.id.toggle);
        ImageView mButtonOpenApp = (ImageView) getWeakPreferenceMainLayout().findViewById(R.id.btn_setting);

        //Start app
        mButtonOpenApp.setOnClickListener(new OnClickStartApp());
        //Show/hide meaning
        mToggleButton.setOnCheckedChangeListener(new ShowMeaningCheckChanged(mTvSentence));


        mTvWord.setTypeface(mBoldtypeface);
        mTvSentence.setTypeface(mRegulartypeface);
//        mReminderLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mReminderLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                //initGestureDetection();
//            }
//        });

        WindowManager.LayoutParams layoutParam = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        //TOAST: ability to view notification
        //lp.type = WindowManager.LayoutParams.TYPE_TOAST;
        //SYSTEM_ERROR: doesn't ability to view notification
        layoutParam.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        layoutParam.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        //Setup in/out animation for reminder
        layoutParam.windowAnimations = android.R.style.Animation_Toast;

        //Set color
        ProviderWrapper providerWrapper = new ProviderWrapper(mContext.getApplicationContext());
        getWeakPreferenceMainLayout().setBackgroundColor(providerWrapper.getColorTheme());
        //Random word
        Word w = providerWrapper.getRandomWord();
        if (w != null) {
            mTvWord.setText(w.getWordName());
            mTvPronun.setText(w.getPronunciation());
            mTvSentence.setText(w.getDefault_Meaning());
        } else {
            Log.d(TAG, "Dictionary is empty");
        }
        getWindowManager().addView(getWeakPreferenceMainLayout(), layoutParam);
    }

    private void setupReminderEvent() {
        if (getWeakPreferenceMainLayout() != null) {
            OpenPanelListener openPanelListener = new OpenPanelListener(mReminderLayout.get());
            OALGestureListener doubletabDetector = new OALGestureListener(mReminderLayout.get().getContext());
            doubletabDetector.setOnOpenSettingPanelListener(openPanelListener);
            GestureDetector g = new GestureDetector(mReminderLayout.get().getContext(), doubletabDetector);
            getWeakPreferenceMainLayout().setOnTouchListener(new OnTouchDismissListener(g));
        } else {
            Log.d(TAG, ">>>setupReminderEvent Cannot set OnTouchDismissListener event");
        }


    }

    private View getWeakPreferenceMainLayout() {
        if (mReminderLayout == null || mReminderLayout.get() == null) {
            return null;
        } else {
            return mReminderLayout.get();
        }
    }
}

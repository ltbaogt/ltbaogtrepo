package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.OnClickIpaSpeaker;
import com.ltbaogt.vocareminder.vocareminder.utils.Utils;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import java.util.ArrayList;

/**
 * Created by My PC on 09/08/2016.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyViewHolder>
        implements Filterable {

    private static final String TAG = Define.TAG + "DictionaryAdapter";
    private ArrayList<Word> mArrayList;
    private ArrayList<Word> mNoFilterList;
    private Context mContext;
    private Typeface mTypeFaceBold;
    private Typeface mTypeFaceRegular;

    public DictionaryAdapter(Context ctx, ArrayList<Word> list) {
        mArrayList = list;
        mNoFilterList = list;
        mContext = ctx;
        AssetManager assetManager = mContext.getApplicationContext().getAssets();
        mTypeFaceBold = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_BOLD);
        mTypeFaceRegular = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_REGILAR);
    }

    public void filterRawWords() {
        VRLog.d(TAG, "filterRawWords START");
        try {
            for (int i = mArrayList.size() - 1; i >= 0; i--) {
                Word w = mArrayList.get(i);
                if (!w.getDefault_Meaning().isEmpty()) {
                    mArrayList.remove(w);
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            VRLog.e(TAG, e);
        }
        VRLog.d(TAG, "filterRawWords END");
    }
    public void noFilter() {
        VRLog.d(TAG, "noFilter START");
        try {
            OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(mContext);
            mArrayList = db.getAllWordsOrderByNameInList();
            notifyDataSetChanged();
        } catch (Exception e) {
            VRLog.e(TAG, e);
        }
        VRLog.d(TAG, "noFilter END");
    }

    public void insertWord(Word w) {
        VRLog.d(TAG, ">>>insertWord START");
        mArrayList.add(w);
        int position = mArrayList.indexOf(w);
        notifyItemInserted(position);
        VRLog.d(TAG, ">>>insertWord END");
    }
    public void updateWord(Word w) {
        int index = mArrayList.indexOf(w);
        if (index >= 0) {
            mArrayList.set(index, w);
            notifyItemChanged(index);
        }
    }

    public Word removeWord(int position) {
        Word w = mArrayList.get(position);
        mArrayList.remove(w);
        if (w != null) {
            notifyItemRemoved(position);
        }
        return w;
    }

    public void insertWordAtIndex(Word w, int index) {
        VRLog.d(TAG, ">>>insertWordAtIndex START, index= " + index);
        mArrayList.add(index, w);
        notifyItemInserted(index);
        VRLog.d(TAG, ">>>insertWordAtIndex END");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VRLog.d(TAG, ">>>onCreateViewHolder START");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_word, parent, false);
        VRLog.d(TAG, ">>>onCreateViewHolder END");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        VRLog.d(TAG, ">>>onBindViewHolder START");
        final Word w = mArrayList.get(holder.getAdapterPosition());
        VRLog.d(TAG, ">>>onBindViewHolder " + w.toString());

        holder.mWordName.setTypeface(mTypeFaceBold);
        holder.mWordName.setText(w.getWordName());

        holder.mMeaning.setTypeface(mTypeFaceRegular);
        holder.mMeaning.setText(w.getDefault_Meaning());
//        int ipaSpeakVisible = isMp3Existed(holder.mWordName.getContext(), w) ? View.VISIBLE : View.GONE;
//        holder.mIpaSpeak.setVisibility(ipaSpeakVisible);
        holder.mSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity) {

                    ((MainActivity) mContext).showDialogEditWord(w);
                }
            }
        });
        holder.mPronunciation.setText(w.getPronunciation());
        OnClickIpaSpeaker onClickIpaSpeaker= new OnClickIpaSpeaker(w);
        holder.setIpaSpeakOnClickListener(onClickIpaSpeaker);
        VRLog.d(TAG, ">>>onBindViewHolder END");
    }

    private boolean isMp3Existed(Context context, Word w) {
        return (!Utils.isStringNullOrEmpty(w.getMp3Url())
                || Utils.checkMp3FileExisted(context, Utils.mp3ForWordName(w.getWordName())));
    }

    @Override
    public int getItemCount() {
        VRLog.d(TAG, ">>>getItemCount mArrayList.size= " + mArrayList.size());
        return mArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Word> filteredList;
                if (constraint.length() == 0) {
                    filteredList = mNoFilterList;
                } else {
                    filteredList = getFilteredResult(constraint.toString().toLowerCase());
                }
                FilterResults result = new FilterResults();
                result.values = filteredList;
                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mArrayList = (ArrayList<Word>) filterResults.values;
                DictionaryAdapter.this.notifyDataSetChanged();
            }
        };
    }

    private ArrayList<Word> getFilteredResult(String s) {
        ArrayList<Word> retList = new ArrayList<>();
        for (Word w : mArrayList) {
            if (w.getWordName().toLowerCase().contains(s)) {
                retList.add(w);
            }
        }
        return retList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView mWordName;
        protected CardView mSingleItem;
        protected TextView mMeaning;
        protected TextView mPronunciation;
        protected ImageView mIpaSpeak;

        public MyViewHolder(View itemView) {
            super(itemView);
            mWordName = (TextView) itemView.findViewById(R.id.tv_title_wordname);
            mMeaning = (TextView) itemView.findViewById(R.id.tv_meaning);
            mSingleItem = (CardView) itemView.findViewById(R.id.cardview);
            mPronunciation = (TextView) itemView.findViewById(R.id.tv_pronunciation);
            mIpaSpeak = (ImageView) itemView.findViewById(R.id.img_ipa_speak);
        }

        public void setIpaSpeakOnClickListener(OnClickIpaSpeaker l) {
            mIpaSpeak.setOnClickListener(l);
        }

    }
}

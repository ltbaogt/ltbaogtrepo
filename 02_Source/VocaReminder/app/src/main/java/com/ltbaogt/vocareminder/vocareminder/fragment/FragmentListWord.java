package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.adapter.DictionaryAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.provider.ProviderWrapper;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import java.util.ArrayList;

/**
 * Created by My PC on 08/08/2016.
 */

public class FragmentListWord extends BaseFragment {

    public static final String TAG = Define.TAG + "FragmentListWord";
    private static final int ITEM_WORD_MARGIN = 20;
    private RecyclerView mRecycler;
//    private RecyclerView mRecyclerTag;
    private View mMainView;
    private boolean mIsArchivedScreen;
//    private LinearLayout mTagPanel;

//    private OALSimpleOnGestureListener mTagPanelSimpleOnGestureListener;

//    private static class OnDoubleTapTagPanel implements OALSimpleOnGestureListener.OnDoubleTap {
//
//        FragmentListWord mFragmentListWord;
//
//        public OnDoubleTapTagPanel(FragmentListWord listWord) {
//            mFragmentListWord = listWord;
//        }
//
//        @Override
//        public void onDoubleTap() {
//            if (mFragmentListWord != null) {
//                mFragmentListWord.toggleTagPanel();
//            }
//        }
//    }

//    private GestureDetector mTagPanelGestureDetector;

//    private OnDoubleTapTagPanel mOnDoubleTapTagPanel;

    public void isArchivedSrceen(boolean isArchived) {
        mIsArchivedScreen = isArchived;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        VRLog.d(TAG, "onCreateView START");
        mMainView = inflater.inflate(R.layout.fragment_list_word, container, false);
        ArrayList<Word> arrayList = getWordList();
        //Show Recyclerview
        mRecycler = (RecyclerView) mMainView.findViewById(R.id.recycler);
        mRecycler.setVisibility(View.VISIBLE);
        DictionaryAdapter da = new DictionaryAdapter(getActivity(), arrayList);
        mRecycler.setAdapter(da);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(lm);
        setRecyclerViewItemTouchListener();
        updateLayoutNoWord();

        //Remve Tag panel start
//        ArrayList<Tag> tagList = bl.getTagList();
//        mRecyclerTag = (RecyclerView) mMainView.findViewById(R.id.recycler_tag);
//        TagAdapter tagAdapter = new TagAdapter(tagList);
//        RecyclerView.LayoutManager lmTag = new LinearLayoutManager(getContext());
//        mRecyclerTag.setLayoutManager(lmTag);
//        mRecyclerTag.setAdapter(tagAdapter);
//
//
//        mTagPanel = (LinearLayout) mMainView.findViewById(R.id.panel_tag);
//        mOnDoubleTapTagPanel = new OnDoubleTapTagPanel(this);
//        mTagPanelSimpleOnGestureListener = new OALSimpleOnGestureListener();
//        mTagPanelSimpleOnGestureListener.setOnDoubleTapListener(mOnDoubleTapTagPanel);
//        mTagPanelGestureDetector = new GestureDetector(getActivity(), mTagPanelSimpleOnGestureListener);
//        mTagPanel.setClickable(true);
//        mTagPanel.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return mTagPanelGestureDetector.onTouchEvent(motionEvent);
//            }
//        });
//Remve Tag panel end
        VRLog.d(TAG, "onCreateView END");
        return mMainView;
    }

    private ArrayList<Word> getWordList() {
        ProviderWrapper pw = new ProviderWrapper(getContext());
        if (mIsArchivedScreen) {
            return pw.getArchivedListWord();
        } else {
            return pw.getListWord();
        }
    }
    //Get Adapter of RecyclerView. Current is DictionaryAdapter instance
    public DictionaryAdapter getWordAdapter() {
        return (DictionaryAdapter) mRecycler.getAdapter();
    }

    public void addNewWord(Word w) {
        getWordAdapter().insertWord(w);
        updateLayoutNoWord();
    }

    public void filterRawWords() {
        getWordAdapter().filterRawWords();
    }

    public void noFilter() {
        getWordAdapter().noFilter();
    }

    public void updateWord(Word w) {
        VRLog.d(TAG, ">>>updateWord START");
        getWordAdapter().updateWord(w);
        VRLog.d(TAG, ">>>updateWord END");
    }

    public void updateLayoutNoWord() {
        VRLog.d(TAG, ">>>updateLayoutNoWord START");
        if (mIsArchivedScreen) return;
        View btnAdd = mMainView.findViewById(R.id.noword_layout);
        if (getWordAdapter() != null && getWordAdapter().getItemCount() <= 0) {

            btnAdd.setVisibility(View.VISIBLE);
            if (mRecycler != null) {
                mRecycler.setVisibility(View.INVISIBLE);
            }
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity = FragmentListWord.this.getParentActivity();
                    if (activity != null) {
                        Word w = new Word();
                        activity.showDialogNewWord(w);
                    }
                }
            };
            btnAdd.setOnClickListener(listener);
        } else {
            if (mRecycler != null) {
                mRecycler.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.INVISIBLE);
            }
        }
        VRLog.d(TAG, ">>>updateLayoutNoWord END");
    }

    private void setRecyclerViewItemTouchListener() {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, getSwipeDirections()) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        handleSwipeRight(viewHolder);
                        break;
                    default:
                        handleSwipeLeft(viewHolder);
                        break;
                }

            }

            private void handleSwipeLeft(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                Word w = getWordAdapter().removeWord(position);
                OALBLL bl = new OALBLL(FragmentListWord.this.getContext());

                if (mIsArchivedScreen) {
                    bl.deleteForever(w.getWordId());
                    w.deleteMp3File(getContext());
                    Toast.makeText(getContext(),String.format(getString(R.string.message_word_was_deleted), w.getWordName()),Toast.LENGTH_SHORT).show();
                } else {
                    bl.deleteWordById(w.getWordId());
                    showSnackbarForDeletionWord(bl, w, position);
                }
            }

            private void handleSwipeRight(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                Word w = getWordAdapter().removeWord(position);
                OALBLL bl = new OALBLL(FragmentListWord.this.getContext());

                if (mIsArchivedScreen) {
                    bl.undoWordById(w.getWordId());
                    Toast.makeText(getContext(),String.format(getString(R.string.snackbar_word_restored), w.getWordName()),Toast.LENGTH_SHORT).show();
                }
            }
            //Show snackbar for deletion, user can undo after deleting
            private void showSnackbarForDeletionWord(final OALBLL bl, final Word w, final int atPosition) {
                if (getActivity() != null) {
                    final CoordinatorLayout coordinatorLayout = ((MainActivity) getActivity()).getCoordinatorLayout();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            String.format(getResources().getString(R.string.message_word_was_archived),
                                    w.getWordName()),
                            Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.snackbar_button_undo),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            undoDeleteWord(bl, coordinatorLayout, w, atPosition);
                                        }
                                    })
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    updateLayoutNoWord();
                                }
                            });
                    snackbar.show();
                }
            }

            //Undo deletion
            private void undoDeleteWord(OALBLL bl, CoordinatorLayout layout, Word w, int atPosition) {
                bl.undoWordById(w.getWordId());
                getWordAdapter().insertWordAtIndex(w,atPosition);
                try {
                    updateLayoutNoWord();
                    Snackbar snackbar1 = Snackbar.make(layout,
                            String.format(getResources().getString(R.string.snackbar_word_restored), w.getWordName()),
                            Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                } catch (IllegalStateException ex) {
                    VRLog.e(TAG, ">>>undoDeleteWord Word was undo but layout cannot update. Reason: Screen is transited");
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX > 0) {
                        p.setColor(getResources().getColor(R.color.green));
                        RectF background = new RectF((float) itemView.getLeft() + ITEM_WORD_MARGIN, (float) itemView.getTop(),(float) itemView.getRight() + dX - 20, (float) itemView.getBottom() - ITEM_WORD_MARGIN);
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_unarchieved);
                        float leftEdge = (float) itemView.getLeft() + 3*ITEM_WORD_MARGIN;
                        RectF icon_dest = new RectF(leftEdge,(float) itemView.getTop() + width, leftEdge + 48,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else if (dX < 0) {
                        p.setColor(getResources().getColor(R.color.red));
                        RectF background = new RectF((float) itemView.getRight() + dX - 5*ITEM_WORD_MARGIN, (float) itemView.getTop(),(float) itemView.getRight() - ITEM_WORD_MARGIN, (float) itemView.getBottom() - ITEM_WORD_MARGIN);
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_trash);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecycler);
    }

    private int getSwipeDirections() {
        if (mIsArchivedScreen) {
            return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            return ItemTouchHelper.LEFT;
        }
    }
//    //Show Tag panel
//    public void toggleTagPanel() {
//        if (mTagPanel != null) {
//            if (mTagPanel.getVisibility() == View.VISIBLE) {
//                mTagPanel.setVisibility(View.GONE);
//            } else {
//                mTagPanel.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//    //Show Tag panel
//    public void hideTagPanel() {
//        if (mTagPanel != null) {
//            mTagPanel.setVisibility(View.GONE);
//        }
//    }
}

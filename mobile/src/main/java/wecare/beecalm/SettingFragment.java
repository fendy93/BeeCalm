package wecare.beecalm;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class SettingFragment extends ListFragment {

    private ArrayList<Pair<Long, String>> mItemArray;
    private DragListView mDragListView;
    private MySwipeRefreshLayout mRefreshLayout;
    private DataContainer dc;
    private ArrayList<String> mantrasString = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dc = DataContainer.getInstance();
        mantrasString = dc.getMantrasString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_view, container, false);
        mRefreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mDragListView = (DragListView) view.findViewById(R.id.drag_list_view);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);

        mItemArray = new ArrayList<>();
        for (int i = 0; i < mantrasString.size(); i++) {
            mItemArray.add(new Pair<>(Long.valueOf(i), mantrasString.get(i)));
        }


        mRefreshLayout.setScrollingView(mDragListView.getRecyclerView());
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.app_color));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        setupListRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mantrasString = dc.getActivityString();
        mItemArray.clear();
        for (int i = 0; i < mantrasString.size(); i++) {
            if (mantrasString.get(i).length() < 27 ) {
                mItemArray.add(new Pair<>(Long.valueOf(i), mantrasString.get(i)));
            } else {
                mItemArray.add(new Pair<>(Long.valueOf(i), mantrasString.get(i).substring(0, 24)));
            }
        }
        setupListRecyclerView();
    }

    @Override
    public void onPause(){
        super.onPause();
        dc.writeConfigure(getActivity());
        System.out.println("start transmitting to the watch");
        getActivity().startService(new Intent(getActivity(), PhoneToWatchService.class));
        System.out.println("finished transmitting to the watch");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
    }

    private void setupListRecyclerView() {
        mDragListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.setting_item, R.id.image, false);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.setting_item));
    }

    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            CharSequence drag = ((TextView) dragView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            ImageView img = (ImageView) dragView.findViewById(R.id.imageView);
            if (text.equals("Mantras")) {
                img.setImageResource(R.mipmap.blackarrow);
                img.setVisibility(View.VISIBLE);
            } else if (text.equals("Yoga")) {
                img.setImageResource(R.mipmap.blackarrow);
                img.setVisibility(View.VISIBLE);
            } else {
                img.setVisibility(View.GONE);
            }
            dragView.setBackgroundColor(dragView.getResources().getColor(R.color.list_item_background));
        }
    }
}

class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private View mScrollingView;

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        return mScrollingView != null;
    }

    public void setScrollingView(View scrollingView) {
        mScrollingView = scrollingView;
    }
}
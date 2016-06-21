package wecare.beecalm;

/**
 * Created by fendyzhou on 4/17/16.
 */
import android.os.Looper;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private ArrayList<ImageView> img_id = new ArrayList<>();
    private ArrayList<TextView> text_view = new ArrayList<>();
    private DataContainer dc;

    public ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(list);
        dc = DataContainer.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        final ImageView img = (ImageView) view.findViewById(R.id.imageView);
        final TextView text = (TextView) view.findViewById(R.id.text);
        img_id.add(img);
        text_view.add(text);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        dc.updateActivityListOrder(position, text);
        Log.d("Activity order list", dc.getActivityOrderByIndex(position));
        holder.mText.setText(text);
        holder.itemView.setTag(text);
        if (text_view.get(position).getText().equals("Mantras")) {
            img_id.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MantraSettingsActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
            img_id.get(position).setVisibility(View.VISIBLE);
        } else if (text_view.get(position).getText().equals("Yoga")) {
            img_id.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), YogaSettingActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
            img_id.get(position).setVisibility(View.VISIBLE);
        } else {
            img_id.get(position).setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public class ViewHolder extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder>.ViewHolder {
        public TextView mText;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            mText = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
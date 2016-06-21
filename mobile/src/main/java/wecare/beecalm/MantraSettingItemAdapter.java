package wecare.beecalm;

/**
 * Created by fendyzhou on 4/17/16.
 */
import android.content.Intent;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class MantraSettingItemAdapter extends DragItemAdapter<Pair<Long, String>, MantraSettingItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private ArrayList<ImageView> img_id = new ArrayList<>();
    private ImageView img_icon;
    private  DataContainer dc;
    private ArrayList<String> mantrasString = null;

    public MantraSettingItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(list);
        dc = DataContainer.getInstance();
        mantrasString = dc.getMantrasString();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        final ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img_id.add(img);
        img_icon = (ImageView) view.findViewById((R.id.image));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        dc.updateMantraList(position, text);
        holder.mText.setText(text);
        holder.itemView.setTag(text);

        img_id.get(position).setImageResource(R.mipmap.blackarrow);
        img_id.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MantraSettingStep1Activity.class);
                    intent.putExtra("position", Integer.toString(position));
                    v.getContext().startActivity(intent);
                }
        });
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public class ViewHolder extends DragItemAdapter<Pair<Long, String>, MantraSettingItemAdapter.ViewHolder>.ViewHolder {
        public TextView mText;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            mText = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
package wecare.beecalm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stephanielin on 4/30/16.
 */
public class YogaGridAdapter extends BaseAdapter  {

    private Context context;
    private DataContainer dc;
    private static ArrayList<Boolean> chosenPoses;
    private ArrayList<String> yogaList;
    private HashMap<String, Integer> yogaImages;
    private Drawable d;

    public YogaGridAdapter(Context c) {
        context = c;
        dc = DataContainer.getInstance();
        chosenPoses = dc.getYogaConfig();
        yogaList = dc.getYogaList();
        yogaImages = dc.getYogaImages();
        d = c.getResources().getDrawable(R.drawable.green_border);
    }


    @Override
    public int getCount() {
        return yogaList.size();
    }

    @Override
    public Object getItem(int position) {
        return yogaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView==null){
            v = LayoutInflater.from(context).inflate(R.layout.grid_image_view,null);
            v.setLayoutParams(new GridView.LayoutParams(300, 300));

            ImageView imageView = (ImageView)v.findViewById(R.id.yoga_image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setImageResource(yogaImages.get(yogaList.get(position)));
        }
        else
        {
            v = convertView;
        }
        if (chosenPoses.get(position)) {
            v.setBackgroundResource(dc.getFixedYogaColor(position));
        } else {
            v.setBackgroundResource(0);
        }

        return v;
    }
}

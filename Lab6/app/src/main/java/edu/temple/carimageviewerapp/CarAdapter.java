package edu.temple.carimageviewerapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;

public class CarAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> items;
    int[] carImage;

    public CarAdapter(Context context, ArrayList items, int[] carImage){
        this.context = context;
        this.items = items;
        this.carImage = carImage;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        LinearLayout linearLayout;

        if(convertView == null){
            linearLayout = new LinearLayout(context);
            textView = new TextView(context);

            //Layaout Code
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(textView);

            //TextView Code
            textView.setGravity(CENTER);
            textView.setTextSize(20);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(null, Typeface.BOLD);
            textView.getLayoutParams().height = 350;
            textView.getLayoutParams().width = 350;
            textView.setPadding(8, 16, 16, 16);

        } else {
            linearLayout = (LinearLayout) convertView;
            textView = (TextView) linearLayout.getChildAt(0);
        }

        textView.setText(items.get(position));
        textView.setBackgroundResource(carImage[position]);

        return linearLayout;
    }
}

package edu.temple.ImageActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        TextView textView = new TextView(context);
        textView.setText(items.get(position));

        textView.setPadding(10,10,10,10);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        ImageView imageView;

        LinearLayout linearLayout;

        if(convertView == null){
            linearLayout = new LinearLayout(context);
            textView = new TextView(context);
            imageView = new ImageView(context);
            textView.setTextSize(20);
            textView.setPadding(10,10,10,10);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            imageView.getLayoutParams().height = 150;
            imageView.getLayoutParams().width = 150;
        } else {
            linearLayout = (LinearLayout) convertView;
            imageView = (ImageView) linearLayout.getChildAt(0);
            textView = (TextView) linearLayout.getChildAt(1);
        }

        imageView.setImageResource(carImage[position]);
        textView.setText(items.get(position));

        return linearLayout;
    }
}

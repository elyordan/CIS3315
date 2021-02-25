package edu.temple.ImageActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        return null;
    }
}

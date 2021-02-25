package edu.temple.ImageActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ImageActivity extends AppCompatActivity {

    ListView listView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] carArray = new String[]{"Buggati Chiron", "Lamborghini Huracan", "La Ferrari",
                "Nissan GTR", "Porsche GT3","Mercedes AMG" };
        int[] carResIds = new int[]{R.drawable.chiron, R.drawable.huracan, R.drawable.laferrari,
                R.drawable.gtr, R.drawable.gt3, R.drawable.amg};

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, carArray);

        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.imageView);

        listView.setAdapter((adapter));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageView.setImageResource(carResIds[position]);

            }
        });




    }
}
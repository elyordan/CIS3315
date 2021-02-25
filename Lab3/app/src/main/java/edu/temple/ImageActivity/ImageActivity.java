package edu.temple.ImageActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    Spinner spinner;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList carNamesArray = new ArrayList<String>();
        carNamesArray.add("Buggati Chiron");
        carNamesArray.add("Lamborghini Huracan");
        carNamesArray.add("La Ferrari");
        carNamesArray.add("Nissan GTR");
        carNamesArray.add("Porsche GT3");
        carNamesArray.add("Mercedes AMG");

//        String[] carNamesArray = new String[]{"Buggati Chiron", "Lamborghini Huracan", "La Ferrari",
//                "Nissan GTR", "Porsche GT3","Mercedes AMG" };

        int[] carResIds = new int[]{R.drawable.chiron, R.drawable.huracan, R.drawable.laferrari,
                R.drawable.gtr, R.drawable.gt3, R.drawable.amg};

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, carArray);
        CarAdapter adapter = new CarAdapter(this, carNamesArray, carResIds);
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.spinner);
        imageView = findViewById(R.id.imageView);

        spinner.setAdapter((adapter));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageView.setImageResource(carResIds[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
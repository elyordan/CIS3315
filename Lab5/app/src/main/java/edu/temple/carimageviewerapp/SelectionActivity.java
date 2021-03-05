package edu.temple.carimageviewerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "edu.temple.carimageviewerapp.EXTRA_NAME";
    public static final String EXTRA_NUMBER = "edu.temple.carimageviewerapp.EXTRA_NUMBER";

    ImageView imageView;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Select Item");

        ArrayList carNamesArray = new ArrayList<String>();
        carNamesArray.add("Chiron");
        carNamesArray.add("Huracan");
        carNamesArray.add("LaFerrari");
        carNamesArray.add("GTR");
        carNamesArray.add("GT3");
        carNamesArray.add("AMG");


        int[] carResIds = new int[]{R.drawable.chiron, R.drawable.huracan, R.drawable.laferrari,
                R.drawable.gtr, R.drawable.gt3, R.drawable.amg};

        CarAdapter adapter = new CarAdapter(this, carNamesArray, carResIds);

        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        Intent launchIntent = new Intent (SelectionActivity.this, DisplayActivity.class);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String carName = carNamesArray.get(position).toString();
                    launchIntent.putExtra(EXTRA_NAME, carName);
                    launchIntent.putExtra(EXTRA_NUMBER, position);
                    startActivity(launchIntent);
            }
        });
    }
}
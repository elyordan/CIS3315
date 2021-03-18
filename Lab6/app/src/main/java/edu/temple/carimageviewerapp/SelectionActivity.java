package edu.temple.carimageviewerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Objects;

public class SelectionActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "edu.temple.carimageviewerapp.EXTRA_NAME";
    public static final String EXTRA_NUMBER = "edu.temple.carimageviewerapp.EXTRA_NUMBER";

    ImageView imageView;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.selection_activity);

        ArrayList<String> carNamesArray;
        carNamesArray = new ArrayList<>();
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



        gridView.setOnItemClickListener((parent, view, position, id) -> {
                String carName = carNamesArray.get(position);
                launchIntent.putExtra(EXTRA_NAME, carName);
                launchIntent.putExtra(EXTRA_NUMBER, position);
                startActivity(launchIntent);
        });
    }
}
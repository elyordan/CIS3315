package edu.temple.carimageviewerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "edu.temple.carimageviewerapp.EXTRA_NAME";
    public static final String EXTRA_NUMBER = "edu.temple.carimageviewerapp.EXTRA_NUMBER";

    Spinner spinner;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ArrayList
        ArrayList carNamesArray = new ArrayList<String>();
        carNamesArray.add("Please Select a Car");
        carNamesArray.add("Buggati Chiron");
        carNamesArray.add("Lamborghini Huracan");
        carNamesArray.add("La Ferrari");
        carNamesArray.add("Nissan GTR");
        carNamesArray.add("Porsche GT3");
        carNamesArray.add("Mercedes AMG");


        int[] carResIds = new int[]{R.drawable.white, R.drawable.chiron, R.drawable.huracan, R.drawable.laferrari,
                R.drawable.gtr, R.drawable.gt3, R.drawable.amg};

        CarAdapter adapter = new CarAdapter(this, carNamesArray, carResIds);

        spinner = findViewById(R.id.spinner);

        spinner.setAdapter(adapter);

        Intent launchIntent = new Intent (SelectionActivity.this, DisplayActivity.class);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){

                } else {

                    String carName = carNamesArray.get(position).toString();
                    launchIntent.putExtra(EXTRA_NAME, carName);
                    launchIntent.putExtra(EXTRA_NUMBER, position);
                    startActivity(launchIntent);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
}
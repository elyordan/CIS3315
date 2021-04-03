package edu.temple.bookshelf;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookSearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search_activity);

        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.startSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = searchEditText.getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("response", response);

                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });
    }
}
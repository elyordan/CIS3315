package edu.temple.bookshelf;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Book> bookArrayList = new ArrayList<>();
    BookList booksCollection;
    boolean twoWindows;
    Book currentBook;
    BookDetailsFragment currentDetails;
    Button searchButton;
    String webURL;
    RequestQueue requestQueue;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twoWindows = (findViewById(R.id.book_details_fragment) != null);

        Resources res = getResources();
        webURL = res.getString(R.string.bookURL);

        requestQueue = Volley.newRequestQueue(this);

        booksCollection = new BookList(this, bookArrayList);

        currentDetails = BookDetailsFragment.newInstance(currentBook);

        if (twoWindows) {
            Fill_The_Fragment(R.id.book_details_fragment, currentDetails, false);
        }

        sharedPref = this.getSharedPreferences("edu.temple.bookshelf", Context.MODE_PRIVATE);
        String search_Book_Term = sharedPref.getString("search_Book_Term", "");

        UpdateListOfBooks(search_Book_Term);
        onClickButtonMethod();
    }

    private void onClickButtonMethod() {

        searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Start_Book_Search();
            }
        });
    }

    public void Book_Selection(int position) {
        currentBook = booksCollection.get(position);

        twoWindows = (findViewById(R.id.book_details_fragment) != null);

        if (!twoWindows) {
            BookDetailsFragment newFragment = BookDetailsFragment.newInstance(currentBook);
            currentDetails = newFragment;
            Fill_The_Fragment(R.id.book_list_fragment, newFragment, true);
        } else {
            currentDetails.displayBook(currentBook);
        }
    }

    private void Fill_The_Fragment(int paneId, Fragment fragment, boolean placeOnBackStack) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .replace(paneId, fragment);
        if (placeOnBackStack)
            ft.addToBackStack(null);
        ft.commit();

        fm.executePendingTransactions();
    }

    public void Start_Book_Search() {
        Intent intent = new Intent(this, BookSearchActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        twoWindows = (findViewById(R.id.book_details_fragment) != null);

        onClickButtonMethod();

        currentDetails = BookDetailsFragment.newInstance(currentBook);
        Fragment list = BookListFragment.newInstance(booksCollection, this);

        if (twoWindows) {
            Fill_The_Fragment(R.id.book_list_fragment, list, false);
            Fill_The_Fragment(R.id.book_details_fragment, currentDetails, false);
        } else {
            if (currentBook != null)
                Fill_The_Fragment(R.id.book_list_fragment, currentDetails, true);
            else
                Fill_The_Fragment(R.id.book_list_fragment, list, false);
        }
    }


    private void UpdateListOfBooks(String searchTerm) {
        String urlString = webURL + searchTerm;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<Book> newList = new ArrayList<Book>();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject currObj = (JSONObject) response.getJSONObject(i);
                        int id = currObj.getInt("id");
                        String title = currObj.getString("title");
                        String author = currObj.getString("author");
                        String coverURL = currObj.getString("cover_url");
                        newList.add(new Book(title, author, coverURL, id));
                    }

                    booksCollection = new BookList(MainActivity.this, newList);
                    Fragment list = BookListFragment.newInstance(booksCollection, MainActivity.this);
                    Fill_The_Fragment(R.id.book_list_fragment, list, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String search_Book_Term = data.getStringExtra("response");

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("search_Book_Term", search_Book_Term);
                editor.apply();

                UpdateListOfBooks(search_Book_Term);
            }
        }
    }
}
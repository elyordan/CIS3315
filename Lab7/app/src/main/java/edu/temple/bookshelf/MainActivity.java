package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BookList Booklibrary;
    boolean twoLayoutWindows;
    Book Book;
    BookDetailsFragment Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twoLayoutWindows = findViewById(R.id.fragment_book_details) != null;

        Resources stringRes = getResources();
        String[] bookTitles = stringRes.getStringArray(R.array.book_titles);
        String[] bookAuthors = stringRes.getStringArray(R.array.book_authors);

        ArrayList<Book> newBooks = new ArrayList<Book>();
        for(int i = 0; i < 10; i++) {
            newBooks.add(new Book(bookTitles[i], bookAuthors[i]));
        }

        Booklibrary = new BookList(newBooks, this);


    }
}
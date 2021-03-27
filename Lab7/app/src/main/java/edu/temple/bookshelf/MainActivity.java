package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
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
        for (int i = 0; i < 10; i++) {
            newBooks.add(new Book(bookTitles[i], bookAuthors[i]));
        }

        Booklibrary = new BookList(newBooks, this);


        fillFragment(R.id.fragment_book_list, BookListFragment.newInstance(Booklibrary, this), false);

        Details = BookDetailsFragment.newInstance(Book);
        if (twoLayoutWindows)
            fillFragment(R.id.fragment_book_details, Details, false);

    }

    private void fillFragment(int fbooklistpos, Fragment fragment, boolean b) {

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragtrans = fragMan.beginTransaction().replace(fbooklistpos, fragment);
        if (b) {
            fragtrans.addToBackStack(null);
        }
        fragtrans.commit();
        fragMan.executePendingTransactions();
    }

    public void BookSelection(int position) {
        Book = Booklibrary.get(position);

        twoLayoutWindows = findViewById(R.id.fragment_book_details) != null;

        if (!twoLayoutWindows) {
            Fragment newFragment = BookDetailsFragment.newInstance(Book);
            Details = (BookDetailsFragment) newFragment;
            fillFragment(R.id.fragment_book_details, newFragment, true);
        } else {
            Details.showBook(Book);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration changeConfig) {
        super.onConfigurationChanged(changeConfig);
        setContentView(R.layout.activity_main);
        twoLayoutWindows = (findViewById(R.id.fragment_book_details) != null);

        Details = BookDetailsFragment.newInstance(Book);
        Fragment newlist = BookListFragment.newInstance(Booklibrary, this);

        if (twoLayoutWindows) {
            fillFragment(R.id.fragment_book_list, newlist, false);
            fillFragment(R.id.fragment_book_details, Details, false);
        } else {
            if (Book != null)
                fillFragment(R.id.fragment_book_list, Details, true);
            else
                fillFragment(R.id.fragment_book_list, newlist, false);
        }

    }
}


package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {

    Book book;
    TextView title;
    TextView author;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance()
    {
        return new BookDetailsFragment();
    }

    public static BookDetailsFragment newInstance(Book book)
    {
        BookDetailsFragment my_new_fragment_details = new BookDetailsFragment();
        my_new_fragment_details.book = book;
        my_new_fragment_details.showBook(my_new_fragment_details.book);
        return my_new_fragment_details;
    }

    public void updateBook(Book book)
    {
        this.book = book;
    }

    public void showBook(Book book) {
        this.book = book;
        if ((title != null) && (author != null) && (book != null)) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_book_details, null);
        title = fragmentView.findViewById(R.id.booktitledetails);
        author = fragmentView.findViewById(R.id.bookauthordetails);
        if(this.book != null)
            this.showBook(this.book);

        return fragmentView;
    }
}
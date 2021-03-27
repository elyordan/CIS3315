package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {

    private BookList bookList;
    private ListView listview;
    private MainActivity parentActivity;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }

    public static BookListFragment newInstance(BookList booklist, MainActivity parent) {
        BookListFragment my_new_fragment = new BookListFragment();
        my_new_fragment.bookList = booklist;
        my_new_fragment.parentActivity = parent;
        return my_new_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_book_list, null);
        listview = fragView.findViewById(R.id.fragment_book_list);
        listview.setAdapter(this.bookList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                parentActivity.BookSelection(position);
            }

        });
        return fragView;
    }
}

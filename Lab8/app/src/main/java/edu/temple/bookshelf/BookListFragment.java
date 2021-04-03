package edu.temple.bookshelf;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class BookListFragment extends Fragment {
    private BookList book_list;
    private ListView listview;
    private MainActivity parentActivity;

    public BookListFragment() {
        //Required empty default constructor
    }

    public static BookListFragment newInstance()
    {
        return new BookListFragment();
    }

    public static BookListFragment newInstance(BookList booklist, MainActivity parent) {
        BookListFragment listFragment = new BookListFragment();
        listFragment.book_list = booklist;
        listFragment.parentActivity = parent;
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.book_list_fragment, null);
        listview = v.findViewById(R.id.book_list_fragment);
        listview.setAdapter(this.book_list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               parentActivity.Book_Selection(position);
            }
        });
        return v;
    }
}

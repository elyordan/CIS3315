package edu.temple.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookList extends BaseAdapter {

    ArrayList<Book> book_array;
    Context context;
    LayoutInflater inflater;

    public BookList(ArrayList<Book> book_array, Context context) {
        this.book_array = book_array;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(Book book) {
        book_array.add(book);
    }

    public void remove(Book book) {
        book_array.remove(book);
    }

    public Book get(int place) {
        return book_array.get(place);
    }

    public int size() {
        return book_array.size();
    }

    public ArrayList<Book> getBookList() {
        return book_array;
    }

    @Override
    public int getCount() {
        return book_array.size();
    }

    @Override
    public Object getItem(int position) {
        return book_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        convertView = inflater.inflate(R.layout.fragment_book_list, parent, false);

        TextView title = convertView.findViewById(R.id.booktitle);
        title.setText(book_array.get(position).getTitle());

        TextView author = convertView.findViewById(R.id.bookauthor);
        author.setText(book_array.get(position).getAuthor());

        return convertView;
    }
}

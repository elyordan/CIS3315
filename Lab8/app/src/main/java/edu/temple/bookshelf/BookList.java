package edu.temple.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookList extends BaseAdapter {
    private ArrayList<Book> bookArrayList;
    private Context context;
    private LayoutInflater inflater;

    public BookList(Context context, ArrayList<Book> bookArray) {
        this.context = context;
        this.bookArrayList = bookArray;
        inflater = LayoutInflater.from(context);
    }

    public void add(Book book)
    {
        bookArrayList.add(book);
    }

    public void remove(Book book)
    {
        bookArrayList.remove(book);
    }

    public Book get(int index)
    {
        return bookArrayList.get(index);
    }

    public int size()
    {
        return bookArrayList.size();
    }

    public ArrayList<Book> getBookArrayList()
    {
        return bookArrayList;
    }

    @Override
    public int getCount()
    {
        return bookArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return bookArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.booklist_fragment, parent, false);

        TextView title = convertView.findViewById(R.id.title);
        title.setText(bookArrayList.get(position).getTitle());

        TextView author = convertView.findViewById(R.id.author);
        author.setText(bookArrayList.get(position).getAuthor());

        return convertView;
    }
}

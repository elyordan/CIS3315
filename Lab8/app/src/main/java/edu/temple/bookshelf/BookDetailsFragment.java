package edu.temple.bookshelf;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {
    private Book book;
    TextView title;
    TextView author;
    ImageView coverView;

    public BookDetailsFragment() {
        //Required empty default constructor
    }

    public static BookDetailsFragment newInstance()
    {
        return new BookDetailsFragment();
    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment detailsFragment = new BookDetailsFragment();
        detailsFragment.book = book;
        detailsFragment.displayBook(detailsFragment.book);
        return detailsFragment;
    }

    public void updateBook(Book book)
    {
        this.book = book;
    }

    public void displayBook(Book book) {
        this.book = book;
        if (title != null && author != null && coverView != null && book != null) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());

            try {
                Picasso.get().load(Uri.parse(book.getCoverURL())).placeholder(R.drawable.placeholder).into(coverView);
            } catch(Exception e){ e.printStackTrace(); }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_fragment, null);
        title = view.findViewById(R.id.title_details);
        author = view.findViewById(R.id.author_details);
        coverView = (ImageView)view.findViewById(R.id.book_cover);

        if(coverView != null) {
            coverView.setImageResource(R.drawable.placeholder);
        }

        if(this.book != null) {
            this.displayBook(this.book);
        }
        return view;
    }
}

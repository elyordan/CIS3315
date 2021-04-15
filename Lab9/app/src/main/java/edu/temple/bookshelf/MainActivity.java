package edu.temple.bookshelf;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity {

    ArrayList<Book> bookArrayList = new ArrayList<>();
    BookList booksCollection;
    boolean twoWindows;
    Book currentBook;
    Book playBook;
    BookDetailsFragment currentDetails;
    Button searchButton;
    String webURL;
    RequestQueue requestQueue;
    SharedPreferences sharedPref;
    private ControlFragment controls;
    private AudiobookService.MediaControlBinder abService;
    private SeekBar seekbar;
    private int progress;
    private boolean playing;
    private TimerTask timertask;
    private Timer timer;
    public ServiceConnection myConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twoWindows = (findViewById(R.id.book_details_fragment) != null);

        Resources res = getResources();
        webURL = res.getString(R.string.bookURL);

        myConnection = new ServiceConnection()
        {
            public void onServiceConnected(ComponentName className, IBinder binder)
            {
                abService = (AudiobookService.MediaControlBinder)binder;
            }

            public void onServiceDisconnected(ComponentName className) {
                abService = null;
            }
        };

        Intent intent = null;
        intent = new Intent(this, AudiobookService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        requestQueue = Volley.newRequestQueue(this);

        booksCollection = new BookList(this, bookArrayList);

        currentDetails = BookDetailsFragment.newInstance(currentBook);
        controls = ControlFragment.newInstance(this);
        Fill_The_Fragment(R.id.fragment_control, controls, false);
        Fill_The_Fragment(R.id.book_list_fragment, BookListFragment.newInstance(booksCollection, this), false);


        if (twoWindows) {
            Fill_The_Fragment(R.id.book_details_fragment, currentDetails, false);
        }

        sharedPref = this.getSharedPreferences("edu.temple.bookshelf", Context.MODE_PRIVATE);
        String search_Book_Term = sharedPref.getString("search_Book_Term", "");

        UpdateListOfBooks(search_Book_Term);
        onClickButtonMethod();

        //Define our Timer Task and Timer
        createTimerTask();
        timer = new Timer("Timer");

        //Shared prefs stored the necessary data to resume our audiobook on restart
        String bookTitle = sharedPref.getString("bookTitle", null);

        //If bookTitle is null, we'll assume we weren't playing anything
        //Otherwise, we'll create a new book object to represent what we were playing
        if(bookTitle != null)
        {
            int bookID = sharedPref.getInt("bookID", 0);
            progress = sharedPref.getInt("progress", 0);
            int bookDuration = sharedPref.getInt("bookDuration", 0);
            String bookAuthor = sharedPref.getString("bookAuthor", "");
            String bookURL = sharedPref.getString("bookURL", "");
            currentBook = new Book( bookTitle, bookAuthor, bookURL, bookID, bookDuration);
            playBook = currentBook;
            controls.setPlayBook(playBook); //Let our Control Fragment Know

            //Set up a delay to start playing so we can dodge that null reference
            TimerTask task = new TimerTask()
            {
                public void run()
                {
                    playing = true;
                    setupSeekBar();
                    abService.play(playBook.getId(), progress);
                }
            };

            long initialDelay = 5000;
            timer.schedule(task, initialDelay);
        }
    }

    //----------------------------------------------------------------------------------------------
    // Helper Methods for Fragment and Activity Management
    //----------------------------------------------------------------------------------------------

    //In Java we can't reuse a timer task for some unfathomable reason, so we have to redefine
    //it every time its called
    private void createTimerTask()
    {
        //ReDefine our TimerTask
        timertask = new TimerTask()
        {
            public void run()
            {
                seekTimeUpdate();
            }
        };
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

        //Reinitialize the Control Fragment
        controls = ControlFragment.newInstance(this);
        Fill_The_Fragment(R.id.fragment_control, controls, false);
        controls.setPlayBook(playBook);
        setupSeekBar();

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
                        int duration = currObj.getInt("duration");
                        String title = currObj.getString("title");
                        String author = currObj.getString("author");
                        String coverURL = currObj.getString("cover_url");
                        newList.add(new Book(title, author, coverURL, id, duration));
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

    //----------------------------------------------------------------------------------------------
    // Helper Methods for AudioBook Playing / Pausing / Stopping
    //----------------------------------------------------------------------------------------------

    //Pauses pauses the current playback, or plays it if already paused.
    public void pause()
    {
        abService.pause();

        if(playBook != null)
            playing = !playing;

        if(!playing)
        {
            timer.cancel();
            timer.purge();
        }
    }

    //playThisBook() is called BookDetailsFragment. It changes which book is currently loaded
    //and playing by the audiobookservice
    public void playThisBook()
    {
        //Button is visible before book is selected, deal with null reference
        if(currentBook == null)
            return;

        if(abService == null)
            return;

        abService.stop(); //Stop a currently playing audiobook
        progress = 0; //Reset our Progress Tracking
        playBook = currentBook; //Track What Book is Playing
        controls.setPlayBook(playBook); //Let our Control Fragment Know
        setupSeekBar(); //Reset our Seekbar
        playing = true; //Track that we're playing a book

        //Write the ID of our Current Book
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bookID", playBook.getId());
        editor.putString("bookTitle", playBook.getTitle());
        editor.putString("bookAuthor", playBook.getAuthor());
        editor.putString("bookURL", playBook.getCoverURL());
        editor.putInt("bookDuration", playBook.getDuration());
        editor.putInt("progress", 0);
        editor.apply();

        //Schedule a timer for our seekbar
        createTimerTask();
        long delay = 2000;
        timer.schedule(timertask, delay);

        abService.play(playBook.getId()); //Actually Call the Service
    }

    //seekTimeUpdate() handles passive updates to the seekbar progress when playing
    public void seekTimeUpdate()
    {
        //Update Our Local Progress
        progress += 2;
        seekbar.setProgress(progress);

        //Save our Local Progress
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress", progress);
        editor.apply();

        //Schedule another timer
        createTimerTask();
        long delay = 2000;
        timer.schedule(timertask, delay);
    }

    //SetupSeekBar sets up the listener for the seek bar, its called in its own method due to the
    //frequency that the ControlFragment is reinitialized
    public void setupSeekBar()
    {
        seekbar = findViewById(R.id.seekBar);
        seekbar.setMax(playBook.getDuration());
        seekbar.setProgress(progress);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int newProgress, boolean fromUser)
            {
                if (fromUser) //We check this because the seekbar will update periodically
                {
                    abService.seekTo(newProgress);
                    progress = newProgress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //onProgressChanged will handle this. This can do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //onProgressChanged will handle this. This can do nothing.
            }
        });
    }

    //Stop() stops the currently playing book and sets playBook to null
    public void stop()
    {
        abService.stop();
        playing = false;

        playBook = null;
        controls.setPlayBook(null);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bookTitle", null);
        editor.apply();

        timer.cancel();
        timer.purge();
    }

}
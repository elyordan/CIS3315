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
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.temple.audiobookplayer.AudiobookService;
import android.app.DownloadManager;
import java.io.File;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayList<Book> bookArrayList = new ArrayList<>();
    BookList booksCollection;
    boolean twoWindows;
    Book currentBook;
    Book audioBook;
    BookDetailsFragment currentDetails;
    Button searchButton;
    String webURL;
    RequestQueue requestQueue;
    SharedPreferences sharedPref;
    private ControlFragment controlFragment;
    private AudiobookService.MediaControlBinder audioBookService;
    private SeekBar seekbar;
    private int progress;
    private boolean playing;
    private TimerTask tTask;
    private Timer timer;
    public ServiceConnection serviceConnection;
    File bookFile;
    DownloadManager downloadManager;
    private final String downloadBookURL = "https://kamorris.com/lab/audlib/download.php?id=";
    long downloadID;
    String lastPlayedBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twoWindows = (findViewById(R.id.book_details_fragment) != null);

        Resources res = getResources();
        webURL = res.getString(R.string.bookURL);

        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder binder) {
                audioBookService = (AudiobookService.MediaControlBinder)binder;
            }

            public void onServiceDisconnected(ComponentName className) {
                audioBookService = null;
            }
        };

        Intent intent;
        intent = new Intent(this, AudiobookService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        requestQueue = Volley.newRequestQueue(this);

        booksCollection = new BookList(this, bookArrayList);

        currentDetails = BookDetailsFragment.newInstance(currentBook);
        controlFragment = ControlFragment.newInstance(this);
        Fill_The_Fragment(R.id.fragment_control, controlFragment, false);
        Fill_The_Fragment(R.id.book_list_fragment, BookListFragment.newInstance(booksCollection, this), false);


        if (twoWindows) {
            Fill_The_Fragment(R.id.book_details_fragment, currentDetails, false);
        }

        sharedPref = this.getSharedPreferences("edu.temple.bookshelf", Context.MODE_PRIVATE);
        String search_Book_Term = sharedPref.getString("search_Book_Term", "");

        UpdateListOfBooks(search_Book_Term);
        onClickButtonMethod();

        createTimerTask();


        String bookTitle = sharedPref.getString("bookTitle", null);
        if(bookTitle != null) {
            int bookID = sharedPref.getInt("bookID", 0);
            progress = sharedPref.getInt("progress", 0);
            int bookDuration = sharedPref.getInt("bookDuration", 0);
            String bookAuthor = sharedPref.getString("bookAuthor", "");
            String bookURL = sharedPref.getString("bookURL", "");
            currentBook = new Book( bookTitle, bookAuthor, bookURL, bookID, bookDuration);
            audioBook = currentBook;
            controlFragment.setPlayBook(audioBook);

            TimerTask task = new TimerTask()
            {
                public void run()
                {
                    playing = true;
                    setupSeekBar();
                    audioBookService.play(audioBook.getId(), progress);
                }
            };

            long initialDelay = 5000;
            timer.schedule(task, initialDelay);
        }
    }

    private void createTimerTask() {
        timer = new Timer("Timer");
        tTask = new TimerTask() {
            public void run() {
                seekTimeUpdate();
            }
        };
    }

    private void onClickButtonMethod() {
        searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(v -> Start_Book_Search());
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

        controlFragment = ControlFragment.newInstance(this);
        Fill_The_Fragment(R.id.fragment_control, controlFragment, false);
        controlFragment.setPlayBook(audioBook);
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlString, null, (Response.Listener<JSONArray>) response -> {
            try {
                ArrayList<Book> newList = new ArrayList<>();

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
        }, (Response.ErrorListener) Throwable::printStackTrace);
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

    public void playBook() {

        if(currentBook == null)
            return;

        if(audioBookService == null)
            return;

        audioBookService.stop();
        progress = 0;
        audioBook = currentBook;
        controlFragment.setPlayBook(audioBook);
        setupSeekBar();
        playing = true;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bookID", audioBook.getId());
        editor.putString("bookTitle", audioBook.getTitle());
        editor.putString("bookAuthor", audioBook.getAuthor());
        editor.putString("bookURL", audioBook.getCoverURL());
        editor.putInt("bookDuration", audioBook.getDuration());
        editor.putInt("progress", 0);
        editor.apply();


        long delay = 2000;

        /* Need to use a try and catch here because if the play button is pressed while no book is being played it will crash the app */
        try {
            timer.schedule(tTask, delay);
            createTimerTask();
        } catch (Exception e) {
            Log.e(("This is the error: "), "error");
        }


        if ( currentBook != null) {
            Log.d("PLAY", "Now playing: " + currentBook.getTitle());

            String bookFileName = currentBook.getTitle().replaceAll(" ", "_");
            bookFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), bookFileName);
            Log.d("FILE", "book file exists: " + bookFile.exists());
            Log.d("FILE", (float) (bookFile.length()) / 1000000f + " MB");

            if (bookFile.exists()) {
                loadBookProgress();
                audioBookService.play(bookFile, progress);
                Toast.makeText(MainActivity.this, "Doing first if!",
                        Toast.LENGTH_LONG).show();

            } else {

                String url = downloadBookURL + currentBook.getId();
                downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                        .setDestinationUri(Uri.fromFile(bookFile))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setTitle(bookFileName);
                downloadID = downloadManager.enqueue(request);

                Log.e("Book Downloaded", "True");
                Toast.makeText(MainActivity.this, "Doing first else!" ,
                        Toast.LENGTH_LONG).show();

                if (!playing) {
                    audioBookService.pause();
                } else {
                    audioBookService.play(currentBook.getId(), progress);
                    Toast.makeText(MainActivity.this, "Doing second else!",
                            Toast.LENGTH_LONG).show();
                }
            }

        }


    }

    public void pause() {
        audioBookService.pause();

        if(audioBook != null)
            playing = !playing;

        if(!playing) {
            timer.cancel();
            timer.purge();
        }
    }

    public void stop() {

        SharedPreferences.Editor editor = sharedPref.edit();

        try {

            editor.putInt(audioBook.getTitle(), 0).apply();
        } catch (Exception e) {
            Log.e(("This is the error: "), "error");
        }




        audioBookService.stop();
        playing = false;

        audioBook = null;
        controlFragment.setPlayBook(null);

        editor.putString("bookTitle", null);
        editor.apply();

        timer.cancel();
        timer.purge();
        progress = 0;
        seekbar.setProgress(progress);

    }

    public void saveBookProgress() {
        if (audioBook != null) {
            int savedPosition = Math.max(0, progress - 10);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(audioBook.getTitle(), savedPosition).apply();
            Toast.makeText(MainActivity.this, "Progress saved for " + audioBook.getTitle() + " at: " + savedPosition,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void loadBookProgress() {
        if (currentBook != null) {
            progress = sharedPref.getInt(currentBook.getTitle(), 0);
            Toast.makeText(MainActivity.this, "Progress loaded for " + currentBook.getTitle() + " at: " + progress,
                    Toast.LENGTH_LONG).show();
            seekTimeUpdate();
        } else
            Log.d("SAVE", "Selected book was null?");
    }


    public void seekTimeUpdate() {

        progress += 2;

        seekbar.setProgress(progress);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress", progress);
        editor.apply();

        createTimerTask();
        long delay = 2000;
        timer.schedule(tTask, delay);
    }

    public void setupSeekBar() {
        seekbar = findViewById(R.id.seekBar);

        if(audioBook != null) {
            seekbar.setMax(audioBook.getDuration());
            seekbar.setProgress(progress);
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int newProgress, boolean fromUser) {
                if (fromUser) {
                    audioBookService.seekTo(newProgress);
                    progress = newProgress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //empty method
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //empty method
            }
        });
    }

}
package edu.temple.bookshelf;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ControlFragment extends Fragment {
    private boolean playing;
    private Book currBook;
    private MainActivity parentAct;
    private Button playButton, pauseButton, stopButton;
    private TextView nowPlaying;

    public ControlFragment() {
        //Required empty default constructor
    }

    public static ControlFragment newInstance() {
        ControlFragment myfrag = new ControlFragment();
        myfrag.playing = false;
        return myfrag;
    }

    public static ControlFragment newInstance(MainActivity parent) {
        ControlFragment myfrag = new ControlFragment();
        myfrag.playing = false;
        myfrag.parentAct = parent;
        return myfrag;
    }

    public void setPlayBook(Book book) {
        currBook = book;

        if(currBook != null)
            playing = true;
        else
            playing = false;

        updateHeader();
    }

    private void updateHeader()
    {
        String header = getString(R.string.nowPlaying);

        if(currBook != null)
            header += currBook.getTitle();

        if(nowPlaying != null)
            nowPlaying.setText(header);
    }

    private void playPause()
    {
        playing = !playing;

        parentAct.pause(); //Pause handles the logic for if already paused or not
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, null);

        nowPlaying = v.findViewById(R.id.nowPlaying);
        updateHeader();

        //Set up Listener for play/pause button
        playButton = v.findViewById(R.id.playButtom);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playPause();
            }
        });

        stopButton = v.findViewById(R.id.stopButtom);
        stopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                parentAct.stop();
            }
        });

//        //Set up Listener for Search
//        search = v.findViewById(R.id.search);
//        search.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                parentAct.StartBookSearch();
//            }
//        });
//
        return v;
    }
}


package edu.temple.bookshelf;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ControlFragment extends Fragment {
    private boolean playing;
    private Book currentBook;
    private MainActivity parentActivity;
    private TextView currentlyPlaying;

    public ControlFragment() {
        //Required empty default constructor
    }

    public static ControlFragment newInstance() {
        ControlFragment controlFragment = new ControlFragment();
        controlFragment.playing = false;
        return controlFragment;
    }

    public static ControlFragment newInstance(MainActivity parent) {
        ControlFragment controlFragment = new ControlFragment();
        controlFragment.playing = false;
        controlFragment.parentActivity = parent;
        return controlFragment;
    }

    public void setPlayBook(Book book) {
        currentBook = book;

        playing = currentBook != null;
        updateNowPlayingBook();
    }

    private void updateNowPlayingBook() {
        String header = getString(R.string.nowPlaying);

        if(currentBook != null)
            header += currentBook.getTitle();

        if(currentlyPlaying != null)
            currentlyPlaying.setText(header);
    }

    private void playAndPause() {
        playing = !playing;

        parentActivity.pause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_control, null);

        currentlyPlaying = v.findViewById(R.id.nowPlaying);
        updateNowPlayingBook();

        Button playButton = v.findViewById(R.id.playButtom);
        playButton.setOnClickListener(v1 -> parentActivity.playBook());

        Button pauseButton = v.findViewById(R.id.pauseButtom);
        pauseButton.setOnClickListener(v12 -> playAndPause());

        Button stopButton = v.findViewById(R.id.stopButtom);
        stopButton.setOnClickListener(v13 -> parentActivity.stop());
        return v;
    }
}


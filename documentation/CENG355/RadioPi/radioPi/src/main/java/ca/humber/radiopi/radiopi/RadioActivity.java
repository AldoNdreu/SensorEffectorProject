package ca.humber.radiopi.radiopi;

/**
 * Created by ryan on 2018-03-19.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RadioActivity extends Activity {

    Button b_play;
    MediaPlayer mediaPlayer;
    boolean prepared = false;          //These are just for checking if were ready to play the stream
    boolean started = false;          //and if the stream has started
    String name, streamLink, description, imageURL;
    URL url1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        //Leave Radio Page open until user locks the phone or exits the page
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(this, getResources().getString(R.string.radioToast), Toast.LENGTH_LONG).show();

        //Passing string values from onItemClick() in StationListFragment
        Bundle bundle = getIntent().getExtras();
        streamLink = bundle.getString(getString(R.string.link));
        imageURL = bundle.getString(getString(R.string.imageurl));
        description = bundle.getString(getString(R.string.description));

        TextView descripView = (TextView) findViewById(R.id.descripView);
        descripView.setText(description);

        b_play = (Button) findViewById(R.id.b_play);
        b_play.setEnabled(false);
        b_play.setText(R.string.LOADING);

        //Setting up madia player here
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(streamLink);

        //checking for a real URL that gets passed in from bundle
        try {
            url1  = new URL(imageURL);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        //Setting pause/play button, after the loading is complete
        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(started){
                    started = false;
                    mediaPlayer.pause();
                    b_play.setText(R.string.PLAY);
                } else {
                    started = true;
                    mediaPlayer.start();
                    b_play.setText(R.string.PAUSE);
                }
            }
        });
    }

    //This task is for setting our radio stream with the first string passed in doInBackground
    class PlayerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String...strings){

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prepared;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean){
            super.onPostExecute(aBoolean);
            b_play.setEnabled(true);
            b_play.setText(R.string.PLAY);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
        }
    }
}
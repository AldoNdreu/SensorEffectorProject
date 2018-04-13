package ca.humber.radiopi.radiopi;

/**
 * Created by ryan on 2018-03-19.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import ca.humber.radiopi.radiopi.MainActivity;
import ca.humber.radiopi.radiopi.R;
import ca.humber.radiopi.radiopi.RadioActivity;
import ca.humber.radiopi.radiopi.adapter.StationListAdapter;
import ca.humber.radiopi.radiopi.beans.Station;
import ca.humber.radiopi.radiopi.utils.SharedPreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RadioActivity extends Activity {

    Button b_play;
    MediaPlayer mediaPlayer;
    boolean prepared = false;          //These are just for checking if were ready to play the stream
    boolean started = false;          //and if the stream has started
    String name, freq, description, imageURL;
    URL url1;

    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference(getString(R.string.stations));

        //Leave Radio Page open until user locks the phone or exits the page
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Toast.makeText(this, getResources().getString(R.string.radioToast), Toast.LENGTH_LONG).show();
        //Passing string values from onItemClick() in StationListFragment
        Bundle bundle = getIntent().getExtras();
        freq = bundle.getString(getString(R.string.freq));
        imageURL = bundle.getString(getString(R.string.imageurl));
        description = bundle.getString(getString(R.string.description));

        TextView descripView = (TextView) findViewById(R.id.descripView);
        descripView.setText(description);

        b_play = (Button) findViewById(R.id.b_play);
        //b_play.setEnabled(false);
        //b_play.setText(R.string.LOADING);

        //Setting up madia player here
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //new PlayerTask().execute(streamLink);

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
                    //b_play.setText(R.string.PLAY);
                    activityChange();
                } else {
                    started = true;
                    mediaPlayer.start();
                    b_play.setText(R.string.MAIN_MENU);
                    reference.child("Frequency").child("Name").setValue(freq);
                }
            }
        });
    }

    //This task is for setting our radio stream with the first string passed in doInBackground
    /* class PlayerTask extends AsyncTask<String, Void, Boolean> {
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
    } */

    public void activityChange(){
        Intent intent = new Intent(RadioActivity.this, MainActivity.class);
        startActivity(intent);
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
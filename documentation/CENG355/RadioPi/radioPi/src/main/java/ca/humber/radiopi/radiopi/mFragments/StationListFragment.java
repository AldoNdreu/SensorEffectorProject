/*
UNITED ENGINEERS
Was use to add products to the list and populate them with meaningful data
- handles data for the list item
- displaying toast alerts
*/
package ca.humber.radiopi.radiopi.mFragments;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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


/**
 * Edited  by ryan on 2018-03-25.
 */

public class StationListFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

    public static final String ARG_ITEM_ID = "station_list";

    /*
        Initialized Strings for the stations frequencies.
    */
    public static String boom_Name, boom_Freq, boom_Description;
    public static String cbc_Name, cbc_Freq, cbc_Description;
    public static String humbercollege_Name, humbercollege_Freq, humbercollege_Description;
    public static String kiss_Name, kiss_Freq, kiss_Description;
    public static String theMove_Name, theMove_Freq, theMove_Description;
    public static String z1035_Name, z1035_Freq, z1035_Description;

    Intent i;
    Activity activity;

    //This is where our data is going to end up
    ListView stationListView;
    List<Station> stations;
    //Which then gets passed to this adapter
    StationListAdapter stationListAdapter;

    SharedPreference sharedPreference;

    //DATABASE REFERENCES
    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_station_list, container, false);
        findViewsById(view);

        //Initializing those above references
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference(getString(R.string.stations));

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //A lot of repeated code to read in all our strings from our Firebase Database
                //This should be done in a for loop...

                if (dataSnapshot.exists()){

                    boom_Name = dataSnapshot.child("Boom973").child("Name").getValue().toString();
                    boom_Freq = dataSnapshot.child("Boom973").child("Frequency").getValue().toString();
                    boom_Description = dataSnapshot.child("Boom973").child("Description").getValue().toString();

                    cbc_Name = dataSnapshot.child("CBCMusic941").child("Name").getValue().toString();
                    cbc_Freq = dataSnapshot.child("CBCMusic941").child("Frequency").getValue().toString();
                    cbc_Description = dataSnapshot.child("CBCMusic941").child("Description").getValue().toString();

                    humbercollege_Name = dataSnapshot.child("HumberCollege969").child("Name").getValue().toString();
                    humbercollege_Freq = dataSnapshot.child("HumberCollege969").child("Frequency").getValue().toString();
                    humbercollege_Description = dataSnapshot.child("HumberCollege969").child("Description").getValue().toString();

                    kiss_Name = dataSnapshot.child("Kiss925").child("Name").getValue().toString();
                    kiss_Freq = dataSnapshot.child("Kiss925").child("Frequency").getValue().toString();
                    kiss_Description = dataSnapshot.child("Kiss925").child("Description").getValue().toString();

                    theMove_Name = dataSnapshot.child("TheMove935").child("Name").getValue().toString();
                    theMove_Freq = dataSnapshot.child("TheMove935").child("Frequency").getValue().toString();
                    theMove_Description = dataSnapshot.child("TheMove935").child("Description").getValue().toString();

                    z1035_Name = dataSnapshot.child("z1035").child("Name").getValue().toString();
                    z1035_Freq = dataSnapshot.child("z1035").child("Frequency").getValue().toString();
                    z1035_Description = dataSnapshot.child("z1035").child("Description").getValue().toString();

                    
                    //This is usually outside the ValueEventListener
                    setStations();
                    stationListAdapter = new StationListAdapter(activity, stations);
                    stationListView.setAdapter(stationListAdapter);
                    stationListView.setOnItemClickListener(StationListFragment.this);
                    stationListView.setOnItemLongClickListener(StationListFragment.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
    }

    private void setStations() {
        //Read in all our strings into an array list of type station

        Station boom973 = new Station(1, boom_Name, boom_Freq, boom_Description);
        Station cbc941 = new Station(2, cbc_Name, cbc_Freq, cbc_Description);
        Station humbercollege969 = new Station(3, humbercollege_Name, humbercollege_Freq, humbercollege_Description);
        Station kiss925 = new Station(4, kiss_Name, kiss_Freq, kiss_Description);
        Station theMove935 = new Station(5, theMove_Name, theMove_Freq, theMove_Description);
        Station z1035 = new Station(6, z1035_Name, z1035_Freq, z1035_Description);

        stations = new ArrayList<>();

        stations.add(boom973);
        stations.add(cbc941);
        stations.add(humbercollege969);
        stations.add(kiss925);
        stations.add(theMove935);
        stations.add(z1035);


    }

    private void findViewsById(View view) { stationListView = (ListView) view.findViewById(R.id.list_station);}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Station station = (Station) parent.getItemAtPosition(position);

        String description = station.getDescription();
        String freq = station.getFreq();
       // String imageURL = station.getImageURL();

        i = new Intent(getActivity(), RadioActivity.class);
        i.putExtra(getActivity().getString(R.string.description), description);
        i.putExtra(getActivity().getString(R.string.freq), freq);
        //i.putExtra(getActivity().getString(R.string.imageurl), imageURL);

        //reference.child("Frequency").child("Name").setValue(cbc_Freq);

        startActivity(i);
        /*Use station.postion to decide which stream is being selected then run Radio activity*/
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
    //Handles all the favouriting tasks .  This function is repated in FavouriteListFragment
        ImageView button = (ImageView) view.findViewById(R.id.imgbtn_favorite);
        String tag = button.getTag().toString();
        if (tag.equalsIgnoreCase(getString(R.string.grey))) {
            sharedPreference.addFavorite(activity, stations.get(position)); // Figure the wrong 2nd argument...
            // Found out I had a package problem for both of the sharedPreference.
            Toast.makeText(activity, activity.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
            button.setTag(getString(R.string.red));
            button.setImageResource(R.drawable.heart_red);
        } else {
            sharedPreference.removeFavorite(activity, stations.get(position)); // Figure the wrong 2nd argument...
            // Found out I had a package problem for both of the sharedPreference.
            button.setTag(getString(R.string.grey));
            button.setImageResource(R.drawable.heart_grey);
            Toast.makeText(activity, activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

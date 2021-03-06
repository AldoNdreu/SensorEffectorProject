/*
UNITED ENGINEERS
This class handles the favourited item within the list.
- Retrieve and display all radio stations that have been favourited in StationListFragment
- Logic for the heart image
- Pulling existing favs from SharedPrefs
- Also makes toast alerts
*/
package ca.humber.radiopi.radiopi.mFragments;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import ca.humber.radiopi.radiopi.R;
import ca.humber.radiopi.radiopi.RadioActivity;
import ca.humber.radiopi.radiopi.adapter.StationListAdapter;
import ca.humber.radiopi.radiopi.beans.Station;
import ca.humber.radiopi.radiopi.utils.SharedPreference;

/**
 * Created by Edited on 2018-03-25.
 */

public class FavouriteListFragment extends Fragment{
    public static final String ARG_ITEM_ID = "favorite_list";

    Intent i;
    ListView favoriteList;
    SharedPreference sharedPreference;
    List<Station> favorites;
    Activity activity;
    StationListAdapter stationListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_list, container, false);
        // Get favorite items from SharedPreferences.
        sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(activity);
        //Checking if anything is available
        if (favorites == null) {
            showAlert(
                    getResources().getString(R.string.no_favorites_items),
                    getResources().getString(R.string.no_favorites_msg));
        } else {
            if (favorites.size() == 0) {
                showAlert(
                        getResources().getString(R.string.no_favorites_items),
                        getResources().getString(R.string.no_favorites_msg));
            }
            favoriteList = (ListView) view.findViewById(R.id.list_station);
            //Whether or not favourites is empty we still need the listview
            if (favorites != null) {
                stationListAdapter = new StationListAdapter(activity, favorites);
                favoriteList.setAdapter(stationListAdapter);

                favoriteList.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                        //Copied code from StationListFragment onItemClick
                        Station station = (Station) parent.getItemAtPosition(position);

                        String description = station.getDescription();
                        String imageURL = station.getImageURL();
                        String freq = station.getFreq();

                        i = new Intent(getActivity(), RadioActivity.class);

                        i.putExtra(getActivity().getString(R.string.description), description);
                        i.putExtra(getActivity().getString(R.string.imageurl), imageURL);
                        i.putExtra(getActivity().getString(R.string.freq), freq);

                        startActivity(i);
                    }
                });

                //set our heart image to red or grey
                //however if its on favouritelistfragment it will be red by default
                //and grey only for a moment then it will be removed from the list
                favoriteList.setOnItemLongClickListener(new OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                ImageView button = (ImageView) view.findViewById(R.id.imgbtn_favorite);

                                String tag = button.getTag().toString();
                                if (tag.equalsIgnoreCase(getString(R.string.grey))) {
                                    sharedPreference.addFavorite(activity, favorites.get(position));
                                    Toast.makeText(
                                            activity, activity.getResources().getString(R.string.add_favr),
                                            Toast.LENGTH_SHORT).show();

                                    button.setTag(getContext().getString(R.string.red));
                                    button.setImageResource(R.drawable.heart_red);
                                } else {
                                    sharedPreference.removeFavorite(activity, favorites.get(position));
                                    button.setTag(getContext().getString(R.string.grey));
                                    button.setImageResource(R.drawable.heart_grey);
                                    stationListAdapter.remove(favorites.get(position));
                                    Toast.makeText(
                                            activity, activity.getResources().getString(R.string.remove_favr),
                                            Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        });
            }
        }
        return view;
    }

    public void showAlert(String title, String message) {
        //if favourites is empty display a dialog box that instructs the user how to add a favourite
        if (activity != null && !activity.isFinishing()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);

            // setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // activity.finish();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
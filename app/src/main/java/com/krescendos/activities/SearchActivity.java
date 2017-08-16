package com.krescendos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.R;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.web.Requester;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Requester requester;
    private TrackListAdapter listAdapter;
    private List<Track> trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requester = new Requester(getApplicationContext());
        listAdapter = new TrackListAdapter(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.searchResultList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Track selection = trackList.get(i);
                Gson gson = new Gson();
                String json = gson.toJson(selection, Track.class);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("AddedTrack", json);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) item.getActionView();

        // Starts as "Recommended" until user searches
        final TextView resultHeader = (TextView) findViewById(R.id.searchRecommendedText);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("QUERY:", query);
                requester.search(query, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RESPONSE", response.toString());
                        Type listType = new TypeToken<List<Track>>() {
                        }.getType();
                        trackList = new Gson().fromJson(response.toString(), listType);
                        Log.d("QUERYRESPONSELISTSIZE", "" + trackList.size());
                        resultHeader.setText(R.string.searchResults);
                        listAdapter.updateTracks(trackList);
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }
}

package com.krescendos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.R;
import com.krescendos.Requester;
import com.krescendos.TrackListAdapter;
import com.krescendos.domain.Track;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private Requester requester;
    private TrackListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requester = new Requester(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) item.getActionView();
        final ListView listView = (ListView) findViewById(R.id.searchResultList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("QUERY:", query);
                requester.search(query, new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RESPONSE", response.toString());
                        Type listType = new TypeToken<List<Track>>() {}.getType();
                        List<Track> trackList = new Gson().fromJson(response.toString(), listType);
                        Log.d("QUERYRESPONSELISTSIZE", ""+trackList.size());
                        listAdapter = new TrackListAdapter(getApplicationContext(), trackList);
                        listAdapter.notifyDataSetChanged();
                        listView.setAdapter(listAdapter);
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

package com.createch.species;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.createch.species.Adapter.SpeciesAdapter;
import com.createch.species.Model.Species;
import com.google.android.material.card.MaterialCardView;
import com.paging.listview.PagingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    String next = "https://swapi.dev/api/species/?page=1";
    String previous = null;
    int count ;
    RecyclerView recyclerView;
    EndlessRecyclerViewScrollListener scrollListener;
    ArrayList<Species> speciesList ,tempList;
    SpeciesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speciesList = new ArrayList<>();
        tempList = new ArrayList<>();
        new GetSpecies().execute();




                 recyclerView =  findViewById(R.id.list);
               adapter = new SpeciesAdapter(speciesList,getApplicationContext());
        recyclerView.setAdapter(adapter);
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
                 scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        int curSize = speciesList.size();

                        // check if there is more data to show
                        if (curSize < count && next != null)
                            new GetSpecies().execute();

                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemRangeInserted(curSize, tempList.size() - 1);
                            }
                        });
                    }
                };
                recyclerView.addOnScrollListener(scrollListener);








        // refresh swiper ....
        SwipeRefreshLayout swpier = findViewById(R.id.swiperefresh);
        swpier.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.scrollToPosition(0);
                tempList.clear();
                speciesList.clear();
                new GetSpecies().execute();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swpier.setRefreshing(false);
                    }
                }, 1500);
            }
        });



    }



    private class GetSpecies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(next);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");
                    next = jsonObj.getString("next");
                    previous = jsonObj.getString("previous");
                    count = jsonObj.getInt("count");

                    // looping through All results
                    tempList.clear();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
                        String name = c.getString("name");
                        String designation = c.getString("designation");
                        String classification = c.getString("classification");
                        boolean sele = false;


                        // tmp  for single
                        Species sp = new Species();

                        sp.setName(name);
                        sp.setClassification(classification);
                        sp.setDesignation(designation);
                        sp.setSelected(sele);

                        // adding  to  list
                        tempList.add(sp);
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            speciesList.addAll(tempList);
             adapter = new SpeciesAdapter(speciesList,getApplicationContext());
            recyclerView.setAdapter(adapter);
        }
    }
}
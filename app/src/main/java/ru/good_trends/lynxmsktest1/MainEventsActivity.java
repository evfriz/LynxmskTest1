package ru.good_trends.lynxmsktest1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainEventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String LOG_TAG = "myLogs";
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    static String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Category = "football";
/*
        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 15000); // задержка 1000ms, повтор 15000ms
*/
        new ParseTask().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ImageView img = (ImageView) findViewById(R.id.imageView2);

        if (id == R.id.nav_football) {
            Category = "football";
            img.setImageDrawable(getDrawable(R.drawable.png_football));
        } else if (id == R.id.nav_hockey) {
            Category = "hockey";
            img.setImageDrawable(getDrawable(R.drawable.png_hockey));
        } else if (id == R.id.nav_tennis) {
            Category = "tennis";
            img.setImageDrawable(getDrawable(R.drawable.png_tennis));
        } else if (id == R.id.nav_basketball) {
            Category = "basketball";
            img.setImageDrawable(getDrawable(R.drawable.png_basketball));
        } else if (id == R.id.nav_volleyball) {
            Category = "volleyball";
            img.setImageDrawable(getDrawable(R.drawable.png_volleyball));
        } else if (id == R.id.nav_cybersport) {
            Category = "cybersport";
            img.setImageDrawable(getDrawable(R.drawable.png_cybersport));
        }

        new ParseTask().execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class ParseTask extends AsyncTask<Void, Void, String> implements EventsRecyclerViewAdapter.ItemClickListener {
    // Получение и разбор JSON

        EventsRecyclerViewAdapter adapter;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://mikonatoruri.win/list.php?category="+Category);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);

            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray currency = dataJsonObj.getJSONArray("events");

                ArrayList<EventsClass> newList = new ArrayList<EventsClass>();

                // перебираем и выводим данные
                for (int i = 0; i < currency.length(); i++) {
                    JSONObject с_currency = currency.getJSONObject(i);

                    String title = с_currency.getString("title");
                    String coefficient = с_currency.getString("coefficient");
                    String time = с_currency.getString("time");
                    String place = с_currency.getString("place");
                    String preview = с_currency.getString("preview");
                    String article = с_currency.getString("article");

                    newList.add(new EventsClass(title, coefficient, time, place, preview, article));
                }

                //вывод данных в RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rv_events);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new EventsRecyclerViewAdapter(newList);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override public void onItemClick(View view, int position) {
            Toast.makeText(getApplicationContext(), "You clicked " + adapter.getArticle(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainEventsActivity.this, ArticleActivity.class);
            intent.putExtra("article", adapter.getArticle(position));
            startActivity(intent);
        }
    }

    //Можно использовать для обновления через заданные промежутки
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ParseTask().execute();
                }
            });
        }
    }


}

package ru.good_trends.lynxmsktest1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    static String Article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Article = getIntent().getExtras().getString("article");
        new ParseTask().execute();
    }


    private class ParseTask extends AsyncTask<Void, Void, String> implements ArticleRecyclerViewAdapter.ItemClickListener {
    // Получение и разбор JSON

        ArticleRecyclerViewAdapter adapter;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://mikonatoruri.win/post.php?article="+Article);

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

                TextView tv_team1 = findViewById(R.id.tv_team1);
                tv_team1.setText(dataJsonObj.getString("team1"));

                TextView tv_team2 = findViewById(R.id.tv_team2);
                tv_team2.setText(dataJsonObj.getString("team2"));

                TextView tv_time = findViewById(R.id.tv_time);
                tv_time.setText(dataJsonObj.getString("time"));

                TextView tv_tournament = findViewById(R.id.tv_tournament);
                tv_tournament.setText(dataJsonObj.getString("tournament"));

                TextView tv_place = findViewById(R.id.tv_place);
                tv_place.setText(dataJsonObj.getString("place"));

                TextView tv_prediction = findViewById(R.id.tv_prediction);
                tv_prediction.setText(dataJsonObj.getString("prediction"));

                //обработка массива "article"
                JSONArray jsa_article = dataJsonObj.getJSONArray("article");
                ArrayList<ArticleClass> newList = new ArrayList<ArticleClass>();

                // перебираем и выводим данные
                for (int i = 0; i < jsa_article.length(); i++) {
                    JSONObject jsa_article_i = jsa_article.getJSONObject(i);
                    String header = jsa_article_i.getString("header");
                    String text = jsa_article_i.getString("text");
                    newList.add(new ArticleClass(header, text));
                }

                //вывод данных в RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rv_article);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ArticleRecyclerViewAdapter(newList);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override public void onItemClick(View view, int position) {

        }
    }


}

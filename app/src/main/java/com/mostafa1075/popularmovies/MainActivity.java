package com.mostafa1075.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mostafa1075.popularmovies.utils.JsonUtils;
import com.mostafa1075.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // TODO: Design the UI and use a RecyclerView
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.tv);

        // TODO: get the sortByPath from user settings and page number from UI
        URL url =  NetworkUtils.getUrl("top_rated", "1");
        new retreiveDataAsyncTask().execute(url);
    }
    // TODO: replace AsyncTask with an AsyncTaskLoader
    private class retreiveDataAsyncTask extends AsyncTask<URL, Void, String>{
        @Override
        protected String doInBackground(URL... urls) {
            String HttpResponse;
            try {
                HttpResponse = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                HttpResponse = "";
            }
            return HttpResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // TODO: parse the JSON result and use it to populate the UI
            tv.setText(result);
        }
    }
}

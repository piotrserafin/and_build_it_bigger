package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import pl.piotrserafin.jokesdisplay.JokesActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void sendJokeToDisplayActivity(String result) {
        Intent jokesDisplayIntent = new Intent(this, JokesActivity.class);
        jokesDisplayIntent.putExtra(JokesActivity.JOKE_KEY, result);
        startActivity(jokesDisplayIntent);
    }

    public void tellJoke(View view) {
        JokesEndpointsAsyncTask jokesEndpointsAsyncTask = new JokesEndpointsAsyncTask();
        jokesEndpointsAsyncTask.setListener(this::sendJokeToDisplayActivity);
        jokesEndpointsAsyncTask.execute();
    }

    static class JokesEndpointsAsyncTask extends AsyncTask<Void, Void, String> {

        private JokesEndpointsAsyncTaskListener listener;
        private MyApi myJokesApiService = null;

        JokesEndpointsAsyncTask() {
        }

        @Override
        protected String doInBackground(Void... params) {
            if (myJokesApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(
                                abstractGoogleClientRequest ->
                                        abstractGoogleClientRequest.setDisableGZipContent(true));
                myJokesApiService = builder.build();
            }

            try {
                return myJokesApiService.provideJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result){
            if (listener != null) {
                listener.onJokesEndpointsAsyncTaskFinished(result);
            }
        }

        void setListener(JokesEndpointsAsyncTaskListener listener) {
            this.listener = listener;
        }

        public interface JokesEndpointsAsyncTaskListener {
            void onJokesEndpointsAsyncTaskFinished(String result);
        }
    }
}

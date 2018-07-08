package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import pl.piotrserafin.jokesdisplay.JokesActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button jokeButton;
    private ProgressBar jokeLoadingPb;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        jokeLoadingPb = root.findViewById(R.id.pb_loading_joke);
        jokeButton = root.findViewById(R.id.joke_btn);
        jokeButton.setOnClickListener(this::tellJoke);

        return root;
    }

    private void sendJokeToDisplayActivity(String result) {
        jokeLoadingPb.setVisibility(View.INVISIBLE);
        Intent jokesDisplayIntent = new Intent(getActivity(), JokesActivity.class);
        jokesDisplayIntent.putExtra(JokesActivity.JOKE_KEY, result);
        startActivity(jokesDisplayIntent);
    }

    public void tellJoke(View view) {
        jokeLoadingPb.setVisibility(View.VISIBLE);
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

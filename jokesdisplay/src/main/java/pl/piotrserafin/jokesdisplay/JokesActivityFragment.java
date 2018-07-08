package pl.piotrserafin.jokesdisplay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JokesActivityFragment extends Fragment {

    public JokesActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_jokes_activity, container, false);

        if (getActivity() != null &&
                getActivity().getIntent() != null &&
                getActivity().getIntent().getExtras() != null) {

            TextView jokeTextView = root.findViewById(R.id.joke_tv);

            String joke = getActivity().getIntent().getStringExtra(JokesActivity.JOKE_KEY);

            if (joke != null && joke.length() != 0) {
                jokeTextView.setText(joke);
            }
        }
        return root;
    }
}

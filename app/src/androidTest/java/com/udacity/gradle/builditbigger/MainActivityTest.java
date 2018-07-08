package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private final int fragmentIndex = 0;

    private final String JOKE_TEXT = "This is not funny Joke";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        FragmentManager fragmentManager = mActivityRule.getActivity().getSupportFragmentManager();
        MainActivityFragment fragment = (MainActivityFragment) fragmentManager.getFragments().get(fragmentIndex);
    }

    @Test
    public void testTellJokeNotEmpty() {
        onView(withId(R.id.joke_btn)).perform(click());
        onView(withId(pl.piotrserafin.jokesdisplay.R.id.joke_tv)).check(matches(not(withText(""))));
    }

    @Test
    public void testTellJokeMatchesText() {
        onView(withId(R.id.joke_btn)).perform(click());
        onView(withId(pl.piotrserafin.jokesdisplay.R.id.joke_tv)).check(matches((withText(JOKE_TEXT))));
    }
}
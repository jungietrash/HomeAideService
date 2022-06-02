package com.homeaide.post;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class HOSearchTest {
    @Rule
    public ActivityTestRule<BookJobActivity> search = new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void SearchTest(){ //checks spinner
        onView(withId(R.id.serviceSpinner)).perform(click());
        onData(anything()).atPosition(7).perform(click()); // Explicitly specify the adapter view_booking to use
        onView(withId(R.id.serviceSpinner)).check(matches(withSpinnerText(containsString("IT - Rogers"))));
    }
}

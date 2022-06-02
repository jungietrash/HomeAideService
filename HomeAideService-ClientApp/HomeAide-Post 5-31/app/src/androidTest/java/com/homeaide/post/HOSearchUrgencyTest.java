package com.homeaide.post;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class HOSearchUrgencyTest {
    @Rule
    public ActivityTestRule<BookJobActivity> checkUrgency = new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void CheckUrgencyTest(){
        onView(withId(R.id.sundayMorningCB)).perform(click()); //checks if there is a service provider available sunday or monday morning
        onView(withId(R.id.mondayMorningCB)).perform(click());
        onView(withId(R.id.urgencySpinner)).perform(click());
        onData(anything()).atPosition(2).perform(click()); //sets the urgency to 2 weeks notice
        onView(withId(R.id.timeSpinner)).perform(click());
        onData(anything()).atPosition(2).perform(click()); //sets time to 1.5 hours
        onView(withId(R.id.findAProviderBtn)).perform(click());
    }
}

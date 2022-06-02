package com.homeaide.post;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class AvailableActivityTest {
    @Rule
    public ActivityTestRule<AvailabilityActivity> availableAccountTestI = new ActivityTestRule<>(AvailabilityActivity.class);

    @Test
    public void pastDateTest(){
        onData(anything()).inAdapterView(withId(R.id.listTimeSlots)) // Specify the explicit id of the ListView
                .atPosition(1) // Explicitly specify the adapter view_booking to use
                .perform(click());
    }
}

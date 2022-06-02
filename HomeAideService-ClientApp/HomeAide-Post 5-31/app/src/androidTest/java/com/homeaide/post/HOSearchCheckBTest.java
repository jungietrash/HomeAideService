package com.homeaide.post;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class HOSearchCheckBTest {
    @Rule
    public ActivityTestRule<BookJobActivity> checkButton= new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void CheckButtonTest(){
        onView(withId(R.id.sundayMorningCB)).perform(click()); //checks if there is a service provider available sunday or monday morning
        onView(withId(R.id.mondayMorningCB)).perform(click());
        onView(withId(R.id.findAProviderBtn)).perform(click());
    }
}

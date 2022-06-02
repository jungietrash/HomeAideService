package com.homeaide.post;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class MyAccountTest {
    @Rule
    public ActivityTestRule<MyAccountActivity> accountActivityTestI = new ActivityTestRule<>(MyAccountActivity.class);

    @Test
    public void AccountTest() { //signed in as service provider
        onView(withId(R.id.editAccountBtn)).perform(click());
        onView(withId(R.id.editPhoneNumber)).perform(typeText("1"), closeSoftKeyboard()); //adds too many numbers to phone number
        onView(withId(R.id.editAccountBtn)).perform(click());

    }


}

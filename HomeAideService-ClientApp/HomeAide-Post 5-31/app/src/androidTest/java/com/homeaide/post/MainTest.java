package com.homeaide.post;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class MainTest {
    @Rule
    public ActivityTestRule<ca.uottawa.service4u.MainActivity> signInTestI = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void invalidSignIn(){ //should fail
        onView(withId(R.id.fieldEmail)).perform(typeText("fakeemail@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.emailSignInButton)).perform(click()); //should fail
        //onView(withText("john smith")).check(matches(isDisplayed()));
    }

}


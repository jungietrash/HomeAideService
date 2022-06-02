package com.homeaide.post;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class HOSignInTest {
    @Rule
    public ActivityTestRule<Bookingv2MainActivity> signInTest = new ActivityTestRule<>(Bookingv2MainActivity.class);
    @Test
    public void SignInAsHomeowner(){
        onView(withId(R.id.fieldEmail)).perform(typeText("home@owner.com"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.emailSignInButton)).perform(click()); //should sign in as homeowner
    }
}

package com.homeaide.post;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class ServiceActivityTest {
    @Rule
    public ActivityTestRule<ServicesActivity> adminAddServiceI = new ActivityTestRule<>(ServicesActivity.class);
    @Test
    public void serviceActivityInvalid(){
        onView(withId(R.id.newServiceBtn)).perform(click());
        onView(withId(R.id.nameField)).perform(typeText("Google Home Installation"), closeSoftKeyboard()); //adding service
        // onView(withId(R.id.typeField)).perform(withSpinnerText());
        // onView(withId(R.id.typeField)).perform(Spinner.drop)
        onView(withId(R.id.rateField)).perform(typeText("9.5")); //must be atleast 10
        onView(withId(R.id.addServiceBtn)).perform(click());
    }

}

package edu.kit.psegruppe3.mensax.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/*
    This is NOT a complete test for the CanteenContract.
 */
public class TestCanteenContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_MEAL = "Sahnefruchtjoghurt";
    private static final long TEST_WEATHER_DATE = 1419033600L;  // December 20th, 2014

    /*
        Uncomment this out to test mensa offer function.
     */
    public void testBuildMealWithName() {
        Uri mealUri = CanteenContract.MealEntry.buildMealWithMealNameUri(TEST_MEAL);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildMealWithMealNameUri in " +
                        "CanteenContract.",
                mealUri);
        assertEquals("Error: Mensa meal not properly appended to the end of the Uri",
                TEST_MEAL, mealUri.getLastPathSegment());
        assertEquals("Error: Mensa meal Uri doesn't match our expected result",
                mealUri.toString(),
                "content://edu.kit.psegruppe3.mensax/meal/Sahnefruchtjoghurt"); //TO CHECK!!
    }
}


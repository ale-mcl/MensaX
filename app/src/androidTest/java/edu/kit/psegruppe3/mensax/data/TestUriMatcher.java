package edu.kit.psegruppe3.mensax.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * TestUriMatcher class.
 * @author MensaX-group
 * @version 1.0
 *
 * Note: This class utilizes constants that are declared with package protection inside of the
 * UriMatcher, which is why the test must be in the same data package as the Android app code.
 * (compromise between data hiding and testability)
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String MEAL_QUERY = "Blattsalat";
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    // content://edu.kit.psegruppe3.mensax/offer"
    private static final Uri TEST_OFFER_DIR = CanteenContract.OfferEntry.CONTENT_URI;
    private static final Uri TEST_OFFER_WITH_DATE = CanteenContract.OfferEntry.buildOfferDate(TEST_DATE);

    // content://edu.kit.psegruppe3.mensax/meal"
    private static final Uri TEST_MEAL_DIR = CanteenContract.MealEntry.CONTENT_URI;
    private static final Uri TEST_MEAL_WITH_NAME = CanteenContract.MealEntry.buildMealWithMealNameUri(MEAL_QUERY);

    /**
     * This function tests that THE UriMatcher returns the correct integer value
     * for each of the Uri types that our ContentProvider can handle.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = CanteenProvider.buildUriMatcher();

        assertEquals("Error: The OFFER URI was matched incorrectly.",
                testMatcher.match(TEST_OFFER_DIR), CanteenProvider.OFFER);
        assertEquals("Error: The OFFER WITH DATE URI was matched incorrectly.",
                testMatcher.match(TEST_OFFER_WITH_DATE), CanteenProvider.OFFER_WITH_DATE);
        assertEquals("Error: The MEAL URI was matched incorrectly.",
                testMatcher.match(TEST_MEAL_DIR), CanteenProvider.MEAL);
        assertEquals("Error: The MEAL URI WITH NAME was matched incorrectly.",
                testMatcher.match(TEST_MEAL_WITH_NAME), CanteenProvider.MEAL_WITH_NAME);
    }
}
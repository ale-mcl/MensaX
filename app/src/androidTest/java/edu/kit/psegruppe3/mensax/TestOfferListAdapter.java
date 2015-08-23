package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.test.AndroidTestCase;

import edu.kit.psegruppe3.mensax.datamodels.DailyMenu;
import edu.kit.psegruppe3.mensax.datamodels.Line;
import edu.kit.psegruppe3.mensax.datamodels.Meal;
import edu.kit.psegruppe3.mensax.datamodels.Offer;

/**
 * Test class for the OfferListAdapter.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class TestOfferListAdapter extends AndroidTestCase {

    private Meal meal1;
    private Meal meal2;
    private Meal meal3;
    private Offer offer1;
    private Offer offer2;
    private Offer offer3;
    private DailyMenu dailyMenu;
    private OfferListAdapter testOfferListAdapter;
    private Context context;

    /**
     * This gets run before every test.
     * @throws Exception if something goes wrong
     */
    @Override
    protected void setUp() throws Exception {
        context = getContext();
        String mealName1 = "pizza";
        String mealName2 = "pasta";
        String mealName3 = "salami";
        meal1 = new Meal(mealName1, 1);
        meal2 = new Meal (mealName2, 2);
        meal3 = new Meal(mealName3, 3);
        offer1 = new Offer(meal1, Line.l1, 150, 250, 350, 350);
        offer2 = new Offer(meal2, Line.l2, 150, 250, 350, 350);
        offer3 = new Offer(meal3, Line.l3, 150, 250, 350, 350);
        Offer[] offerArray = {offer1, offer2, offer3};
        dailyMenu = new DailyMenu(offerArray);
        testOfferListAdapter = new OfferListAdapter(context, dailyMenu);

        super.setUp();
    }

    public void testAmmountLines() {
        int groupCount = testOfferListAdapter.getGroupCount();
        assertEquals(10, groupCount);
    }

    public void testAmmountChilds() {
        int childCount1 = testOfferListAdapter.getChildrenCount(1);
        assertEquals(1, childCount1);
        int childCount2 = testOfferListAdapter.getChildrenCount(2);
        assertEquals(1, childCount2);
        int childCount3 = testOfferListAdapter.getChildrenCount(3);
        assertEquals(1, childCount3);
    }

    public void testChildId() {
        long childId = testOfferListAdapter.getChildId(1,1);
        assertEquals(1, childId);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}

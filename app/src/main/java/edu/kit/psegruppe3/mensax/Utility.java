package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import edu.kit.psegruppe3.mensax.datamodels.Meal;

/**
 * Utility class that provides often used methods.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class Utility {

    /**
     * Metod to get the drawable of a Tag.
     * @param context the activity
     * @param tagIndex the indext of the Tag
     * @return drawable object representig the Tag
     */
    public static Drawable getTagDrawable(Context context, int tagIndex){
        String uri = null;
        switch (tagIndex) {
            case Meal.TAG_BIO:
                uri = "@drawable/ic_meal_bio";
                break;
            case Meal.TAG_FISH:
                uri = "@drawable/ic_meal_fish";
                break;
            case Meal.TAG_PORK:
                uri = "@drawable/ic_meal_pork";
                break;
            case Meal.TAG_COW:
                uri = "@drawable/ic_meal_cow";
                break;
            case Meal.TAG_COW_AW:
                uri = "@drawable/ic_meal_cow_aw";
                break;
            case Meal.TAG_VEGAN:
                uri = "@drawable/ic_meal_vegan";
                break;
            case Meal.TAG_VEG:
                uri = "@drawable/ic_meal_veg";
                break;
            default: return null;
        }
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        return context.getResources().getDrawable(imageResource);
    }

    /**
     * Method to get the string description of a Tag
     * @param context the adctivity
     * @param tagIndex the indext of the Tag
     * @return string description of the Tag
     */
    public static String getTagString(Context context, int tagIndex) {
        switch (tagIndex) {
            case Meal.TAG_BIO:
                return context.getString(R.string.tag_bio);
            case Meal.TAG_FISH:
                return context.getString(R.string.tag_fish);
            case Meal.TAG_PORK:
                return context.getString(R.string.tag_pork);
            case Meal.TAG_COW:
                return context.getString(R.string.tag_cow);
            case Meal.TAG_COW_AW:
                return context.getString(R.string.tag_cow_aw);
            case Meal.TAG_VEGAN:
                return context.getString(R.string.tag_vegan);
            case Meal.TAG_VEG:
                return context.getString(R.string.tag_veg);
            default: return null;
        }
    }

    public static String getPreferredPriceGroup(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_priceGroup_key),
                context.getString(R.string.pref_priceGroup_default));
    }
}

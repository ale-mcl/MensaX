package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.kit.psegruppe3.mensax.datamodels.DailyMenu;
import edu.kit.psegruppe3.mensax.datamodels.Line;
import edu.kit.psegruppe3.mensax.datamodels.Meal;
import edu.kit.psegruppe3.mensax.datamodels.Offer;

/**
 * ExpandableListAdapter that shows the daily menu.
 */
public class OfferListAdapter extends BaseExpandableListAdapter {

    private static final int NUM_LINES = 10;
    private DailyMenu mDailyMenu;
    private Context mContext;

    public OfferListAdapter(Context context, DailyMenu dailyMenu) {
        mDailyMenu = dailyMenu;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return NUM_LINES;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int result = 0;
        for (int i = 0; i < mDailyMenu.getOfferCount(); i++) {
            if (mDailyMenu.getOffer(i).getLine().equals(getLine(groupPosition))) {
                result++;
            }
        }
        return result;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getLine(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Line currentLine = getLine(groupPosition);
        for (int i = 0; i < mDailyMenu.getOfferCount(); i++) {
            if (mDailyMenu.getOffer(i).getLine().equals(currentLine)) {
                if (childPosition == 0) {
                    return mDailyMenu.getOffer(i);
                } else {
                    childPosition--;
                }
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Line currentLine = getLine(groupPosition);
        for (int i = 0; i < mDailyMenu.getOfferCount(); i++) {
            if (mDailyMenu.getOffer(i).getLine().equals(currentLine)) {
                if (childPosition == 0) {
                    return mDailyMenu.getOffer(i).getMeal().getMealId();
                } else {
                    childPosition--;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_line, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.list_item_line_textview);
        textView.setText(getLineName(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_offer, parent, false);
        }
        Offer offer = (Offer) getChild(groupPosition, childPosition);

        //TODO: View second tag!
        ImageView image = (ImageView) convertView.findViewById(R.id.firstTagDrawable);
        image.setImageDrawable(getTagDrawable(offer));

        TextView textViewName = (TextView) convertView.findViewById(R.id.list_item_offer_textview_name);
        textViewName.setText(offer.getMeal().getName());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String priceGroup = prefs.getString(mContext.getString(R.string.pref_priceGroup_key),
                mContext.getString(R.string.pref_priceGroup_default));

        float price = 0;
        if (priceGroup.equals(mContext.getString(R.string.pref_priceGroup_student))) {
            price = (float) offer.getPrice(0) / 100;
        } else if (priceGroup.equals(mContext.getString(R.string.pref_priceGroup_guest))) {
            price = (float) offer.getPrice(1) / 100;
        } else if (priceGroup.equals(mContext.getString(R.string.pref_priceGroup_staff))) {
            price = (float) offer.getPrice(2) / 100;
        } else if (priceGroup.equals(mContext.getString(R.string.pref_priceGroup_pupil)) ) {
            price = (float) offer.getPrice(3) / 100;
        }

        String priceFloat = String.format("%.2f", price);
        String real_price = priceFloat + "€";
        TextView textViewPrice = (TextView) convertView.findViewById(R.id.list_item_offer_textview_price);
        textViewPrice.setText(real_price);

        RatingBar globalRating = (RatingBar) convertView.findViewById(R.id.list_item_offer_ratingbar);
        globalRating.setRating((float) offer.getMeal().getGlobalRating());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private Drawable getTagDrawable(Offer offer){
        String uri;
        if (offer.getMeal().hasTag(Meal.TAG_BIO)) {
            uri = "@drawable/ic_meal_bio";
        } else if (offer.getMeal().hasTag(Meal.TAG_COW_AW)) {
            uri = "@drawable/ic_meal_cow_aw";
        } else if (offer.getMeal().hasTag(Meal.TAG_PORK)) {
            uri = "@drawable/ic_meal_pork";
        } else if (offer.getMeal().hasTag(Meal.TAG_VEG)) {
            uri = "@drawable/ic_meal_veg";
        } else if (offer.getMeal().hasTag(Meal.TAG_VEGAN)) {
            uri = "@drawable/ic_meal_vegan";
        } else if (offer.getMeal().hasTag(Meal.TAG_FISH)) {
            uri = "@drawable/ic_meal_fish";
        } else if (offer.getMeal().hasTag(Meal.TAG_COW)) {
            uri = "@drawable/ic_meal_cow";
        } else {
            return null;
        }
        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        return mContext.getResources().getDrawable(imageResource);
    }

    private Line getLine(int position) {
        if (position <= NUM_LINES) {
            switch (position) {
                case 0: return Line.l1;
                case 1: return Line.l2;
                case 2: return Line.l3;
                case 3: return Line.l45;
                case 4: return Line.schnitzelbar;
                case 5: return Line.update;
                case 6: return Line.abend;
                case 7: return Line.aktion;
                case 8: return Line.heisstheke;
                case 9: return Line.nmtisch;
            }
        }
        return null;
    }

    private String getLineName(int position) {
        if (position <= NUM_LINES) {
            switch (position) {
                case 0: return mContext.getString(R.string.l1);
                case 1: return mContext.getString(R.string.l2);
                case 2: return mContext.getString(R.string.l3);
                case 3: return mContext.getString(R.string.l45);
                case 4: return mContext.getString(R.string.schnitzelbar);
                case 5: return mContext.getString(R.string.update);
                case 6: return mContext.getString(R.string.abend);
                case 7: return mContext.getString(R.string.aktion);
                case 8: return mContext.getString(R.string.heisstheke);
                case 9: return mContext.getString(R.string.nmtisch);
            }
        }
        return null;
    }
}

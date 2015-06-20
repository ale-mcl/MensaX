package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.zip.Inflater;

import edu.kit.psegruppe3.mensax.datamodels.DailyMenu;
import edu.kit.psegruppe3.mensax.datamodels.Line;
import edu.kit.psegruppe3.mensax.datamodels.Offer;

/**
 * Created by ekremsenturk on 20.06.15.
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
        TextView textView = (TextView) convertView.findViewById(R.id.list_item_offer_textview);
        textView.setText(offer.getMeal().getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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

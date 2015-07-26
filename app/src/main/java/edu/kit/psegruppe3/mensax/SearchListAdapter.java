package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.kit.psegruppe3.mensax.data.CanteenContract;

/**
 * Cursor adapter for the results of a search query.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class SearchListAdapter extends CursorAdapter {

    /**
     * Constructor of the CursorAdapter
     * @param context the context of the class
     * @param c the cursor
     * @param flags flags of the adapter
     */
    public SearchListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView textView;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.list_item_search_textview);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_search, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String mealName = cursor.getString(cursor.getColumnIndex(CanteenContract.MealEntry.COLUMN_MEAL_NAME));
        viewHolder.textView.setText(mealName);
    }
}

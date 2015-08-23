package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.kit.psegruppe3.mensax.sync.MensaXSyncAdapter;

/**
 * The Main Activity of the app. Shows all offer of the week
 *
 * @author MensaX-group
 * @version 1
 */
public class MainActivity extends ActionBarActivity {

    /**
     * The number of tabs in the TagAdapter, represents the number of days shown.
     */
    private static final int NUM_TABS = 5;
    /**
     * The date of a day in the adapter.
     */
    static final String ARG_DATE = "date";

    private String mPriceGroup;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPriceGroup = Utility.getPreferredPriceGroup(this);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(tabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        MensaXSyncAdapter.initializeSyncAdapter(this);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        } else if (id == R.id.action_search) {
            onSearchRequested();
            return true;

        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);

        }else if (id == R.id.action_liveCam) {
            Intent intent = new Intent(this, LiveCamsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
        String priceGroup = Utility.getPreferredPriceGroup(this);
        if (priceGroup != null && !priceGroup.equals(mPriceGroup)) {
            List<Fragment> dmfList = getSupportFragmentManager().getFragments();
            for (int i = 0; i < dmfList.size(); i++) {
                DailyMenuFragment dmf = (DailyMenuFragment) dmfList.get(i);
                if (dmf != null) {
                    dmf.onPriceGroupChanged();
                }
            }
            mPriceGroup = priceGroup;
        }
    }

    /**
     * Class for the TabAdapter that shows the days of the week and the offers in them.
     */
    public static class TabAdapter extends FragmentPagerAdapter {

        private Context context;

        /**
         * The constructor of the class
         * @param fm the fragment manager of the activity
         * @param context the activity
         */
        public TabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (currentDay + position > Calendar.FRIDAY) {
                calendar.add(Calendar.DATE, position + 2);
            } else if (currentDay == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, position + 1);
            } else {
                calendar.add(Calendar.DATE, position);
            }

            Fragment f = new DailyMenuFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(ARG_DATE, calendar.getTimeInMillis());
            f.setArguments(bundle);
            return f;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount() {
            return NUM_TABS;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            String dayOfWeek = "";
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (currentDay + position > Calendar.FRIDAY) {
                calendar.add(Calendar.DATE, position + 2); //skip weekend
            } else if (currentDay == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, position + 1);
            } else {
                calendar.add(Calendar.DATE, position);
            }
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if (day == currentDay) {
                dayOfWeek = context.getString(R.string.today);
            } else if (day == currentDay + 1) {
                dayOfWeek = context.getString(R.string.tomorrow);
            } else {
                switch (day) {
                    case Calendar.MONDAY: dayOfWeek = context.getString(R.string.monday); break;
                    case Calendar.TUESDAY: dayOfWeek = context.getString(R.string.tuesday); break;
                    case Calendar.WEDNESDAY: dayOfWeek = context.getString(R.string.wednesday); break;
                    case Calendar.THURSDAY: dayOfWeek = context.getString(R.string.thursday); break;
                    case Calendar.FRIDAY: dayOfWeek = context.getString(R.string.friday);
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.dateFormat));
            Date currentDate = calendar.getTime();
            return dayOfWeek + " " + dateFormat.format(currentDate);
        }
    }

}

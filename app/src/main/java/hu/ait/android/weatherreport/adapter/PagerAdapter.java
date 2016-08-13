package hu.ait.android.weatherreport.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hu.ait.android.weatherreport.R;
import hu.ait.android.weatherreport.WeatherDetailsActivity;
import hu.ait.android.weatherreport.fragment.ShowDetailsFragment;
import hu.ait.android.weatherreport.fragment.ShowSummaryFragment;

/**
 * Created by nathanmajumder on 5/5/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    Context context;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return new ShowSummaryFragment();
            case 1:
                return new ShowDetailsFragment();
            default:
                return new ShowSummaryFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.summaryFragTitle);
            case 1:
                return context.getResources().getString(R.string.detailsFragTitle);
            default:
                return context.getResources().getString(R.string.unknownFragDetails);
        }
    }
}

package uit.money.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    private int[] layouts = new int[]{};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fragmentManager, int[] layouts) {
        super(fragmentManager);
        this.layouts = layouts;
    }

    public FragmentAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return Math.max(layouts.length, fragments.size());
    }

    @Override
    public Fragment getItem(int i) {
        if (layouts.length != 0) {
            return new FragmentPage(layouts[i]);
        } else if (fragments.size() != 0) {
            return fragments.get(i);
        } else return null;
    }

    public Fragment getFragment(int index) {
        if (fragments == null) return null;
        else return fragments.get(index);
    }
}

package com.nanostuffs.maimai.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nanostuffs.maimai.activity.ItemsTabActivity;
import com.nanostuffs.maimai.fragment.HighPriceFragment;
import com.nanostuffs.maimai.fragment.LowPriceFragment;
import com.nanostuffs.maimai.fragment.RecentFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	boolean flag;
	
	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		flag=false;
	}

	@Override
	public Fragment getItem(int index) {
		
		
		switch (index) {
		case 0:
			return new RecentFragment();
		case 1:
			return new HighPriceFragment();
		case 2:
			return new LowPriceFragment();
		}
		return null;

		
	}

	@Override
	public int getCount() {
		return 3;
	}

}

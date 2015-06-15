package com.nanostuffs.maimai.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nanostuffs.maimai.fragment.MeHighPriceFragment;
import com.nanostuffs.maimai.fragment.MeLowPriceFragment;
import com.nanostuffs.maimai.fragment.MeRecentFragment;

public class MeTabsPagerAdapter extends FragmentPagerAdapter {

	public MeTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new MeRecentFragment();
			
		case 1:
			return new MeHighPriceFragment();
			
		case 2:
			return new MeLowPriceFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}

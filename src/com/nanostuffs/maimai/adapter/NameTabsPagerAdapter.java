package com.nanostuffs.maimai.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nanostuffs.maimai.fragment.NameHighPriceFragment;
import com.nanostuffs.maimai.fragment.NameLowPriceFragment;
import com.nanostuffs.maimai.fragment.NameRecentFragment;

public class NameTabsPagerAdapter extends FragmentPagerAdapter {

	public NameTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new NameRecentFragment();
		case 1:
			return new NameHighPriceFragment();
		case 2:
			return new NameLowPriceFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}

package com.nanostuffs.maimai.model;

public class SearchItem {

	private String mItemId;
	private String mItemName;

	public String getItemId() {
		return mItemId;
	}

	public void setItemId(String mItemId) {
		this.mItemId = mItemId;
	}

	public String getItemName() {
		return mItemName;
	}

	public void setItemName(String mItemName) {
		this.mItemName = mItemName;
	}

	@Override
	public String toString() {
		return mItemName;
	}

}

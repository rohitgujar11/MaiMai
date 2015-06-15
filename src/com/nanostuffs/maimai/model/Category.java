package com.nanostuffs.maimai.model;

public class Category {
	private String mCategoryID;
	private String mCategoryImage;
	private String mCategoryName;
	private String mViewedCount;
	private String mFollowigViewedCount;
	private String mNearMeViewedCount;

	public String getFollowigViewedCount() {
		return mFollowigViewedCount;
	}

	public void setFollowigViewedCount(String mFollowigViewedCount) {
		this.mFollowigViewedCount = mFollowigViewedCount;
	}

	public String getNearMeViewedCount() {
		return mNearMeViewedCount;
	}

	public void setNearMeViewedCount(String mNearMeViewedCount) {
		this.mNearMeViewedCount = mNearMeViewedCount;
	}

	public String getCategoryID() {
		return mCategoryID;
	}

	public void setCategoryID(String mCategoryID) {
		this.mCategoryID = mCategoryID;
	}

	public String getCategoryImage() {
		return mCategoryImage;
	}

	public void setCategoryImage(String mCategoryImage) {
		this.mCategoryImage = mCategoryImage;
	}

	public String getCategoryName() {
		return mCategoryName;
	}

	public void setCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public String getViewedCount() {
		return mViewedCount;
	}

	public String getCategory() {
		return "Category";
	}

	public void setViewedCount(String mViewedCount) {
		this.mViewedCount = mViewedCount;
	}

	@Override
	public String toString() {
		return "Category [mCategoryID=" + mCategoryID + ", mCategoryImage="
				+ mCategoryImage + ", mCategoryName=" + mCategoryName
				+ ", mViewedCount=" + mViewedCount + ", mFollowigViewedCount="
				+ mFollowigViewedCount + ", mNearMeViewedCount="
				+ mNearMeViewedCount + "]";
	}

}

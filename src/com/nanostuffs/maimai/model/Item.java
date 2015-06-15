package com.nanostuffs.maimai.model;

public class Item {

	private String mItemId;
	private String mUserId;
	private String mItemName;
	private String mUserName;
	private String mItemImage;
	private String mItemViewedCount;
	private String mItemTotalLikes;
	private String mItemDateCreated;
	private String mLocation;
	private String mDays;
	private String mPrice;
	private String mDescription;
	private String mIsLike;
	private boolean mSelected;
	private String mUserPic;
	private String mVideo;
	private String mThumb;
	private String mFollow;

	public String getFollow() {
		return mFollow;
	}

	public void setFollow(String mFollow) {
		this.mFollow = mFollow;
	}

	public String getThumb() {
		return mThumb;
	}

	public void setThumb(String mThumb) {
		this.mThumb = mThumb;
	}

	public String getUserPic() {
		return mUserPic;
	}

	public void setUserPic(String mUserPic) {
		this.mUserPic = mUserPic;
	}

	public String getVideo() {
		return mVideo;
	}

	public void setVideo(String mVideo) {
		this.mVideo = mVideo;
	}

	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}

	public String getIsLike() {
		return mIsLike;
	}

	public void setIsLike(String mIsLike) {
		this.mIsLike = mIsLike;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getPrice() {
		return mPrice;
	}

	public void setPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String mLocation) {
		this.mLocation = mLocation;
	}

	public String getDays() {
		return mDays;
	}

	public void setDays(String mDays) {
		this.mDays = mDays;
	}

	public String getItemId() {
		return mItemId;
	}

	public void setItemId(String mItemId) {
		this.mItemId = mItemId;
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public String getItemName() {
		return mItemName;
	}

	public void setItemName(String mItemName) {
		this.mItemName = mItemName;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getItemImage() {
		return mItemImage;
	}

	public void setItemImage(String mItemImage) {
		this.mItemImage = mItemImage;
	}

	public String getItemViewedCount() {
		return mItemViewedCount;
	}

	public void setItemViewedCount(String mItemViewedCount) {
		this.mItemViewedCount = mItemViewedCount;
	}

	public String getItemTotalLikes() {
		return mItemTotalLikes;
	}

	public void setItemTotalLikes(String mItemTotalLikes) {
		this.mItemTotalLikes = mItemTotalLikes;
	}

	public String getItemDateCreated() {
		return mItemDateCreated;
	}

	public void setItemDateCreated(String mItemDateCreated) {
		this.mItemDateCreated = mItemDateCreated;
	}

	@Override
	public String toString() {
		return "Item [mItemId=" + mItemId + ", mUserId=" + mUserId
				+ ", mItemName=" + mItemName + ", mUserName=" + mUserName
				+ ", mItemImage=" + mItemImage + ", mItemViewedCount="
				+ mItemViewedCount + ", mItemTotalLikes=" + mItemTotalLikes
				+ ", mItemDateCreated=" + mItemDateCreated + ", mLocation="
				+ mLocation + ", mDays=" + mDays + ", mPrice=" + mPrice
				+ ", mDescription=" + mDescription + ", mIsLike=" + mIsLike
				+ ", mSelected=" + mSelected + ", mUserPic=" + mUserPic
				+ ", mVideo=" + mVideo + ", mThumb=" + mThumb + ", mFollow="
				+ mFollow + "]";
	}

}

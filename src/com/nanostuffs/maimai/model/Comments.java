package com.nanostuffs.maimai.model;

public class Comments {
	private String mItemId;
	private String mUserId;
	private String mUserName;
	private String mUserPic;
	private String mItemComment;
	private String mTimeAgo;

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

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getUserPic() {
		return mUserPic;
	}

	public void setUserPic(String mUserPic) {
		this.mUserPic = mUserPic;
	}

	public String getItemComment() {
		return mItemComment;
	}

	public void setItemComment(String mItemComment) {
		this.mItemComment = mItemComment;
	}

	public String getTimeAgo() {
		return mTimeAgo;
	}

	public void setTimeAgo(String mTimeAgo) {
		this.mTimeAgo = mTimeAgo;
	}

	@Override
	public String toString() {
		return "Comments [mItemId=" + mItemId + ", mUserId=" + mUserId
				+ ", mUserName=" + mUserName + ", mUserPic=" + mUserPic
				+ ", mItemComment=" + mItemComment + ", mTimeAgo=" + mTimeAgo
				+ "]";
	}

}

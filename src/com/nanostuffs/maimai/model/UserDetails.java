package com.nanostuffs.maimai.model;

public class UserDetails {
	private String mUid;
	private String mUsername;
	private String mPassword;
	private String mEmail;
	private String mImage;
	private String mAge;
	private String mMobile;
	private String mGender;
	private String mCountry;
	private String mRating;
	private String mLocation;
	private String mDateCreated;
	private String mReceiveNotification;

	@Override
	public String toString() {
		return "UserDetails [mUid=" + mUid + ", mUsername=" + mUsername
				+ ", mPassword=" + mPassword + ", mEmail=" + mEmail
				+ ", mImage=" + mImage + ", mAge=" + mAge + ", mMobile="
				+ mMobile + ", mGender=" + mGender + ", mCountry=" + mCountry
				+ ", mRating=" + mRating + ", mLocation=" + mLocation
				+ ", mDateCreated=" + mDateCreated + ", mReceiveNotification="
				+ mReceiveNotification + "]";
	}

	public String getReceiveNotification() {
		return mReceiveNotification;
	}

	public void setReceiveNotification(String mReceiveNotification) {
		this.mReceiveNotification = mReceiveNotification;
	}

	public String getUid() {
		return mUid;
	}

	public void setUid(String mUid) {
		this.mUid = mUid;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String mUsername) {
		this.mUsername = mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getImage() {
		return mImage;
	}

	public void setImage(String mImage) {
		this.mImage = mImage;
	}

	public String getAge() {
		return mAge;
	}

	public void setAge(String mAge) {
		this.mAge = mAge;
	}

	public String getMobile() {
		return mMobile;
	}

	public void setMobile(String mMobile) {
		this.mMobile = mMobile;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String mGender) {
		this.mGender = mGender;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String mCountry) {
		this.mCountry = mCountry;
	}

	public String getRating() {
		return mRating;
	}

	public void setRating(String mRating) {
		this.mRating = mRating;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String mLocation) {
		this.mLocation = mLocation;
	}

	public String getDateCreated() {
		return mDateCreated;
	}

	public void setDateCreated(String mDateCreated) {
		this.mDateCreated = mDateCreated;
	}

}

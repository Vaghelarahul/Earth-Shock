package com.batman.droidapps.shockearth;

public class ShockDataModel {

    private Double mLongitude;
    private Double mLatitude;
    private Double mDepth;

    private Double mMagnitude;
    private String mLocoation;
    private long mQuakeDate;
    private String mEarthequakeUrl;

    public ShockDataModel(Double mMagnitude, String mLocoation, long mQuakeDate, String mQuakeUrl) {
        this.mMagnitude = mMagnitude;
        this.mLocoation = mLocoation;
        this.mQuakeDate = mQuakeDate;
        this.mEarthequakeUrl = mQuakeUrl;
    }

    public Double getmLongitude() {
        if (mLongitude == null) return 0.0;
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Double getmLatitude() {
        if (mLatitude == null) return 0.0;
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getmDepth() {
        return mDepth;
    }

    public void setmDepth(Double mDepth) {
        this.mDepth = mDepth;
    }

    public Double getMagnitude() {
        return mMagnitude;
    }

    public String getLocoation() {
        return mLocoation;
    }

    public long getQuakeDate() {
        return mQuakeDate;
    }

    public String getQuakeUrl() {
        return mEarthequakeUrl;
    }
}

package com.tmate.user.data;

import java.util.Date;

public class PointData {
    private String poTime;
    private int poResult;
    private int mPoint;
    private String poExact;
    private String poCourse;

    public String getPoCourse() {
        return poCourse;
    }

    public void setPoCourse(String poCourse) {
        this.poCourse = poCourse;
    }

    public String getPoTime() {
        return poTime;
    }

    public void setPoTime(String poTime) {
        this.poTime = poTime;
    }

    public int getPoResult() {
        return poResult;
    }

    public void setPoResult(int poResult) {
        this.poResult = poResult;
    }

    public int getmPoint() {
        return mPoint;
    }

    public void setmPoint(int mPoint) {
        this.mPoint = mPoint;
    }

    public String getPoExact() {
        return poExact;
    }

    public void setPoExact(String poExpact) {
        this.poExact = poExpact;
    }
}

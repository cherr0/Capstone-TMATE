package com.tmate.user.data;

import java.sql.Timestamp;
import java.util.Date;

public class PointData {
//    private String poTime;
//    private int poResult;
//    private int mPoint;
//    private String poExact;
//    private String poCourse;
//
    private String po_course;

    private String po_exact;

    private int po_result;

    private Timestamp po_time;

    private String bd_id;

    // DB에는 없다. -> 총 포인트
    private int po_point;

    public int getPo_point() {
        return po_point;
    }

    public void setPo_point(int po_point) {
        this.po_point = po_point;
    }

    public String getPo_course() {
        return po_course;
    }

    public void setPo_course(String po_course) {
        this.po_course = po_course;
    }

    public String getPo_exact() {
        return po_exact;
    }

    public void setPo_exact(String po_exact) {
        this.po_exact = po_exact;
    }

    public int getPo_result() {
        return po_result;
    }

    public void setPo_result(int po_result) {
        this.po_result = po_result;
    }

    public Timestamp getPo_time() {
        return po_time;
    }

    public void setPo_time(Timestamp po_time) {
        this.po_time = po_time;
    }

    public String getBd_id() {
        return bd_id;
    }

    public void setBd_id(String bd_id) {
        this.bd_id = bd_id;
    }

    // 지울 것
//    public String getPoCourse() {
//        return poCourse;
//    }
//
//    public void setPoCourse(String poCourse) {
//        this.poCourse = poCourse;
//    }
//
//    public String getPoTime() {
//        return poTime;
//    }
//
//    public void setPoTime(String poTime) {
//        this.poTime = poTime;
//    }
//
//    public int getPoResult() {
//        return poResult;
//    }
//
//    public void setPoResult(int poResult) {
//        this.poResult = poResult;
//    }
//
//    public int getmPoint() {
//        return mPoint;
//    }
//
//    public void setmPoint(int mPoint) {
//        this.mPoint = mPoint;
//    }
//
//    public String getPoExact() {
//        return poExact;
//    }
//
//    public void setPoExact(String poExpact) {
//        this.poExact = poExpact;
//    }
}

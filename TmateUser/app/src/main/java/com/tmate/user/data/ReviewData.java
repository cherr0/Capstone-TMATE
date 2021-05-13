package com.tmate.user.data;

public class ReviewData {
    private String name;
    private int review_picture;
    private String r_to_seat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReview_picture() {
        return review_picture;
    }

    public void setR_to_seat(String r_to_seat) {
        this.r_to_seat = r_to_seat;
    }

    public void setReview_picture(int review_picture) {
        this.review_picture = review_picture;
    }

    public String getR_to_seat() {
        return  r_to_seat;
    }
}

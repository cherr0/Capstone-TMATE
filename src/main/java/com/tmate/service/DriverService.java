package com.tmate.service;

import com.tmate.domain.*;

import java.util.List;
import java.util.Map;

public interface DriverService {

    // 기사 리스트 페이지 네이션 - 데이터
    public List<JoinDriversVO> getListDriverPagingList(Criteria cri, String d_id);

    // 기사 리스트 페이지 네이션 - 개수
    public int countTotalDrivers();

    // 기사 상세페이지 -> 상세페이지 및 좋아요 , 싫어요 횟수 같이 보내기
    public Map<String, Object> getDriver(String d_id);

    // 상세페이지에 운행 내역 , 평점이력 페이지 네이션 처리 하기 위한 서비스 로직
    public int getCountHistory(String d_id);

    public int getCountReview(String d_id);

    public List<JoinHistoryVO> getHistoryList(Criteria cri, String d_id);

    public List<ReviewDTO> getReviewList(Criteria cri, String d_id);

    // 기사 블랙리스트 내역
    public List<JoinBanVO> getBanList(String d_id);

    
}

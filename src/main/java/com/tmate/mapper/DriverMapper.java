package com.tmate.mapper;

import com.tmate.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DriverMapper {

    // 기사 리스트
    public List<JoinDriversVO> getList(@Param("cri") Criteria cri);

    // 기사 좋아요
    public int getLikeCount(String d_id);

    // 기사 싫어요
    public int getDisLikeCount(String d_id);

    // 기사 밴
    public List<JoinBanVO> getBanListByd_id(String d_id);

    // 기사 상세 페이지
    public JoinDriverProfileVO getDriverByd_id(String d_id);

    // 운행 내역
    public List<JoinHistoryVO> getDriverHistoryList(@Param("cri") Criteria cri, String d_id);

    // 평점 이력
    public List<ReviewDTO> getDriverReviewList(@Param("cri") Criteria cri, String d_id);


    // 기사 토탈 개수는 있음

    // 총 운행 내역 개수
    public int getCountHistory(String d_id);

    // 총 평점 이력 개수
    public int getCountReview(String d_id);
}

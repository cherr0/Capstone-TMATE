package com.tmate.service;

import com.tmate.domain.*;
import com.tmate.mapper.DriverMapper;
import com.tmate.mapper.Membermapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class DriverServiceImpl implements DriverService {

    private final DriverMapper driverMapper;

    private final Membermapper membermapper;

    // 기사 관리 페이지 리스트
    @Override
    public List<JoinDriversVO> getListDriverPagingList(Criteria cri) {
        log.info("기사리스트 서비스 처리중...");
        return driverMapper.getList(cri);
    }

    // 기사 관리 페이지 네이션 위한 총 개수
    @Override
    public int countTotalDrivers() {
        log.info("승인 받은 기사의 개수...");
        return membermapper.countDrivers();
    }

    // 기사 관리 상세 페이지 -> 상세 정보, 좋아요, 싫어요 개수,운행 횟수
    @Override
    public JoinDriverProfileVO getDriver(String d_id) {
        log.info("기사 상세페이지 상세정보 서비스...");
        return driverMapper.getDriverByd_id(d_id);
    }

    @Override
    public int countLike(String d_id) {
        return driverMapper.getLikeCount(d_id);
    }

    @Override
    public int countDislike(String d_id) {
        return driverMapper.getDisLikeCount(d_id);
    }

    // 상세 정보 페이지 처리를 위한 - 개수 (이용내역, 평점이력)
    @Override
    public int getCountHistory(String d_id) {
        log.info("운행내역 총 개수");
        return driverMapper.getCountHistory(d_id);
    }

    @Override
    public int getCountReview(String d_id) {
        log.info("평점 총 개수");
        return driverMapper.getCountReview(d_id);
    }

    // 상세 정보 페이지 처리를 위한 - 데이터
    @Override
    public  HisotryDPageDTO getHistoryList(Criteria cri, String d_id) {
        log.info("상세정보 운행내역 서비스...");
        return new HisotryDPageDTO(driverMapper.getCountHistory(d_id),
                driverMapper.getDriverHistoryList(cri,d_id));
    }

    @Override
    public ReviewDPageDTO getReviewList(Criteria cri, String d_id) {
        log.info("상세정보 평점내역 서비스...");
        return new ReviewDPageDTO(driverMapper.getCountReview(d_id),
                driverMapper.getDriverReviewList(cri, d_id));
    }

    // 무한스크롤 -> 블랙리스트 by 기사
    @Override
    public List<JoinBanVO> getBanList(String d_id) {
        log.info("블랙리스트 기사 서비스...");
        return driverMapper.getBanListByd_id(d_id);
    }
}

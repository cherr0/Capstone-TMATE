package com.tmate.service;


import com.tmate.domain.HistoryDTO;
import com.tmate.domain.MonthlyUsersVO;
import com.tmate.domain.PlaceDTO;
import com.tmate.domain.UsersByAgeVO;
import com.tmate.mapper.Membermapper;
import com.tmate.mapper.StaticsMapper;
import com.tmate.mapper.UserMainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final Membermapper membermapper;

    private final StaticsMapper staticsMapper;

    private final UserMainMapper userMainMapper;

    // 전체 사용자 수
    @Override
    public int countMembers() {
        return membermapper.countMembers();
    }

    // 전체 기사 수
    @Override
    public int countDrivers() {
        return membermapper.countDrivers();
    }

    // 주간 사용자 수
    @Override
    public int countWeeklyUsers() {
        return staticsMapper.getWeeklyUsers();
    }

    // 월간 사용자 차트 js로 넘길 것
    @Override
    public List<MonthlyUsersVO> countMonthlyUsers() {
        return staticsMapper.getMonthlyUsers();
    }

    // 나이대 별 사용자 수 차트 JS로 넘길것
    @Override
    public List<UsersByAgeVO> countUsersByAge() {
        return staticsMapper.getUsersByAge();
    }

    // 핫플레이스 출발지 횟수에 따른 랭크
    @Override
    public List<PlaceDTO> rankHotplaceByStart() {
        return staticsMapper.getHotPlaceList();
    }

    // 탑승지 순위
    @Override
    public List<HistoryDTO> rankStartByCnt() {
        return staticsMapper.getStartRankList();
    }

    // 목적지 순위
    @Override
    public List<HistoryDTO> rankFinishByCnt() {
        return staticsMapper.getEndRankList();
    }

    // 미사용 포인트 조회
    @Override
    public int getUnused(String m_id) {
        return userMainMapper.getUnusedPoint(m_id);
    }
}

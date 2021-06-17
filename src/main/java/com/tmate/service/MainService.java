package com.tmate.service;


import com.tmate.domain.HistoryDTO;
import com.tmate.domain.MonthlyUsersVO;
import com.tmate.domain.PlaceDTO;
import com.tmate.domain.UsersByAgeVO;

import java.util.List;

public interface MainService {

    // T 메이트 유저 수
    public int countMembers();

    // T 메이트 기사 수
    public int countDrivers();

    // 주간 이용자 수
    public int countWeeklyUsers();

    // 월간 이용자 수
    public List<MonthlyUsersVO> countMonthlyUsers();

    // 연령별 사용자 수
    public List<UsersByAgeVO> countUsersByAge();

    // 장소별 핫플레이스 이용 순위
    public List<PlaceDTO> rankHotplaceByStart();

    // 탑승지 순위
    public List<HistoryDTO> rankStartByCnt();

    // 미사용 포인트 조회
    public int getUnused(String m_id);

}

package com.tmate.mapper;


import com.tmate.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StaticsMapper {

    // 주간 이용자 수
    int getWeeklyUsers();

    // 월별 사용자 수
    List<MonthlyUsersVO> getMonthlyUsers();

    // 연령대별 사용자 수
    List<UsersByAgeVO> getUsersByAge();

    // 핫플레이스 이용 횟수 순위
    List<PlaceDTO> getHotPlaceList();

    // 월간 포인트 - 사용
    List<ChartDTO> getCountUsePoint(String m_id);

    // 월간 포인트 - 적립
    List<ChartDTO> getCountGetPoint(String m_id);

    // 월간 이용내역 - 일반
    List<ChartDTO> getCountNormal(String m_id);

    // 월간 이용내역 - 동승
    List<ChartDTO> getCountTogether(String m_id);

    // 월간 결제이력 - 카드
    List<ChartDTO> getCountUseCard(String m_id);

    // 월간 결제 이력 - 현금
    List<ChartDTO> getCountUseCash(String m_id);

    // 탑승지 순위 리스트
    List<HistoryDTO> getStartRankList();

    // 목적지 순위 리스트
    List<HistoryDTO> getEndRankList();
}

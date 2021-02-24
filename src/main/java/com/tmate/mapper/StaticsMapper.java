package com.tmate.mapper;


import com.tmate.domain.MonthlyUsersVO;
import com.tmate.domain.PlaceDTO;
import com.tmate.domain.UsersByAgeVO;
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

}

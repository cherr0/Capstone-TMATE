package com.tmate.service.android.driver;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.DriverDTO;

import java.util.List;

public interface AppCallService {


    // 기사 상태 값 바꾸기
    Boolean modifyDriverStatus(DriverDTO driverDTO);

    // 기사 2km 이내 콜 정보 리스트 가져오기
    List<DispatchDTO> getCallList(double m_lat, double m_lng);

    // 기사가 콜을 수락 할 시
    /*
     *  @Transactional
     *  1. 기사코드를 그 호출 정보에 넣어준다.
     *  2. 이용정보의 상태를 바꿔준다. -> 3
     *  3. 기사의 실시간 위치를 넣어준다.
     *  4. 기사의 상태도 운행 중으로 바꿔준다.
     * */
    Boolean driverCallAgree(String dp_id, String d_id, double m_lat, double m_lng);

    // 기사의 위치 실시간 최신화
    Boolean modifyRealTimeDriverLocation(double m_lat, double m_lng, String d_id);

    // 손님 태웠을시 -> 1. 탑승 중  2. 운행 시작 시간
    DispatchDTO modifyDispatchBoarding(String d_id);

    // 탑승 완료시 -> 1. 탑승 완료 2. 운행 종료 시간
    Boolean modifyDispatchBoardingEnds(String dp_id);
}

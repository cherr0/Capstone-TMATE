package com.tmate.mapper;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.DriverDTO;
import com.tmate.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DispatchMapper {

    /*
    *  일반 호출시
    * */

    /*
    *  사용자 APP
    * */

    // 일반 호출 시 -> 배차 정보 생성 (O)
    int insertNormalMatch(DispatchDTO dispatchDTO);

    // 일반 호출 취소 시 -> 일반 호출 삭제 (O)
    int deleteNormalMatch(String dp_id);

    // 일반 호출 시 계속해서 기사코드를 찾는다.
    String selectDriver(String dp_id);

    // 호출 매칭시 상태 바꿔주면서 기사 값도 넣어준다. (O)
    int updateNormalMatchCall(@Param("dp_id") String dp_id, @Param("d_id") String d_id);

    // 이용 서비스 상태 변경 (O)
    int updateNormalMatchStatus(DispatchDTO dispatchDTO);

    // 이용정보 가져오기 (O)
    DispatchDTO getCurrentDispatchInfo(String m_id);

    // 이용정보 상세 가져오기 (O)
    DispatchDTO getDetailCurrentDispatchInfo(String dp_id);

    // 기사 위치 실시간으로 가져오기 (O)
    MemberDTO getRealTimeDriverLocation(String d_id);

    /*
    *  기사 APP
    * */

    // 기사의 위치 (O)
    int updateDriverLocation(@Param("m_lat") double m_lat, @Param("m_lng") double m_lng, @Param("d_id") String d_id);

    // 기사 상태 변경 (O)
    int updateDriverStatus(DriverDTO driverDTO);

    // 기사 호출 성공 시 기사 운행중으로 변경
    int updateDriverDriving(String d_id);

    // 기사 자기 위치에서 2KM 이내의 호출을 가져온다. (O)
    List<DispatchDTO> selectCallInfo(double m_lat, double m_lng);

    // 손님 태웠을시 -> 운행 시간, 탑승 중 (O)
    int updateDispatchBoarding(String dp_id);

    // 탑승완료시 -> 탑승 종료, 운행 종료 시간 (O)
    int updateDisptchBoardingEnds(String dp_id);

    /*
    * 동승 호출시
    * */

}

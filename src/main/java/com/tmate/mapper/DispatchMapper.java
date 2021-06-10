package com.tmate.mapper;

import com.tmate.domain.*;
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

    // 일반 호출 시 -> 배차 정보 생성 (O) + 참여 테이블 까지 같이 삽입
    int insertNormalMatch(DispatchDTO dispatchDTO);

    // 일반 호출 시 -> 참여 테이블 같이 생성 (일반 호출용) (O)
    int insertNormalAttend(DispatchDTO dispatchDTO);

    // 일반 호출 취소 시 -> 일반 호출 삭제 (O)
    int deleteNormalMatch(String dp_id);

    // 일반 호출 취소 시 -> 일반 참여 삭제(O)
    int deleteNormalAttend(String dp_id);

    // 일반 호출 시 계속해서 기사코드를 찾는다.
    String selectDriver(String dp_id);

    // 호출 매칭시 상태 바꿔주면서 기사 값도 넣어준다. (O)
    int updateNormalMatchCall(@Param("dp_id") String dp_id, @Param("d_id") String d_id);

    // 이용 서비스 상태 변경 (O)
    int updateNormalMatchStatus(DispatchDTO dispatchDTO);

    // 이용정보 가져오기 (O) -> 방만든 사람이거나 , 일반 호출 경우
    DispatchDTO getCurrentDispatchInfo(String m_id);

    // 동승 호출 시 -> 신청자나 동승자 정보
    DispatchDTO getCurrentPassengerDispatchInfo(String m_id);

    // 이용정보 상세 가져오기 (O)
    DispatchDTO getDetailCurrentDispatchInfo(String dp_id);

    // 기사 위치 실시간으로 가져오기 (O)
    MemberDTO getRealTimeDriverLocation(String d_id);

    // 기사 위치 실시간으로 가져오기 - 수정버전 (O)
    DispatchDTO getRealTimeDriverPosition(String dp_id);

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

    DispatchDTO getDestination(String dp_id);

    // 기사가 현재 운행중인 이용코드 가져오기
    String getDrivingDp_id(String d_id);

    // 탑승완료시 -> 탑승 종료, 운행 종료 시간 (O)
    int updateDisptchBoardingEnds(String dp_id);

    // 기사가 전화할 승객의 전화를 하기위해 승객 회원 코드를 가져온다. (O)
    DispatchDTO getUseDispatchM_id(String d_id);

    // 결제 미터기 화면시 미터기 넣고 입력을 누르면 all_fare 업데이트, 배차 상태 (O)
    int updateFareDuringPayment(
            @Param("dp_id") String dp_id,
            @Param("all_fare") int all_fare,
            @Param("dp_status") String dp_status);

    // 기사의 블랙리스트 선택 시 (O)
    int insertBlacklist(BanDTO banDTO);

    /*
    * 동승 호출시
    * */

    /*
    *  사용자 APP
    * */

    // 1. 출발지 800m, 목적지 가까운 순으로 리스트 뽑아오기 -> select (배차) (O) (O)
    List<DispatchDTO> selectNearMatchList(
            @Param("s_lat") double s_lat,
            @Param("s_lng") double s_lng,
            @Param("f_lat") double f_lat,
            @Param("f_lng") double f_lng);

    // 2. 맘에 드는거 없을시에 자기가 만든다. -> insert (배차, 참여) (O) (O)
    // 2.1. 배차 정보 (O) (O)
    int insertTogetherDispatch(DispatchDTO dispatchDTO);

    // 2.2 참여 정보 (O) (O)
    int insertTogehterAttend(AttendDTO attendDTO);

    // 배차 정보 삭제 --> 배차 삭제, 참여 삭제 (O) (O)
    int deleteTogetherDispatch(String dp_id);

    int deleteTogetherAttend(String dp_id);


    // 2. 동승 참가 버튼 (insert 참여) (O)
    int insertAttendApply(AttendDTO attendDTO);

    // 3. 동승 수락 및 거절 버튼 (update -> 참여) -> 거절, 삭제 (O)
    int updateAttendStatus(@Param("dp_id") String dp_id, @Param("m_id") String m_id);

    int deleteAttendInfo(@Param("dp_id") String dp_id, @Param("m_id") String m_id);

    // 4. 동승자 신청 리스트 (O) (O)
    List<AttendDTO> getApplyTogetherList(String dp_id);

    // 5. 동승자 신청 거절 수락 (O) (O)
    int rejectApply(@Param("dp_id") String dp_id, @Param("m_id") String m_id);

    // 올라간다.
    int plusCurPeople(String dp_id);

    int agreeApply(@Param("dp_id") String dp_id, @Param("m_id") String m_id);

    // 6.동승자 정보 (O) (O)
    List<AttendDTO> getPassengerList(String dp_id);

    // 7. 이미 참가된 승객들의 좌석 보여주기 (O) (O)
    List<AttendDTO> getJoinSeat(String dp_id);

    // 운행 완료 시 리뷰 데이터 업데이트
    int reviewUpdate(ReviewVO reviewVO);




    /*
     *  일반 일시 동승일시
     *  횟수 업데이트
     * */
    Boolean updateAddUseCnt(String m_id);

    Boolean updateAddTogetherCnt(String m_id);

    /*
     *  ------------------------
     *  지인 번호 가져오기
     *  ------------------------
     * */
    List<String> selectMyFriend(String m_id);
}

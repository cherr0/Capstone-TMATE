package com.tmate.mapper;

import com.tmate.domain.HistoryDTO;
import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.MemberDTO;
import com.tmate.domain.TogetherDTO;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.domain.user.TogetherRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface HistoryMapper {

    // 이용 내역 삭제 부분 merchant_uid 0 동승 , 1 일반
    int deleteHistoryM(@Param("merchant_uid") String merchant_uid, @Param("m_id") String m_id);

    int deleteTogetherM(@Param("merchant_uid") String merchant_uid, @Param("m_id") String m_id);

    // 거리 순으로 한다. -> xml 작성
    List<HistoryDTO> findTogetherList(@Param("slttd") double lttd, @Param("slngtd") double lngtd,
                                      @Param("flttd") double flttd, @Param("flngtd") double flngtd);


    // 상세 방 정보 가져오기 -> xml 작성
    HistoryDTO findMatchingDetail(@Param("merchant_uid") String merchant_uid, @Param("m_id") String m_id);

    // 일반 호출 삽입
    int insertNormalMatch(HistoryDTO historyDTO);

    /*
     * 일반 호출 취소 시 삭제가 된다.
     * */
    int deleteNormalMatch(String merchant_uid);

    /*
     *   호출 매칭시 상태 바꿔주면서 기사 값도 넣어준다. -> xml
     */
    int updateMatchStatus(@Param("merchant_uid") String merchant_uid, @Param("d_id") String d_id);

    //     <!-- 이용서비스 상태 변경  -->  xml
    int updateH_status(HistoryDTO historyDTO);

    // 기사의 위치 -> xml
    int updateDriverLocation(@Param("m_lttd") double m_lttd, @Param("m_lngtd") double m_lngtd, @Param("d_id") String d_id);

    // 기사 상태 변경 -> xml
    int updateDriverStatus(String d_id);

    // 매칭방 생성시 인서트 문 총 2개 필요함 이용내역 하나 넣고 그 다음 동승 넣고, 동승자가 들올때도 같다.

    // 2. 승인 버튼을 누를시
    // 1. 매칭 -> xml 작성
    int insertMatching(HistoryDTO historyDTO);

    // 2. 동승 -> xml 작성
    int insertTogether(TogetherDTO togetherDTO);


    /*
     * 동승 신청 관련 - approval 이용
     * */

    // 신청한 유저의 리스트 가져오기
    List<TogetherRequest> selectApplyerList(String merchant_uid);

    // 다른 유저가 신청을 했을 경우 --> approval --> xml
    int insertTogetherMatchingApplyer(ApprovalDTO approvalDTO);

    // 거절을 누를시 approval 지워진다.
    int deleteTogetherMatchingAPplyer(@Param("id") String id, @Param("merchant_uid") String merchant_uid);

    // 인원이 다 찼을 시 남은 신청자들은 자동으로 삭제된다. --> approval
    int deleteRemainderApplyer(String merchant_uid);

    // 방장이 방을 삭제하게 되면 이용내역 지워진다. Transaction 이용 history 삭제 -> together 삭제 -> xml
    int deleteMatchingH(String merchant_uid);

    int deleteMatchingT(String merchant_uid);

    // 사용자가 나가거나 매칭 거부 당하였을 시 -> xml
    int deleteHistoryPersonal(@Param("merchant_uid") String merchant_uid,@Param("m_id") String m_id);

    int deleteTogetherPersonal(@Param("merchant_uid") String merchant_uid,@Param("m_id") String m_id);





    // 수용 인원으로 인하여 현재 인원 수를 나타낸다. -> xml 작성
    int selectPassengerCnt(String merchant_uid);

    // 좌석 변경 -> xml 작성
    int updateChangeSeatNo(TogetherDTO togetherDTO);

    // 현재 매칭방 좌석
    List<TogetherDTO> selectCurrentSeatNums(String merchant_uid);

    // 기사와 매칭이 되었을 때 기사코드 매칭에 업데이트 한다. -> xml 작성
    int updateMatchingDriver(HistoryDTO historyDTO);

    //  탑승 중일시 운행 출발 시간 -> xml 작성
    int updateH_s_time(HistoryDTO historyDTO);

    // 탑승 완료일시 운행 종료 시간 -> xml 작성
    int updateH_e_time(HistoryDTO historyDTO);



    // 택시를 부른 후
    // 1. 탑승전 정보 화면 -> 그 기사의 실시간 위치, 나의 실시간 위치, 기사 프로필, 기사 이름, 기사 차종, 기사 차량 번호, 기사에게 전화나 문자할 수 있게 -> 홈 버튼 & 호출 취소 버튼이 있다.

    // 2. 이용 중일떄 다른 화면으로 갔을 시 이용 서비스 창에 카드뷰로 있는데 -> 매칭 상태 뱃지(배차 성공, 매칭 중, 출발지, 도착지, 차량정보 ) -> xml 작성
    JoinHistoryVO selectHistoryCard(String m_id);

    // 택시를 탔을 시 -> 택시 정보 현재 택시의 실시간 위치 -> 예상 시간-> 차량 정보, 신고하기 기능도 있고 , 택시기사를 바로 평가할 수 있다.

    // 승인 신청 수락시
    // 이용 내역
    HistoryDTO selectHistory(@Param("merchant_uid") String merchant_uid, @Param("m_id") String m_id);

    // 동승
    TogetherDTO selectTogether(@Param("merchant_uid") String merchant_uid, @Param("m_id") String m_id);

    // 동승 신청 수락시 생성 --- 이용
    int insertAgreeHistory(HistoryDTO historyDTO);

    // 동승 신청 수락시 생성 --- 동승
    int insertAgreeTogether(TogetherDTO togetherDTO);

    // 현재 인원 수 증가
    int currentCntPlus(String merchant_uid, String m_id);

    // 현재 인원 수 가져오기
    int selectCurrentCnt(String merchant_uid, String m_id);

    // 일반 호출 기사 찾는 부분 --> xml 작성
    String selectD_id(String merchant_uid);


    /*
     *  기사 APP 관련 - 호출
     * */
    // 기사 운행 시작 - 대기중
    int update_statusW(String d_id);

    // 기사 운행  - 휴식중
    int update_statusR(String d_id);

    // 기사 자기 위치에서 2KM 이내의 호출을 가져온다.
    List<HistoryDTO> selectCallInfo(double m_lttd, double m_lngtd);


}
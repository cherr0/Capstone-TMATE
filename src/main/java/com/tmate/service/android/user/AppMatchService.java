package com.tmate.service.android.user;

import com.tmate.domain.AttendDTO;
import com.tmate.domain.DispatchDTO;
import com.tmate.domain.MemberDTO;
import com.tmate.domain.ReviewVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AppMatchService {

    /*
    *  일반 호출 시
    * */

    // 1. 사용자 APP
    // 1.1 일반 호출 생성
    String registerNormalMatch(DispatchDTO dispatchDTO);

    // 1.2 일반 호출 CallWaiting에서 기사코드 찾는 서비스
    String getD_idDuringCall(String dp_id);

    // 1.3 일반 호출 시 뒤로가기 버튼 눌렀을 때 삭제 시킨다.
    Boolean removeNowCall(String dp_id);

    // 1.4 일반 호출 이용시 이용정보 가져오기
    DispatchDTO getCurrentCallInfo(String m_id);

    // 1.5 일반 호출 이용시 이용정보 클릭 후 상세정보 가져오기
    DispatchDTO getDetailCurrentCallInfo(String dp_id);

    // 1.6 일반 호출 이용 시 계속하여 기사 위치를 가져온 후 기사 위치를 맵에 그린다.
    DispatchDTO getCurrentDriverLocation(String dp_id);

    /*
    *  동승 호출 시
    * */

    // 1. 출발지 800m, 목적지 가까운 순으로 리스트 뽑아오기 (O)
    List<DispatchDTO> getNearMatchList(double s_lat, double s_lng, double f_lat, double f_lng);

    // 2. 맘에 드는거 없을시에 자기가 만든다.
    // @Transactional -> 배차정보, 참여정보 (O)
    String registerMatch(DispatchDTO dispatchDTO, AttendDTO attendDTO);

    // 3. 배차 정보 삭제 (O)
    // @Transactional -> 배차정보, 참여정보
    Boolean removeMatch(String dp_id);

    // 4. 동승 참가 버튼 (O)
    Boolean registerApplyButton(AttendDTO attendDTO);

    // 5. 동승 수락 및 거절 버튼
    // 5.1 동승 수락 버튼 (O)
    Boolean modifyAggreeMatching(String dp_id, String m_id);

    // 5.1 동승 거절 버튼 (O)
    Boolean modifyRejectMatching(String dp_id, String m_id);

    // 6. 동승자 신청 리스트 (O)
    List<AttendDTO> getApplyerList(String dp_id);

    // 7. 동승자 정보
    List<AttendDTO> getPassengerList(String dp_id);

    // 8. 이미 참가된 승객들의 좌석 보여주기
    List<AttendDTO> alreadyChoiceSeatNO(String dp_id);

    // 운행 완료 시 리뷰 업데이트
    Boolean attendReviewUpdate(ReviewVO reviewVO);

    // 동승시 방장이 호출하기 , 호출하기 눌렀을 시 방상태 변경
    Boolean modifyTogetherStatus(DispatchDTO dispatchDTO);

    /*
     * 이것은 동승 , 일반 이용 시 횟수 업데이트
     * */
    Boolean modifyCallCnt(String dp_id, int flag);
}

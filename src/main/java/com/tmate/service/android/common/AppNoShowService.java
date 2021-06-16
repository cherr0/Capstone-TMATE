package com.tmate.service.android.common;

public interface AppNoShowService {

    // 1. 방장이 동승자 노쇼 버튼 눌렀을시
    /*
     *  -------------------------------
     *  참여상태 3, 노쇼한 회원의 상태 ,
     *  노쇼 카운트 +1, 이용횟수 -1
     *  -------------------------------
     * */
    Boolean MasterClickNoShowBtn(String m_id, String dp_id);


    // 2. 기사가 승객 노쇼 버튼 눌렀을 시 -> 일반 호출
    /*
     *  --------------------------------
     *  참여 상태 3, 노쇼한 회원의 상태, 노쇼카운트 +1
     *  이용 횟수 -1, 배차 정보 6
     *  --------------------------------
     * */
    Boolean DriverClickNoShowBtn(String m_id, String dp_id);

    // 3. 회원 정보 가져오기
    String getMemberStatus(String m_id);

    // 4. 멤버 정상 상태로 돌리는 것
    Boolean modifyMemberNormalStatus(String m_id);
}

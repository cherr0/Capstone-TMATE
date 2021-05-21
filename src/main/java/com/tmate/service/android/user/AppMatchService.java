package com.tmate.service.android.user;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.MemberDTO;
import org.springframework.stereotype.Service;


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
    MemberDTO getCurrentDriverLocation(String d_id);

}

package com.tmate.service.android.user;

import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;

import java.util.List;
import java.util.Map;

public interface AppMemberService {

    // 회원가입 - register
    Boolean registerMember(Map<String,String> map);

    // 회원 정보 - 프로필 + 좋아요 + 싫어요 횟수
    MemberDTO getMemberProfile(String m_id);

    // 이용 내역 - 유저 이용 내역
    List<DispatchDTO> getMemberHistoryList(String m_id);

    // 이용 내역 삭제
    Boolean removeMemberHistory(String merchant_uid, String m_id);

    // 소셜 로그인 연동 -> 이때 멤버 권한 까지 같이 할것이다.
    Boolean registerSocialEmail(SocialDTO socialDTO);

    // 포인트 내역 가져오기
    List<PointDTO> getPointListByM_id(String m_id);

    // 미사용 포인트 조회
    Integer getunusedPoint(String m_id);

    // 메인 뷰 최신 공지 리스트 가져오기
    List<BoardDTO> getMainNoticeList();

    // 포인트 삽입
    Boolean registerPoint(PointDTO pointDTO);

    // 등급 가져오기
    Integer getUsingCount(String m_id);


    /*
     *  즐겨 찾기
     * */
    List<BookmarkDTO> getBookmarkListByM_id(String m_id);

    // 즐겨 찾기 삭제
    Boolean removeBookmark(String bm_name, String m_id);

    // 즐겨 찾기 추가
    Boolean insertBookmark(BookmarkDTO bookmarkDTO);

    /*
     * 운행 옵션 부분
     * */
    // 1. 운행 옵션 가져오기
    Dv_optionDTO getDv_optionByM_id(String m_id);

    // 2. 운행 옵션 상태 값들 계속 변경 하는 서비스
    Boolean modifyDv_optionByM_id(Dv_optionDTO dv_optionDTO);


    // 회원 정보 수정 - 프로필

    // 회원 정보 탈퇴 - 프로필

    // 카드 등록 -> 다른 서비스 이용

    // 카드 삭제 -> 다른 서비스 이용



    // 결제 정보 확인 시


    // 이용 서비스 화면
    // 1. 탑승전 정보 화면


    // 2. 탑승 화면



    // 평가하기 화면
    // 택시 기사 이름, 동승자의 평가




}

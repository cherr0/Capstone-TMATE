package com.tmate.service.android.user;

import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;

import java.util.List;
import java.util.Map;

public interface AppMemberService {

    // 회원가입 - register
    public Boolean registerMember(Map<String,String> map);

    // 회원 정보 - 프로필 + 좋아요 + 싫어요 횟수
    public MemberDTO getMemberProfile(String m_id);

    // 이용 내역 - 유저 이용 내역
    public List<JoinHistoryVO> getMemberHistoryList(String m_id);

    // 소셜 로그인 연동 -> 이때 멤버 권한 까지 같이 할것이다.
    public Boolean registerSocialEmail(SocialDTO socialDTO);

    // 포인트 내역 가져오기
    public List<PointDTO> getPointListByM_id(String m_id);

    // 포인트 삽입

    // 포인트 삽입 시 멤버 테이블 총 포인트 업데이트


    /*
     *  즐겨 찾기
     * */
    public List<BookmarkDTO> getBookmarkListByM_id(String m_id);

    // 즐겨 찾기 삭제
    public Boolean removeBookmark(int bm_id, String m_id);


    // 회원 정보 수정 - 프로필

    // 회원 정보 탈퇴 - 프로필

    // 카드 등록

    // 카드 삭제

    // 일반 호출 시

    // 동승 호출 시
    // 거리순으로 리스트 보여주기
    public List<HistoryDTO> getTogetherMatchingList(String slttd, String slngtd, String flttd, String flngtd);

    // 매칭 상세 정보
    public HistoryDTO getTogetherMatchingDetail(String merchant_uid);

    
    // 매칭 신청 시 - 승인시 동승내역에 저장 -> update
    
    // 매칭 거절 시 -> 불린값 던저주면서 false 던져줘서 매칭 실패 했다고 함

    // 결제 정보 확인 시


    // 이용 서비스 화면
    // 1. 탑승전 정보 화면

    // 2. 탑승 화면

    // 평가하기 화면
    // 택시 기사 이름, 동승자의 평가


    // 매칭시 상태 바꾸기 - 매칭 대기중

    // 매칭시 상태 바꾸기 - 탑승 대기중

    // 매칭시 상태 바꾸기 - 탑승 중

    // 매칭시 상태 바꾸기 - 탑승 완료

}

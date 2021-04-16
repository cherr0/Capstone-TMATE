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
    List<JoinHistoryVO> getMemberHistoryList(String m_id);

    // 소셜 로그인 연동 -> 이때 멤버 권한 까지 같이 할것이다.
    Boolean registerSocialEmail(SocialDTO socialDTO);

    // 포인트 내역 가져오기
    List<PointDTO> getPointListByM_id(String m_id);

    // 포인트 삽입

    // 포인트 삽입 시 멤버 테이블 총 포인트 업데이트


    /*
     *  즐겨 찾기
     * */
    List<BookmarkDTO> getBookmarkListByM_id(String m_id);

    // 즐겨 찾기 삭제
    Boolean removeBookmark(int bm_id, String m_id);


    // 회원 정보 수정 - 프로필

    // 회원 정보 탈퇴 - 프로필

    // 카드 등록 -> 다른 서비스 이용

    // 카드 삭제 -> 다른 서비스 이용

    // 일반 호출 시 -> 다음 주 예정

    // 동승 호출 시
    // 거리순으로 리스트 보여주기
    List<HistoryDTO> getTogetherMatchingList(String slttd, String slngtd, String flttd, String flngtd);

    // 매칭 상세 정보
    HistoryDTO getTogetherMatchingDetail(String merchant_uid);

    // 매칭 방 추가시 -> 동승 까지 들어간다.
    Boolean registerTogether(HistoryDTO historyDTO, TogetherDTO togetherDTO);

    // 카운트 세어야 한다. -> 계속 그 현재 방 확인하기 위해서
    int getRidingCnt(String merchant_uid);

    // 신청 내역 보기
    List<ApprovalDTO> getApplyerList(String merchant_uid);

    // 동승자 다이얼로그 정보 -> m_id 이용, UserMainMapper와 Membermapper 이용
    MemberDTO getCheckPassengerApplyInfo(String m_id);

    // 리스트 만든사람이 동승 취소를 해버릴시 -> Transaction 사용
    Boolean removeTogetherMatch(String merchant_uid);

    // 매칭 취소 당하거나 매칭을 나갈시
    Boolean cancelTogetherMatch(String merchant_uid, String m_id);

    // 매칭 신청 시 -> Approval
    Boolean applyTogetherMatching(ApprovalDTO approvalDTO);

    // 매칭 거절 시 -> 불린값 던저주면서 false 던져줘서 매칭 실패 했다고 함
    Boolean rejectNcancelTogetherMatching(ApprovalDTO approvalDTO);

    // 인원이 다 찼을 시 남은 신청자들은 자동으로 삭제된다.
    Boolean removeRemainderApplyers(String merchant_uid);

    // 결제 정보 확인 시


    // 이용 서비스 화면
    // 1. 탑승전 정보 화면


    // 2. 탑승 화면



    // 평가하기 화면
    // 택시 기사 이름, 동승자의 평가


    // 매칭시 상태 바꾸기 - 매칭 대기중, 매칭 완료 ,탑승 대기중, 탑승 중, 탑승완료
    Boolean modifyH_status(HistoryDTO historyDTO);


}

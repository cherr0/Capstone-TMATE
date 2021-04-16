package com.tmate.service.android.user;

import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppMemberServiceImpl implements AppMemberService {

    // 멤버 mapper 의존
    private final Membermapper membermapper;

    // userMainMapper 의존
    private final UserMainMapper userMainMapper;

    // joinMapper 의존
    private final JoinMapper joinMapper;

    // plceMapper 의존
    private final PlaceMapper placeMapper;

    // HistoryMapper 의존
    private final HistoryMapper historyMapper;

    //  회원가입 등록
    @Override
    public Boolean registerMember(Map<String,String> map) {

        log.info("App에서 회원가입 서비스 넘어오는 멤버 정보 : " + map);

        MemberDTO member = new MemberDTO();

        // 회원 코드
        member.setM_id(map.get("m_id"));
        
        // 회원 생년월일 포맷 변환
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
            Date date = dateFormat.parse(map.get("m_birth"));
            Timestamp timestamp = new Timestamp(date.getTime());
            member.setM_birth(timestamp);
        } catch (ParseException e) {
            System.out.println(e);
        }
        
        // 회원 이름
        member.setM_name(map.get("m_name"));

        member.setM_imei(map.get("m_imei"));
        /*
        * 
        * 회원 집주소, 회원 이메일 null 값 체크뒤 DTO에 저장
        * 
        * */
        if (map.get("m_house") != null) {
            member.setM_house(map.get("m_house"));
        }

        if (map.get("m_email") != null) {
            member.setM_email(map.get("m_email"));
        }
        
        // insertMember 실행
        return membermapper.insertMember(member) == 1;
    }


    // 프로필란에 회원정보 넣기
    @Transactional
    @Override
    public MemberDTO getMemberProfile(String m_id) {
        log.info("회원 정보 서비스 로직 처리중");

        MemberDTO member = membermapper.getMemberByM_id(m_id);

        int like = userMainMapper.getCountLike(m_id);
        int dislike = userMainMapper.getCountDisLike(m_id);

        member.setLike(like);
        member.setDislike(dislike);

        return member;
    }

    // 유저 이용내역 리스트
    @Override
    public List<JoinHistoryVO> getMemberHistoryList(String m_id) {
        log.info("회원 이용 내역 서비스 로직 처리중");


        return joinMapper.findHistoryToApp(m_id);
    }


    // 소셜 계정 연동 시
    @Transactional
    @Override
    public Boolean registerSocialEmail(SocialDTO socialDTO) {

        String m_id = socialDTO.getM_id();

        membermapper.insertSocialEmail(socialDTO);

        MemberRole memberRole = new MemberRole("USER",m_id);

        return membermapper.insertMemberRole(memberRole) == 1;
    }

    @Transactional
    @Override
    public List<PointDTO> getPointListByM_id(String m_id) {
        log.info("포인트 리스트 가져오기 서비스 처리중 ....");

        int mPoint = membermapper.findM_Point(m_id);
        List<PointDTO> pointList = membermapper.findPointListByM_id(m_id);
        pointList.forEach(i -> {
            i.setPo_point(mPoint);
        });

        return pointList;
    }


    /*
    *  즐겨 찾기 DTO
    * */

    @Override
    public List<BookmarkDTO> getBookmarkListByM_id(String m_id) {

        log.info("즐겨찾기 찾는 중 서비스 로직");

        return placeMapper.findBookmarkList(m_id);
    }

    @Override
    public Boolean removeBookmark(int bm_id, String m_id) {
        return placeMapper.deleteBookmark(bm_id, m_id) == 1;
    }


   /*
   * 동승 호출 시
   * */

    // 1. 거리 검색 순으로 나열 해주는 서비스
    @Override
    public List<HistoryDTO> getTogetherMatchingList(String slttd, String slngtd, String flttd, String flngtd) {
        log.info("자 출발지 500m 반경 목적지는 가까운 순으로 날립니다잉~");

        return historyMapper.findTogetherList(Double.valueOf(slttd),Double.valueOf(slngtd),Double.valueOf(flttd),Double.valueOf(flngtd));
    }

    // 2. 매칭 리스트 상세 내역
    @Override
    public HistoryDTO getTogetherMatchingDetail(String merchant_uid) {
        log.info("요청정보 상세 내역으로 한번 봅니다잉 ");
        return historyMapper.findMatchingDetail(merchant_uid);
    }


    // 동승 리스트 생성
    @Transactional
    @Override
    public Boolean registerTogether(HistoryDTO historyDTO, TogetherDTO togetherDTO) {
        log.info("AppMemberService 동승 리스트 생성 서비스중 ...");

        historyMapper.insertMatching(historyDTO);

        log.info("AppMemberService 매칭 리스트 삽입 성공! ");

        togetherDTO.setMerchant_uid(historyDTO.getMerchant_uid());

        return historyMapper.insertTogether(togetherDTO) == 1;
    }

    // 동승 자 수
    @Override
    public int getRidingCnt(String merchant_uid) {
        log.info("AppMemberService 동승 인원 수 서비스 중 ...");

        int cnt = historyMapper.selectPassengerCnt(merchant_uid);

        log.info("AppMemberService 동승 인원 수 : " + cnt);

        return cnt;
    }

    /*
    *  동승 매칭 다른 유저가 신청하는 부분
    * */
    // 방 리스트 장이 신청 내역을 본다.
    @Override
    public List<ApprovalDTO> getApplyerList(String merchant_uid) {
        log.info("AppMemberSerivce 방장이 신청내역을 봅니다.");

        List<ApprovalDTO> applyerList = historyMapper.selectApplyerList(merchant_uid);

        return applyerList;
    }

    // 매칭 신청 시
    @Override
    public Boolean applyTogetherMatching(ApprovalDTO approvalDTO) {
        log.info("AppMemberSerivce 동승 신청 서비스");

        return historyMapper.insertTogetherMatchingApplyer(approvalDTO) == 1;
    }

    // 신청 취소하거나 거절 당할시
    @Override
    public Boolean rejectNcancelTogetherMatching(ApprovalDTO approvalDTO) {
        log.info("AppMemberSerivce 거절이나 취소 서비스");

        return historyMapper.deleteTogetherMatchingAPplyer(approvalDTO) == 1;
    }

    // 인원이 다 찼을 시 남은 신청자들은 자동으로 삭제된다.
    @Override
    public Boolean removeRemainderApplyers(String merchant_uid) {
        log.info("AppMemberSerivce 남은 신청자들은 자동으로 삭제 서비스");

        return historyMapper.deleteRemainderApplyer(merchant_uid) == 1;
    }

    // 다이얼로그 동승 신청 회원 정보
    @Transactional
    @Override
    public MemberDTO getCheckPassengerApplyInfo(String m_id) {
        log.info("AppMemberService 동승 신청한 사람 정보 얻어오기 서비스 중");

        MemberDTO member = membermapper.getMemberByM_id(m_id);
        member.setLike(userMainMapper.getCountLike(m_id));
        member.setDislike(userMainMapper.getCountDisLike(m_id));

        log.info("AppMemberService 동승 신청한 사람 정보 : " +  member);

        return member;
    }

    // 매칭시 상태 바꾸기 - 매칭 대기중, 매칭 완료 ,탑승 대기중, 탑승 중, 탑승완료
    @Override
    public Boolean modifyH_status(HistoryDTO historyDTO) {
        log.info("AppMemberService 현재 매칭 상태 업데이트 서비스 ");


        return historyMapper.updateH_status(historyDTO) == 1;
    }

    // 삭제 하는 것
    // 리스트 만든사람이 동승 취소를 해버릴시 -> Transaction 사용
    @Transactional
    @Override
    public Boolean removeTogetherMatch(String merchant_uid) {

        log.info("AppMemberService 현재 방 삭제 서비스");

        // 동승 삭제
        historyMapper.deleteMatchingT(merchant_uid);

        // 매칭 삭제
        int i = historyMapper.deleteMatchingH(merchant_uid);

        return i == 1;
    }

    // 매칭 취소 당하거나 매칭을 나갈시
    @Transactional
    @Override
    public Boolean cancelTogetherMatch(String merchant_uid, String m_id) {

        log.info("AppMemberService 현재 취소 하거나 방 삭제 되는 것 서비스");

        // 동승 삭제
        historyMapper.deleteTogetherPersonal(merchant_uid, m_id);

        // 매칭 삭제
        int i = historyMapper.deleteHistoryPersonal(merchant_uid, m_id);

        return i == 1;
    }
}

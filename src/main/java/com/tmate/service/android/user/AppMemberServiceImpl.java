package com.tmate.service.android.user;

import com.tmate.domain.*;
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
}

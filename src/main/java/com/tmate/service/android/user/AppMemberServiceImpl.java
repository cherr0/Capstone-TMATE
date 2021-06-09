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

    // BoardMapper 의존
    private final BoardMapper boardMapper;

    //  회원가입 등록
    @Transactional
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
        member.setPassword(map.get("password"));

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

        membermapper.insertMember(member);

        // insertMember 실행
        return  membermapper.insertCallOption(map.get("m_id")) == 1 ;
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
    public List<DispatchDTO> getMemberHistoryList(String m_id) {
        log.info("회원 이용 내역 서비스 로직 처리중");


        return joinMapper.findHistoryToApp(m_id);
    }

    // 메인 뷰 최신 공지 리스트 가져오기
    @Override
    public List<BoardDTO> getMainNoticeList() {
        log.info("최신 공지 리스트 가져오는 중");
        return boardMapper.findMainNoticeList();
    }

    // 유저 이용내역 삭제
    @Transactional
    @Override
    public Boolean removeMemberHistory(String merchant_uid, String m_id) {

        if (merchant_uid.substring(27).equals("0")) {
            // 동승 일때
            historyMapper.deleteTogetherM(merchant_uid, m_id);

            return historyMapper.deleteHistoryM(merchant_uid, m_id) == 1;
        }
        else {
            // 일반 일때
            return historyMapper.deleteHistoryM(merchant_uid, m_id) == 1;
        }
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

    @Override
    public List<PointDTO> getPointListByM_id(String m_id) {
        log.info("포인트 리스트 가져오기 서비스 처리중 ....");

        List<PointDTO> pointList = membermapper.findPointListByM_id(m_id);

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
    public Boolean removeBookmark(String bm_name, String m_id) {
        return placeMapper.deleteBookmark(bm_name, m_id) == 1;
    }

    @Override
    public Boolean insertBookmark(BookmarkDTO bookmarkDTO) {
        log.info("즐겨찾기 추가 중 : " + bookmarkDTO);
        return placeMapper.insertBookmark(bookmarkDTO) == 1;
    }

    /*
    * 운행 옵션
    * */

    @Override
    public Dv_optionDTO getDv_optionByM_id(String m_id) {
        return membermapper.selectDv_optionByM_id(m_id);
    }

    @Override
    public Boolean modifyDv_optionByM_id(Dv_optionDTO dv_optionDTO) {
        return membermapper.updateDv_optionStatus(dv_optionDTO) == 1;
    }

    // 미사용 포인트 조회
    @Override
    public Integer getunusedPoint(String m_id) {
        int point = membermapper.unusedPointByM_id(m_id);
        log.info("사용자 미사용 포인트 : " + point);
        return point;
    }
}

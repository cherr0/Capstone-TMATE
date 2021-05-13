package com.tmate.mapper;

import com.tmate.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Membermapper {

    // 관리자 페이지 회원리스트
    List<MemberDTO> getList(Criteria cri);

    // 회원 상세 정보
    MemberDTO getMemberByM_id(String m_id);

    // 회원 가입
    int insertMember(MemberDTO memberDTO);

    // 회원 수정
    int updateMember(MemberDTO memberDTO);

    // 회원 삭제 -> 회원 및 기사 관련
    int deleteMember(String m_id);

    // 드라이버 삭제
    int deleteDriver(String d_id);

    // 승인 거절 시 차량 삭제
    int deleteCar(String d_id);

    // 승인 시 승인 날짜 변경
    int updateDate(String d_id);

    // 전체 유저 수
    int countMembers();

    // 전체 기사 수
    int countDrivers();

    // 멤버 수 토탈 카운트
    int getTotalCount(Criteria cri);

    // 유저 확인
    MemberDTO searchPermission(PhoneDTO phone);

    // App 소셜 로그인 연동 부분 관련 작업
    int insertSocialEmail(SocialDTO socialDTO);

    // App 소셜 로그인 시 멤버 권한 삽입 작업
    int insertMemberRole(MemberRole memberRole);

    // 소셜 연동 유저 조회
    MemberDTO findSocialMember(String email);

    // 권한 조회
    List<MemberRole> findRoleList(String m_id);

    // 유저 로그인 체크
    LoginVO loginCheck(String id, String password, String auth);

    /*
    * APP 포인트 내역
    * 포인트 내역
    * 총 포인트 가져오는 로직, 업데이트 하는 로직, 포인트 내역 리스트로 받아오는 것
    * */

    // 멤버 총요금 업데이트 시 값 변경 하기 위함
    int updateM_point(@Param("m_id") String m_id,@Param("fare") int fare);

    int insertPoint(PointDTO pointDTO);

    // 포인트 리스트 가져오기
    List<PointDTO> findPointListByM_id(String m_id);

    // 총 요금 가져오기
    int findM_Point(String m_id);

    /*
    * 운행 옵션 기능
    * */

    // 1. 운행 옵션 가져오기
    Dv_optionDTO selectDv_optionByM_id(String m_id);

    // 2. 운행 옵션 상태 변경
    int updateDv_optionStatus(Dv_optionDTO dv_optionDTO);



}

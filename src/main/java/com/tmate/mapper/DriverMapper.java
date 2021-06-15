package com.tmate.mapper;

import com.tmate.domain.*;
import com.tmate.domain.driver.AttendListVO;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;
import com.tmate.domain.driver.SidebarProfileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface DriverMapper {

    // 기사 리스트
    public List<JoinDriversVO> getList(@Param("cri") Criteria cri);

    // 기사 좋아요
    public int getLikeCount(String d_id);

    // 기사 싫어요
    public int getDisLikeCount(String d_id);

    // 기사 밴
    public List<JoinBanVO> getBanListByd_id(String d_id);

    // 기사 상세 페이지
    public JoinDriverProfileVO getDriverByd_id(String d_id);

    // 운행 내역
    public List<DriverHistoryVO> getDriverHistoryList(@Param("cri") Criteria cri, String d_id);

    // 평점 이력
    public List<ReviewDTO> getDriverReviewList(@Param("cri") Criteria cri, String d_id);


    // 기사 토탈 개수는 있음

    // 총 운행 내역 개수
    public int getCountHistory(String d_id);

    // 총 평점 이력 개수
    public int getCountReview(String d_id);

    /* ----------------
        기사앱 관련 매핑
       ---------------- */

    // 회원가입 - 멤버 테이블 삽입
    public int registerDriver(MemberDTO memberDTO);

    // 회원가입 - 기사 테이블 삽입
    public int addDriverLicense(DriverDTO driverDTO);

    // 회원가입 - 기사 법인 구분 삽입
    public int addDriverCorp(CorpDTO corpDTO);

    // 기사 운행이력
    public List<DriverHistoryVO> getAppDriverHistoryList(String d_id);

    // 기사 프로필 정보
    public DriverProfileVO getDriverDetail(String d_id);

    // 기사 이메일 변경
    public int emailModify(MemberDTO memberDTO);

    // 기사 차량 정보
    public List<CarDTO> getCarList(String d_id);

    // 기사 차량 선택
    public int updateDriverCar(String d_id, String car_no);

    // 기사 차량 추가
    public int insertCar(CarDTO carDTO);

    // 기사 차량 제거
    public int deleteCar(String car_no);

    // 블랙리스트에 표시할 데이터 리스트
    public List<AttendListVO> getAttendList(String dp_id);

    // 블랙리스트 추가
    public int insertBlacklist(BanDTO banDTO);

    // 블랙리스트 제거
    public int deleteBlacklist(String d_id, String m_id);

    // 기사 상태 바꾸기
    public int setDriverStatus(String d_id, int d_status);

    // 기사 승인 상태 확인
    public DriverDTO findDriverDateById(String d_id);

    // 기사 사이드바 프로필
    public SidebarProfileVO findSidebarProfileById(String d_id);

    // 기사 운행 완료 데이터
    public DispatchDTO findDriveFinishById(String dp_id);
}

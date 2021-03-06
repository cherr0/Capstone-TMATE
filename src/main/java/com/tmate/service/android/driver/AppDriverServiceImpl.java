package com.tmate.service.android.driver;

import com.tmate.domain.*;
import com.tmate.domain.driver.AttendListVO;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;
import com.tmate.domain.driver.SidebarProfileVO;
import com.tmate.mapper.DriverMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@Service
public class AppDriverServiceImpl implements AppDriverService{

    private final DriverMapper driverMapper;

    @Transactional
    @Override   // 기사용 어플 회원가입 - 회원, 기사 테이블
    public Boolean saveDriverProfile(Map<String, String> map) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setM_id(map.get("m_id"));
        memberDTO.setM_name(map.get("m_name"));
        memberDTO.setM_imei(map.get("m_imei"));
        memberDTO.setPassword(map.get("password"));
        memberDTO.setM_email(map.get("m_email"));
        // 회원 생년월일 포맷 변환
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = dateFormat.parse(map.get("m_birth"));
            Timestamp timestamp = new Timestamp(date.getTime());
            memberDTO.setM_birth(timestamp);
        } catch (ParseException e) {
            System.out.println(e);
        }

        log.info("AppDriverService 기사 회원가입 멤버 데이터 : " + memberDTO);
        driverMapper.registerDriver(memberDTO);

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setD_id(memberDTO.getM_id());
        driverDTO.setD_license_no(map.get("d_license_no"));
        driverDTO.setBank_company(map.get("bank_company"));
        driverDTO.setD_acnum(map.get("d_acnum"));

        log.info("AppDriverService 기사 회원가입 기사 데이터 : " + driverDTO);
        driverMapper.addDriverLicense(driverDTO);

        CarDTO carDTO = new CarDTO();
        carDTO.setM_id(driverDTO.getD_id());
        carDTO.setCar_no(map.get("car_no"));
        carDTO.setCar_color(map.get("car_color"));
        carDTO.setCar_kind(map.get("car_kind"));
        carDTO.setCar_model(map.get("car_model"));
        driverMapper.insertCar(carDTO);

        CorpDTO corpDTO = new CorpDTO();
        corpDTO.setCorp_code(map.get("corp_code"));
        corpDTO.setCorp_company(map.get("corp_company"));
        corpDTO.setD_id(driverDTO.getD_id());
        corpDTO.setCar_no(carDTO.getCar_no());

        return driverMapper.addDriverCorp(corpDTO) == 1;
    }

    @Override // 기사 승인 상태 확인
    public Boolean searchApprove(String d_id) {
        Timestamp result = driverMapper.findDriverDateById(d_id).getD_j_date();
        log.info("AppDriverService 기사 승인 인증 d_id : " + d_id);
        log.info("AppDriverService 기사 승인 인증 시간 : " + result);
        return result != null;
    }
    
    @Override   // 기사 운행이력
    public List<DriverHistoryVO> historyList(String d_id) {
        log.info("AppDriverService 기사 운행이력 d_id : " + d_id);
        return driverMapper.getAppDriverHistoryList(d_id);
    }

    @Override
    public SidebarProfileVO getSidebarProfileById(String d_id) {
        log.info("AppDriverService 기사 사이드바 프로필 가져오기 : " + d_id);
        return driverMapper.findSidebarProfileById(d_id);
    }

    @Override   // 기사 프로필
    public DriverProfileVO getDriverProfile(String d_id) {
        log.info("AppDriverService 기사 프로필 가져오기 d_id : " + d_id);
        return driverMapper.getDriverDetail(d_id);
    }

    @Override   // 기사 이메일 수정
    public Boolean modifyDriverEmail(MemberDTO memberDTO) {
        log.info("AppDriverService 기사 이메일 수정 memberDTO :" + memberDTO);
        return driverMapper.emailModify(memberDTO) == 1;
    }

    @Override   // 기사 차량 정보
    public List<CarDTO> getDriverCarList(String d_id) {
        log.info("AppDriverService 기사 차량 정보 d_id : " + d_id);
        return driverMapper.getCarList(d_id);
    }

    @Override // 기사 차량 선택
    public Boolean selectDriverCar(String d_id, String car_no) {
        log.info("AppDriverService 기사 차량 선택 car_no : " + car_no);
        return driverMapper.updateDriverCar(d_id, car_no) == 1;
    }

    @Override   // 기사 차량 추가
    public Boolean insertDriverCar(CarDTO carDTO) {
        log.info("AppDriverService 기사 차량 추가 carDTO : " + carDTO);
        return driverMapper.insertCar(carDTO) == 1;
    }

    @Override   // 기사 차량 제거
    public Boolean deleteDriverCar(String car_no) {
        log.info("AppDriverService 기사 차량 제거 car_no : " + car_no);
        return driverMapper.deleteCar(car_no) == 1;
    }

    @Override   // 블랙리스트에 표시할 데이터 리스트
    public List<AttendListVO> searchAttendList(String dp_id) {
        List<AttendListVO> attendList = driverMapper.getAttendList(dp_id);
        log.info("AppDriverService 배차 코드에 따른 승객 리스트 : " + attendList);
        return attendList;
    }

    @Override   // 블랙리스트 추가
    public Boolean blacklistRegister(BanDTO banDTO) {
        log.info("AppDriverService 블랙리스트 추가 : " + banDTO);
        return driverMapper.insertBlacklist(banDTO) == 1;
    }

    @Override   // 블랙리스트 제거
    public Boolean blacklistRemove(String d_id, String m_id) {
        log.info("AppDriverService 블랙리스트 제거(기사,회원) : " + d_id + " & " + m_id);
        return driverMapper.deleteBlacklist(d_id,m_id) == 1;
    }

    @Override   // 기사 상태 변경
    public Boolean driverModStatus(String d_id, int d_status) {
        log.info("AppDriverService 기사 상태 변경 : " + d_id + " status : " + d_status);
        return driverMapper.setDriverStatus(d_id, d_status) == 1;
    }

    @Override
    public DispatchDTO getDriveFinish(String dp_id) {
        DispatchDTO driveFinishById = driverMapper.findDriveFinishById(dp_id);
        log.info("AppDriverService 기사 운행 완료 데이터 : " + driveFinishById);
        return driveFinishById;
    }
}

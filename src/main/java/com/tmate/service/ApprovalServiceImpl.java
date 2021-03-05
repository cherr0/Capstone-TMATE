package com.tmate.service;

import com.tmate.domain.Criteria;
import com.tmate.domain.JoinApprovalVO;
import com.tmate.domain.JoinDriverVO;
import com.tmate.mapper.JoinMapper;
import com.tmate.mapper.Membermapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final Membermapper membermapper;

    private final JoinMapper joinMapper;

    // 승인 대기중인 기사 리스트
    @Override
    public List<JoinApprovalVO> getNoneApprovalList(Criteria cri) {

        List<JoinApprovalVO> drivers = joinMapper.getApprovalDrivers(cri);
        return drivers;
    }

    // 승인 대기중인 기사 리스트 중 하나 눌렀을 때 그 기사의 정보가 뜬다.
    @Override
    public JoinDriverVO getDrivers(String d_id) {

        JoinDriverVO driver = joinMapper.getDriver(d_id);
        return driver;
    }

    // 관리자가 택시 승인을 하였을 시
    @Override
    public int allowApproval(String d_id) {
        int check = membermapper.updateDate(d_id);
        return check;
    }


    // 거절 눌렀을 시 삭제 -> 기사만 지우고 추가정보는 나중에 재 작성
    @Transactional
    @Override
    public int removeDriver(String d_id) {
        membermapper.deleteCar(d_id); // 차량 지우고
        int check = membermapper.deleteDriver(d_id);
        return check;
    }

    // 페이지 네이션 처리용 카운트 - 승인 대기중인 기사 수
    @Override
    public int getTotalApproCount(Criteria cri) {
        return joinMapper.getTotalApproCount(cri);
    }
}

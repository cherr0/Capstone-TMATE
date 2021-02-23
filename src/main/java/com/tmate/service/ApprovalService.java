package com.tmate.service;

import com.tmate.domain.JoinApprovalVO;
import com.tmate.domain.JoinDriverVO;

import java.util.List;

public interface ApprovalService {


    // 승인대기중인 기사들 리스트
    public List<JoinApprovalVO> getNoneApprovalList();

    // 승인 모달창 눌렀을때 기사 정보 뜰수 있게
    public JoinDriverVO getDrivers(String d_id);

    // 관리자가 승인을 눌렀을 때
    public int AllowApproval(String d_id);

    // 거절 눌렀을 시 삭제
    public int removeDriver(String d_id);

}

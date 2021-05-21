package com.tmate.service.android.user;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.MemberDTO;
import com.tmate.mapper.DispatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppMatchServiceImpl implements AppMatchService {

    private final DispatchMapper dispatchMapper;

    @Override
    public String registerNormalMatch(DispatchDTO dispatchDTO) {
        String dp_id = "";

        if(dispatchMapper.insertNormalMatch(dispatchDTO) == 1)
            dp_id = dispatchDTO.getDp_id();

        return dp_id;
    }

    @Override
    public String getD_idDuringCall(String dp_id) {
        return dispatchMapper.selectDriver(dp_id);
    }

    @Override
    public Boolean removeNowCall(String dp_id) {
        return dispatchMapper.deleteNormalMatch(dp_id) == 1;
    }

    @Override
    public DispatchDTO getCurrentCallInfo(String m_id) {
        return dispatchMapper.getCurrentDispatchInfo(m_id);
    }

    @Override
    public DispatchDTO getDetailCurrentCallInfo(String dp_id) {
        return dispatchMapper.getDetailCurrentDispatchInfo(dp_id);
    }

    @Override
    public MemberDTO getCurrentDriverLocation(String d_id) {
        return dispatchMapper.getRealTimeDriverLocation(d_id);
    }
}

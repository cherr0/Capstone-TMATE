package com.tmate.domain;

import com.tmate.domain.driver.DriverHistoryVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class HisotryDPageDTO {

    private int historyCnt;
    private List<DriverHistoryVO> list;
}

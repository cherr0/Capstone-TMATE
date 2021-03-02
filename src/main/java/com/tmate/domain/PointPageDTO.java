package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class PointPageDTO {

    private int pointCnt;
    private List<JoinPointVO> list;

}

package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class HistoryPageDTO {

    private int historyCnt;
    private List<DispatchDTO> list;
}

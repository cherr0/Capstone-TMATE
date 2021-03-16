package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class ChartDTO {

    private String standard;

    private int count;
}

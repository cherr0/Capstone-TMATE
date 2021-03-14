package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class ReviewDPageDTO {
    private int reviewCnt;
    private List<ReviewDTO> list;
}

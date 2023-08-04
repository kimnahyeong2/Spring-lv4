package com.sparta.springlv4.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class BoardSearchCond {
    private String title;
    private String contents;

    @QueryProjection
    public BoardSearchCond(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}

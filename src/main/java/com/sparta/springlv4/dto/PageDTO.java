package com.sparta.springlv4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Builder
@AllArgsConstructor
public class PageDTO {

    private final Integer size = 10;
    private final Integer currentPage = 1;
    private String sortBy;

/*    public PageDTO(Integer size, Integer currentPage) {
        this.size = 10;
        this.currentPage = 1;
    }*/

    public Pageable toPageable() {
        if (Objects.isNull(sortBy)) {
            return PageRequest.of(currentPage - 1, size);
        } else {
            return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
        }
    }

    public Pageable toPageable(String sortBy) {
        return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
    }
}
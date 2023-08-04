package com.sparta.springlv4.repository;

import com.sparta.springlv4.dto.BoardSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryQuery {
    Page<BoardSearchCond> search(String searchWorld, Pageable pageable);
}

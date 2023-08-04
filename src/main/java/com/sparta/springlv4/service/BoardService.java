package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.BoardRequestDto;
import com.sparta.springlv4.dto.BoardResponseDto;
import com.sparta.springlv4.dto.BoardSearchCond;
import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    // 게시물 전체 목록 조회
    List<BoardResponseDto.BoardReadResponseDto> getBoards();

    // 게시물 작성
    BoardResponseDto.BoardBasicResponseDto createBoard(BoardRequestDto requestDto, User user);

    //게시물 선택 조회
    BoardResponseDto.BoardReadResponseDto getSelectBoards(Long id);

    // 게시물 수정
    BoardResponseDto.BoardReadResponseDto updateBoard(Board board, BoardRequestDto requestDto, User user);

    // 게시물 삭제
    void deleteBoard(Board board, User user);

    // 게시물 유무 확인
    Board findBoard(Long id);

    // 게시물 검색
    Page<BoardSearchCond> getSearchList(String searchWorld, Pageable pageable);
}
package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.BoardRequestDto;
import com.sparta.springlv4.dto.BoardResponseDto;
import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.repository.BoardRepository;
import com.sparta.springlv4.repository.LikeRepository;
import com.sparta.springlv4.error.MessageDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
}
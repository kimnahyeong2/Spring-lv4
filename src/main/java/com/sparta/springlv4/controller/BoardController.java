package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.BoardRequestDto;
import com.sparta.springlv4.dto.BoardResponseDto;
import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.error.MessageDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.BoardService;
import com.sparta.springlv4.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor

public class BoardController {
    private final BoardService boardService;
    private final LikeService likeService;

    @GetMapping("/feed")
    public ResponseEntity<List<BoardResponseDto.BoardReadResponseDto>> getBoards() {
        List<BoardResponseDto.BoardReadResponseDto> result = boardService.getBoards();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/feed")
    public ResponseEntity<BoardResponseDto.BoardBasicResponseDto> createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto.BoardBasicResponseDto result = boardService.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/feed/{id}")
    public ResponseEntity<BoardResponseDto.BoardReadResponseDto> getSelectBoards(@PathVariable Long id) {
        BoardResponseDto.BoardReadResponseDto result = boardService.getSelectBoards(id);
        return ResponseEntity.ok().body(result);
    }


    @PutMapping("/feed/{id}")
    public ResponseEntity<MessageDto> updateBoard(
            @PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardService.findBoard(id);
        BoardResponseDto.BoardReadResponseDto result = boardService.updateBoard(board, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/feed/{id}")
    public ResponseEntity<MessageDto> deleteBoard(
            @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardService.findBoard(id);
        boardService.deleteBoard(board, userDetails.getUser());
        return ResponseEntity.ok().body(new MessageDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/feed/like/{id}")
    public ResponseEntity<MessageDto> likeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likeBoardOrComment(id, userDetails.getUser(), 0L);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageDto("게시글 좋아요 성공", HttpStatus.ACCEPTED.value()));
    }

    @DeleteMapping("/feed/like/{id}")
    public ResponseEntity<MessageDto> deleteLikeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.deleteLikeBoardOrComment(id, userDetails.getUser(), 0L);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageDto("게시글 좋아요 취소 성공", HttpStatus.ACCEPTED.value()));
    }
}

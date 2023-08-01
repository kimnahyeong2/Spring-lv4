package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.BoardRequestDto;
import com.sparta.springlv4.dto.BoardResponseDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.BoardService;
import com.sparta.springlv4.service.LikeService;
import com.sparta.springlv4.status.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<BoardResponseDto.BoardReadResponseDto> getBoards(){
        return boardService.getBoards();
    }

    @PostMapping("/feed")
    public BoardResponseDto.BoardBasicResponseDto createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.createBoard(requestDto, userDetails.getUser());
    }

    @GetMapping("/feed/{id}")
    public BoardResponseDto.BoardReadResponseDto getSelectBoards(@PathVariable Long id){
        return boardService.getSelectBoards(id);
    }


    @PutMapping("/feed/{id}")
    public BoardResponseDto.BoardReadResponseDto updateBoard(
            @PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/feed/{id}")
    public ResponseEntity<Message> deleteMemo(
            @PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return boardService.deleteBoard(id, requestDto, userDetails.getUser());
    }

    @PostMapping("/feed/like/{id}")
    public ResponseEntity<Message> likeBoard (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.likeBoardOrComment(id, userDetails.getUser(), 0L);
    }

    @DeleteMapping("/feed/like/{id}")
    public ResponseEntity<Message> deleteLikeBoard (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.deleteLikeBoardOrComment(id, userDetails.getUser(), 0L);
    }
}

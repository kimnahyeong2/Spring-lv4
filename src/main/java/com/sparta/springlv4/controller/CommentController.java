package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.error.MessageDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.CommentService;
import com.sparta.springlv4.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping("/comment/{boardId}")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto result = commentService.addComment(boardId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<MessageDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment comment = commentService.findComment(commentId);
        CommentResponseDto result = commentService.updateComment(comment, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<MessageDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment comment = commentService.findComment(commentId);
        commentService.deleteComment(comment, userDetails.getUser());
        return ResponseEntity.ok().body(new MessageDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/comment/like/{id}")
    public ResponseEntity<MessageDto> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likeBoardOrComment(id, userDetails.getUser(), 1L);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageDto("댓글 좋아요 성공", HttpStatus.ACCEPTED.value()));
    }

    @DeleteMapping("/comment/like/{id}")
    public ResponseEntity<MessageDto> deleteLikeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.deleteLikeBoardOrComment(id, userDetails.getUser(), 1L);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageDto("댓글 좋아요 취소 성공", HttpStatus.ACCEPTED.value()));
    }
}

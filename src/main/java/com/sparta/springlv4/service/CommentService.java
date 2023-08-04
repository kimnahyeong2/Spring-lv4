package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.User;

public interface CommentService {
    // 댓글 추가
    CommentResponseDto addComment(Long boardId, CommentRequestDto requestDto, User user);

    // 댓글 수정
    CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user);

    // 댓글 삭제
    void deleteComment(Comment comment, User user);

    // 댓글 유무 확인
    Comment findComment(Long commentId);

}

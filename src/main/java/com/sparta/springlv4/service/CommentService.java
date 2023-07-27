package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.entity.*;
import com.sparta.springlv4.repository.BoardRepository;
import com.sparta.springlv4.repository.CommentRepository;
import com.sparta.springlv4.repository.LikeRepository;
import com.sparta.springlv4.status.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final MessageSource messageSource;


    @Transactional
    public CommentResponseDto addComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NullPointerException(messageSource.getMessage(
                        "not.exist.post",
                        null,
                        "해당 게시물이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );
        Comment comment = commentRepository.save(new Comment(requestDto, user, board));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        if(!confirmUser(comment, user)){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "not.your.post",
                    null,
                    "작성자만 수정이 가능합니다",
                    Locale.getDefault()
            ));
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity<Message> deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        if(!confirmUser(comment, user)){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "not.your.post",
                    null,
                    "작성자만 삭제가 가능합니다",
                    Locale.getDefault()
            ));
        }

        commentRepository.delete(comment);

        String msg ="삭제 완료";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException(messageSource.getMessage(
                        "not.exist.comment",
                        null,
                        "해당 댓글이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );
    }

    private boolean confirmUser(Comment comment, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        return userRoleEnum != UserRoleEnum.USER || Objects.equals(comment.getUser().getId(), user.getId());
    }
    @Transactional
    public ResponseEntity<Message> likeComment(Long id, User user) {
        Comment comment = findComment(id);
        if(likeRepository.findByUserAndComment(user, comment).isPresent()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.like",
                    null,
                    "이미 좋아요 되어 있습니다",
                    Locale.getDefault()
            ));
        }
        Like like = likeRepository.save(new Like(user, id, 2L));
        comment.increseLikesCount();
        String msg ="좋아요 완료";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Message> deleteLikeComment(Long id, User user) {
        Comment comment = findComment(id);
        if(likeRepository.findByUserAndComment(user, comment).isEmpty()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.delete.like",
                    null,
                    "이미 좋아요 되어 있지 않습니다",
                    Locale.getDefault()
            ));
        }
        Optional<Like> like = likeRepository.findByUserAndComment(user, comment);
        likeRepository.delete(like.get());
        comment.decreseLikesCount();
        String msg ="좋아요 취소";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
}

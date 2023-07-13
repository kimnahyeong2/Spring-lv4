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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public CommentResponseDto addComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.save(new Comment(requestDto, user, board));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        if(!confirmUser(comment, user)){
            Message message = new Message();
            message.setMessage("작성자만 수정할 수 있습니다.");
            message.setStatusCode(400);
            return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
        }

        comment.update(requestDto);
        return ResponseEntity.ok().body(new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseEntity<Message> deleteComment(Long commentId, User user) {
        Message message = new Message();

        Comment comment = findComment(commentId);

        if(!confirmUser(comment, user)){
            message.setMessage("작성자만 삭제할 수 있습니다.");
            message.setStatusCode(400);
            return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
        }

        commentRepository.delete(comment);

        message.setMessage("삭제 완료");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );
    }

    private boolean confirmUser(Comment comment, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        return userRoleEnum != UserRoleEnum.USER || Objects.equals(comment.getUser().getId(), user.getId());
    }
    @Transactional
    public ResponseEntity<Message> likeComment(Long id, User user) {
        Message message = new Message();
        Comment comment = findComment(id);
        if(likeRepository.findByUserAndComment(user, comment).isPresent()){
            message.setMessage("좋아요 이미 되어 있습니다");
            return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
        }
        Like like = likeRepository.save(new Like(user, comment));
        comment.increseLikesCount();
        message.setMessage("좋아요 완료");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Message> deleteLikeComment(Long id, User user) {
        Message message = new Message();
        Comment comment = findComment(id);
        if(likeRepository.findByUserAndComment(user, comment).isEmpty()){
            message.setMessage("좋아요 이미 삭제 되어 있습니다");
            return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
        }
        Optional<Like> like = likeRepository.findByUserAndComment(user, comment);
        likeRepository.delete(like.get());
        comment.decreseLikesCount();
        message.setMessage("좋아요 취소");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
}

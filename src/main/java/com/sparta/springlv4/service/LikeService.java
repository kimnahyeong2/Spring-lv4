package com.sparta.springlv4.service;

import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.Like;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.BoardRepository;
import com.sparta.springlv4.repository.CommentRepository;
import com.sparta.springlv4.repository.LikeRepository;
import com.sparta.springlv4.status.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final MessageSource messageSource;

    @Transactional
    public ResponseEntity<Message> likeBoardOrComment(Long id, User user, Long category) {

        if(likeRepository.findByUserAndCategoryAndTargetId(user, category, id).isPresent()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.like",
                    null,
                    "이미 좋아요 되어 있습니다",
                    Locale.getDefault()
            ));
        }

        if(category==0L){
            Board board = boardRepository.findById(id).orElseThrow(()->
                    new IllegalArgumentException(messageSource.getMessage(
                            "not.exist.post",
                            null,
                            "해당 게시물이 존재하지 않습니다",
                            Locale.getDefault()
                    ))
            );;
            board.increseLikesCount();
        }
        else{
            Comment comment = commentRepository.findById(id).orElseThrow(()->
                    new IllegalArgumentException(messageSource.getMessage(
                            "not.exist.post",
                            null,
                            "해당 댓글이 존재하지 않습니다",
                            Locale.getDefault()
                    ))
            );;
            comment.increseLikesCount();
        }

        Like like = likeRepository.save(new Like(user, id, category));
        String msg ="좋아요 완료";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> deleteLikeBoardOrComment(Long id, User user, Long category) {

        if(likeRepository.findByUserAndCategoryAndTargetId(user, category, id).isEmpty()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.delete.like",
                    null,
                    "이미 좋아요 되어 있지 않습니다",
                    Locale.getDefault()
            ));
        }

        if(category == 0L){
            Board board = boardRepository.findById(id).orElseThrow(()->
                    new IllegalArgumentException(messageSource.getMessage(
                            "not.exist.post",
                            null,
                            "해당 게시물이 존재하지 않습니다",
                            Locale.getDefault()
                    ))
            );

            board.decreseLikesCount();
        }
        else{
            Comment comment = commentRepository.findById(id).orElseThrow(()->
                    new IllegalArgumentException(messageSource.getMessage(
                            "not.exist.post",
                            null,
                            "해당 댓글이 존재하지 않습니다",
                            Locale.getDefault()
                    ))
            );

            comment.decreseLikesCount();
        }

        Optional<Like> like = likeRepository.findByUserAndCategoryAndTargetId(user, category, id);
        likeRepository.delete(like.get());
        String msg ="좋아요 취소";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }


}

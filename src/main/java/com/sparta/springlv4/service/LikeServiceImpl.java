package com.sparta.springlv4.service;

import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.Like;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.BoardRepository;
import com.sparta.springlv4.repository.CommentRepository;
import com.sparta.springlv4.repository.LikeRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class LikeServiceImpl implements LikeService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final MessageSource messageSource;
    private final BoardService boardService;
    private final CommentService commentService;

    /**
     * 좋아요 등록
     *
     * @param id       선택한 게시글 혹은 댓글 요청 정보
     * @param user     좋아요 요청자
     * @param category 게시글/댓글 구분 요청자
     */
    @Override
    @Transactional
    public void likeBoardOrComment(Long id, User user, Long category) {

        if (category == 0L) {
            Board board = boardService.findBoard(id);
            board.increseLikesCount();
        } else {
            Comment comment = commentService.findComment(id);
            comment.increseLikesCount();
        }
        if (likeRepository.existsByUserAndCategoryAndTargetId(user, category, id)) {
            throw new DuplicateRequestException("이미 좋아요 완료 되었습니다.");
        }
        Like like = new Like(user, id, category);
        likeRepository.save(like);
    }

    /**
     * 좋아요 취소
     *
     * @param id       선택한 게시글 혹은 댓글 요청 정보
     * @param user     좋아요 취소 요청자
     * @param category 게시글/댓글 구분 요청자
     */
    @Override
    @Transactional
    public void deleteLikeBoardOrComment(Long id, User user, Long category) {
        if (category == 0L) {
            Board board = boardService.findBoard(id);
            board.decreseLikesCount();
        } else {
            Comment comment = commentService.findComment(id);
            comment.decreseLikesCount();
        }
        Optional<Like> like = likeRepository.findByUserAndCategoryAndTargetId(user, category, id);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        } else {
            throw new IllegalArgumentException("취소할 좋아요가 없습니다.");
        }
    }
}

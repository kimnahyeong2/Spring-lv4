package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.CommentRepository;
import com.sparta.springlv4.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardServiceImpl boardService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    /**
     * 댓글 생성
     *
     * @param boardId    댓글 생성할 게시글 요청정보
     * @param requestDto 댓글 생성 요청정보
     * @param user       댓글 생성 요청자
     * @return 댓글 생성 결과
     */
    @Override
    public CommentResponseDto addComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = boardService.findBoard(boardId);
        Comment comment = commentRepository.save(new Comment(requestDto, user, board));
        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 수정
     *
     * @param comment    댓글 요청정보
     * @param requestDto 댓글 수정 요청정보
     * @param user       댓글 수정 요청자
     * @return 댓글 수정 결과
     */
    @Override
    @Transactional
    public CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user) {
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param comment 댓글 요청정보
     * @param user    댓글 삭제 요청자
     */
    @Override
    @Transactional
    public void deleteComment(Comment comment, User user) {
        commentRepository.delete(comment);
        likeRepository.deleteAllByCategoryAndTargetIdAndUser(1L, comment.getId(), user);
    }

    /**
     * 댓글 유무 확인
     *
     * @param commentId 확인하고 싶은 댓글 요청정보
     * @return 확인 결과
     */
    @Override
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );
    }
}

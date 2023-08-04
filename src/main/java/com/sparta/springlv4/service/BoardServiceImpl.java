package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.BoardRequestDto;
import com.sparta.springlv4.dto.BoardResponseDto;
import com.sparta.springlv4.dto.BoardSearchCond;
import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.BoardRepository;
import com.sparta.springlv4.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    /**
     * 게시글 조회
     *
     * @return 등록된 게시물 조회 결과
     */
    @Override
    public List<BoardResponseDto.BoardReadResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream().map(BoardResponseDto.BoardReadResponseDto::new).toList();
    }

    /**
     * 게시글 생성
     *
     * @param requestDto 게시글 생성 요청정보
     * @param user       게시글 생성 요청자
     * @return 게시글 생성 결과
     */
    @Override
    public BoardResponseDto.BoardBasicResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = boardRepository.save(new Board(requestDto, user));
        return new BoardResponseDto.BoardBasicResponseDto(board);
    }

    /**
     * 게시글 선택 조회
     *
     * @param id 선택 게시글 요청정보
     * @return 선택 게시글 조회 결과
     */
    @Override
    public BoardResponseDto.BoardReadResponseDto getSelectBoards(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto.BoardReadResponseDto(board);
    }

    /**
     * 게시글 수정
     *
     * @param board      게시글 요청정보
     * @param requestDto 게시글 수정 요청정보
     * @param user       게시글 수정 요청자
     * @return 게시글 수정 결과
     */
    @Override
    @Transactional
    public BoardResponseDto.BoardReadResponseDto updateBoard(Board board, BoardRequestDto requestDto, User user) {
        board.update(requestDto);
        return new BoardResponseDto.BoardReadResponseDto(board);
    }

    /**
     * 게시글 삭제
     *
     * @param board 게시글 요청정보
     * @param user  게시글 삭제 요청자
     */
    @Override
    @Transactional
    public void deleteBoard(Board board, User user) {
        boardRepository.delete(board);
        List<Comment> commentList = board.getCommentList();
        likeRepository.deleteAllByCategoryAndTargetIdAndUser(0L, board.getId(), user);
        for (Comment comment : commentList) {
            likeRepository.deleteAllByCategoryAndTargetIdAndUser(1L, comment.getId(), user);
        }
    }

    /**
     * 게시글 유무 확인
     *
     * @param id 확인하고 싶은 게시글 요청정보
     * @return 확인 결과
     */
    @Override
    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    /**
     * 게시글 검색
     *
     * @return 등록된 게시물 조회 결과
     * @Param 검색 단어 요청 정보
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BoardSearchCond> getSearchList(String searchWorld, Pageable pageable) {
        var cond = BoardSearchCond.builder().title(searchWorld).build();
        return boardRepository.search(searchWorld, pageable);
    }
}

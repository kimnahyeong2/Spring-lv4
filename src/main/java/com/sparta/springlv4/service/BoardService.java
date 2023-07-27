package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.BoardRequestDto;
import com.sparta.springlv4.dto.BoardResponseDto;
import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.Like;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.repository.BoardRepository;
import com.sparta.springlv4.repository.LikeRepository;
import com.sparta.springlv4.status.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final MessageSource messageSource;


    public List<BoardResponseDto.BoardReadResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream().map(BoardResponseDto.BoardReadResponseDto::new).toList();
    }

    public BoardResponseDto.BoardBasicResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = boardRepository.save(new Board(requestDto, user));
        return new BoardResponseDto.BoardBasicResponseDto(board);
    }

    public BoardResponseDto.BoardReadResponseDto getSelectBoards(Long id) {
        Board board = findBoard(id);
        return ResponseEntity.ok().body(new BoardResponseDto.BoardReadResponseDto(board)).getBody();
    }

    @Transactional
    public BoardResponseDto.BoardReadResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        Board board = findBoard(id);
        confirmUser(board, user);
        board.update(requestDto);
        return ResponseEntity.ok().body(new BoardResponseDto.BoardReadResponseDto(board)).getBody();
    }

    public ResponseEntity<Message> deleteBoard(Long id, BoardRequestDto requestDto, User user){
        Board board = findBoard(id);
        confirmUser(board, user);
        
        boardRepository.delete(board);
        String msg ="삭제 완료";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    private Board findBoard(Long id){
        return boardRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException(messageSource.getMessage(
                        "not.exist.post",
                        null,
                        "해당 게시물이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );
    }

    private void confirmUser(Board board, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER && !Objects.equals(board.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "not.your.post",
                    null,
                    "작성자만 수정 및 삭제가 가능합니다",
                    Locale.getDefault()
            ));
        }
    }

    @Transactional
    public ResponseEntity<Message> likeBoard(Long id, User user) {
        Board board = findBoard(id);
        if(likeRepository.findByUserAndBoard(user, board).isPresent()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.like",
                    null,
                    "이미 좋아요 되어 있습니다",
                    Locale.getDefault()
            ));
        }
        Like like = likeRepository.save(new Like(user, id, 1L));
        String msg ="좋아요 완료";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> deleteLikeBoard(Long id, User user){
        Board board = findBoard(id);
        if(likeRepository.findByUserAndBoard(user, board).isEmpty()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.delete.like",
                    null,
                    "이미 좋아요 되어 있지 않습니다",
                    Locale.getDefault()
            ));
        }
        Optional<Like> like = likeRepository.findByUserAndBoard(user, board);
        likeRepository.delete(like.get());
        board.decreseLikesCount();
        String msg ="좋아요 취소";
        Message message = new Message(msg, HttpStatus.OK.value());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
}
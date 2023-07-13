package com.sparta.springlv4.entity;

import com.sparta.springlv4.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="comment")
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comments", nullable = false)
    private String comments;

    @Column(name = "likes", nullable = false)
    private int likesCount=0;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    public Comment(CommentRequestDto requestDto, User user, Board board) {
        this.comments = requestDto.getComments();
        this.user = user;
        this.board = board;
    }

    public void update(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }

    public void increseLikesCount(){
        this.likesCount++;
    }

    public void decreseLikesCount(){
        this.likesCount--;
    }
}

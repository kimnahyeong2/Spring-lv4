package com.sparta.springlv4.dto;

import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.error.MessageDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto extends MessageDto {
    private Long id;
    private String username;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.comments = comment.getComments();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikesCount();
    }
}

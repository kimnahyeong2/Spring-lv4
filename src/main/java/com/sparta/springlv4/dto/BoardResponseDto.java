package com.sparta.springlv4.dto;

import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardResponseDto {
    // param 형식으로 값을 넘겨주려고 할 때에는 @Setter와 @NoArgsConstructor를 넣어주어야함
    // @JsonInclude(JsonInclude.NON_NULL) null값이 아닌 애들만 반환을 하겠다는 것
    @Getter
    public static class BoardBasicResponseDto{
        private Long id;
        private String title;
        private String username;
        private String contents;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public BoardBasicResponseDto(Board board){
            this.id = board.getId();
            this.title = board.getTitle();
            this.username = board.getUser().getUsername();
            this.contents = board.getContents();
            this.createdAt = board.getCreatedAt();
            this.modifiedAt = board.getModifiedAt();
        }
    }

    @Getter
    public static class BoardReadResponseDto {
        private String title;
        private String username;
        private String contents;
        private LocalDateTime createdAt;
        private int likeCount;

        private List<CommentResponseDto> commentList  = new ArrayList<>();

        public BoardReadResponseDto(Board board){
            this.title = board.getTitle();
            this.username = board.getUser().getUsername();
            this.contents = board.getContents();
            this.createdAt = board.getCreatedAt();
            this.likeCount = board.getLikesCount();

            for(Comment comment : board.getCommentList()) {
                commentList.add(new CommentResponseDto(comment));
            }
        }
    }
}

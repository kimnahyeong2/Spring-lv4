package com.sparta.springlv4.service;

import com.sparta.springlv4.entity.User;

public interface LikeService {
    void likeBoardOrComment(Long id, User user, Long category);

    void deleteLikeBoardOrComment(Long id, User user, Long category);
}

package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Like;
import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndCategoryAndTargetId(User user, Long category, Long id);

    Boolean existsByUserAndCategoryAndTargetId(User user, Long category, Long id);

    void deleteAllByCategoryAndTargetIdAndUser(Long category, Long targetId, User user);
}

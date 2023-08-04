package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = Board.class, idClass = Long.class)
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryQuery {
    List<Board> findAllByOrderByCreatedAtDesc();
}
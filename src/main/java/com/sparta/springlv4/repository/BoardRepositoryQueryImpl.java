package com.sparta.springlv4.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springlv4.dto.BoardSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta.springlv4.entity.QBoard.board;

@RequiredArgsConstructor
@Slf4j(topic = "짜증")
public class BoardRepositoryQueryImpl implements BoardRepositoryQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardSearchCond> search(String searchWorld, Pageable pageable) {
        long totalSize = countQuery(searchWorld).fetch().get(0);
        log.info("totalSize" + String.valueOf(totalSize));

        log.info("pageable.getOffset() : " + pageable.getOffset());
        log.info("pageable.getPageSize() : " + pageable.getPageSize());
        List<BoardSearchCond> query =
                jpaQueryFactory.select(
                                Projections.fields(BoardSearchCond.class, board.title, board.contents)
                        )
                        .from(board)
                        //.leftJoin(board.user)
                        .where(
                                searchWorldTitleContain(searchWorld)
                                        .or(searchWorldContentsContain(searchWorld))
                        )
                        .offset(pageable.getOffset())// 페이지 번호
                        .limit(pageable.getPageSize())// 페이지 사이즈
                        .fetch();

        return PageableExecutionUtils.getPage(query, pageable, () -> totalSize);
    }

    private BooleanExpression searchWorldTitleContain(String searchWorld) {
        return board.title.contains(searchWorld);
    }

    private BooleanExpression searchWorldContentsContain(String searchWorld) {
        return board.contents.contains(searchWorld);
    }

    private JPAQuery<Long> countQuery(String searchWorld) {
        return jpaQueryFactory.select(Wildcard.count)
                .from(board)
                .where(
                        searchWorldTitleContain(searchWorld)
                                .or(searchWorldContentsContain(searchWorld))
                );
    }
}

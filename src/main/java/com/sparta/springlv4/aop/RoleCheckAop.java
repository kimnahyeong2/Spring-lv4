package com.sparta.springlv4.aop;

import com.sparta.springlv4.entity.Board;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

@Slf4j(topic = "RoleCheckAop")
@Aspect
@Component
public class RoleCheckAop {

    @Pointcut("execution(* com.sparta.springlv4.service.BoardService.updateBoard(..))")
    private void updatePost() {}

    @Pointcut("execution(* com.sparta.springlv4.service.BoardService.deleteBoard(..))")
    private void deletePost() {}

    @Pointcut("execution(* com.sparta.springlv4.service.BoardService.updateBoard(..))")
    private void updateBoard() {}

    @Pointcut("execution(* com.sparta.springlv4.service.BoardService.deleteBoard(..))")
    private void deleteBoard() {}


    @Around("updatePost() || deletePost()")
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫번째 매개변수로 게시글 받아옴
        Board board = (Board) joinPoint.getArgs()[0];

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인 회원 정보
            UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
            User loginUser = userDetails.getUser();

            // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || board.getUser().equals(loginUser))) {
                log.warn("[AOP] 작성자만 게시글을 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }

    @Around("updateBoard() || deleteBoard()")
    public Object executeBoardRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫번째 매개변수로 게시글 받아옴
        Board board = (Board)joinPoint.getArgs()[0];

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인 회원 정보
            UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
            User loginUser = userDetails.getUser();

            // 댓글 작성자(board.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || board.getUser().equals(loginUser))) {
                log.warn("[AOP] 작성자만 댓글을 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }
}
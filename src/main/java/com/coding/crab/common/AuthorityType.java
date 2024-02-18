package com.coding.crab.common;

/**
 * @author : yong
 * @fileName : AuthorityType
 * @date : 2024-02-11
 * @description : 회원의 권한을 구분하기 위한 enum 클래스입니다.
 */
public enum AuthorityType {
    ROLE_ADMIN,     // 관리자
    ROLE_USER,      // 유저 (로그인 O)
    ROLE_GUEST      // 게스트 (로그인 X)
}

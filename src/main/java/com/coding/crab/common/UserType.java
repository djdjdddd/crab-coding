package com.coding.crab.common;

/**
 * @author : yong
 * @fileName : UserType
 * @date : 2024-02-11
 * @description : 기본 회원가입을 통해 가입한 계정과 소셜 로그인을 통해 가입한 계정을 구분하기 위한 enum 클래스입니다.
 */
public enum UserType {
    DEFAULT,    // 기본 회원가입을 통해 가입한 계정
    OAUTH       // 소셜 로그인을 통해 가입한 계정
}

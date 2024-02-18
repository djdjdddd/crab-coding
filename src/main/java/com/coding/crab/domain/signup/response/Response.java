package com.coding.crab.domain.signup.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Response {
    SUCCESS("SUCCESS")
    ,FAIL("FAIL");

    private final String status;

    /**
     * ★@JsonCreator란? annotation used for indicating that a constructor or static factory method should be used for creating value instances during deserialization.
     * @see JsonCreator Jackson 공식문서 : https://github.com/FasterXML/jackson-annotations/wiki/Jackson-Annotations
     * @param status
     * @return
     */
    @JsonCreator
    private static Response from(String status){
        for(Response response : Response.values()){
            if(response.getStatus().equalsIgnoreCase(status)){ // 1. 예를 들어, FE에서 보낸 값이 "FAIL"이면
                return response; // 2. FAIL 객체를 리턴합니다.
            }
        }
        throw new IllegalArgumentException(">>> 잘못된 status 값을 입력하였습니다 : " + status);
    }
}

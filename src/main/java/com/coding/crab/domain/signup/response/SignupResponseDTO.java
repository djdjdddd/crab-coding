package com.coding.crab.domain.signup.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupResponseDTO {
    Response response;

    @Builder
    public SignupResponseDTO(Response response){
        this.response = response;
    }
}

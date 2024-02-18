package com.coding.crab.domain.signup.controller;

import com.coding.crab.api.mail.MailAuthenticationMessage;
import com.coding.crab.api.mail.MailDTO;
import com.coding.crab.common.exception.ValidationException;
import com.coding.crab.domain.signup.request.SignupRequestDTO;
import com.coding.crab.domain.signup.response.Response;
import com.coding.crab.domain.signup.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입", description = "회원가입 API") // swagger 적용 위한 어노테이션
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    @GetMapping("/home")
    @Operation(summary = "회원가입 화면 요청")
    public String signup(){
        return "/signup/home";
    }

    @PostMapping("/auth-request")
    @ResponseBody
    @Operation(summary = "인증 요청 버튼", description = "회원가입시 필요한 메일 인증을 요청합니다. 요청시 인증 코드가 발송됩니다.")
    public String authRequest(@RequestBody MailDTO mailDTO){ // **MailDTO를 SignupRequestDTO로 바꿔줘도 될듯
        log.info("인증 요청 버튼 누름");
        log.info("mailDTO = {}", mailDTO);

        signupService.sendAuthCodeMail(mailDTO);

//        return "MEMBER_EXISTS"; // 적절한 Code를 리턴하게끔
        return "0";

//        // 테스트
//        MailDTO testDTO = new MailDTO("djdjdddd@khu.ac.kr");
//        MailDTO testDTO2 = new MailDTO("djdjdddd@naver.com");
//        signupService.sendAuthCodeMail(testDTO2);
//        // 테스트
    }

    @PostMapping("/mail-auth")
    @ResponseBody
    @Operation(summary = "메일 인증 버튼", description = "인증 코드를 확인합니다.")
    public MailAuthenticationMessage mailAuth(@RequestBody SignupRequestDTO signupRequestDTO){
        log.info("메일 인증 버튼 누름");
        log.info("signupRequestDTO = {}", signupRequestDTO);
        return signupService.checkAuthentication(signupRequestDTO);
    }

    /**
     * @param signupRequestDTO
     * ★form 태그의 action의 문제점 : setter 메서드가 없으면 값 매핑이 안됨. 왜냐면 스프링이 DTO 객체를 생성하고 Setter를 통해 값을 할당하기 때문에...
     * ★form 태그는 쓰되 json 데이터 만들어서 넘겨주면 당연히 setter 메서드가 없어도 됨. 대신 @RequestBody 어노테이션 필요
     */
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Response> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("회원가입 유효성 검사 실패.", bindingResult.getFieldErrors()); // **(Throwable) 캐스팅이 왜 필요 없지?? -> 커스텀 ValidationException 클래스를 사용 중이었음.
        }
        log.info("회원가입 유효성 검사 통과.");
        log.info("signupRequestDTO = {}", signupRequestDTO);
        signupService.signup(signupRequestDTO);

        // TODO. ResponseEntity 로 변경하기. (참조 : https://stir.tistory.com/343#ResponseEntity%EB%A-%BC%--%EC%-E%--%--%EC%--%B-%EB%-A%--%--%EB%B-%A-%EB%B-%--)
//        return ResponseEntity.ok("SUCCESS"); // 기존 코드
//        return ResponseEntity.ok(Response.SUCCESS); // 변경 코드 1
        return ResponseEntity.ok()
                //.headers()
                .body(Response.SUCCESS);
    }

}

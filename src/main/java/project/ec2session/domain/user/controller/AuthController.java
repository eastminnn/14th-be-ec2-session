package project.ec2session.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ec2session.domain.user.dto.TokenDto;
import project.ec2session.domain.user.dto.UserReq;
import project.ec2session.domain.user.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public class AuthController{
    private final AuthService authService;

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하고 JWT 액세스 토큰을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenDto.class))),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(examples = @ExampleObject(value = "{\"username\":\"아이디는 필수 입력 값입니다.\"}"))),
            @ApiResponse(responseCode = "404", description = "아이디 또는 비밀번호 불일치",
                    content = @Content(examples = @ExampleObject(value = "{\"status\":404,\"message\":\"정보를 정확히 입력해주세요.\"}")))
    })
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @RequestBody @Valid UserReq.SignInDto request
    ) {
        TokenDto response = authService.signIn(request);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 닉네임으로 새 사용자를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(examples = @ExampleObject(value = "{\"userId\":1}"))),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(examples = @ExampleObject(value = "{\"nickname\":\"닉네임은 필수 입력 값입니다.\"}")))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserReq.SignUpDto request) {
        Long userId = authService.signUp(request);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }
}

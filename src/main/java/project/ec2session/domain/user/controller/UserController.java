package project.ec2session.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ec2session.common.auth.CustomUserDetails;
import project.ec2session.domain.user.dto.UserReq;
import project.ec2session.domain.user.dto.UserRes;
import project.ec2session.domain.user.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 조회 및 정보 수정 API")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "JWT 토큰으로 인증된 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserRes.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "{\"status\":404,\"message\":\"존재하지 않는 회원입니다.\"}")))
    })
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.readById(userDetails.getUserId()));
    }

    @Operation(summary = "전체 사용자 조회", description = "가입된 모든 사용자 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 사용자 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRes.class)))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.readAll());
    }

    @Operation(summary = "내 정보 수정", description = "JWT 토큰으로 인증된 사용자의 닉네임을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "요청 성공"))),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(examples = @ExampleObject(value = "{\"nickname\":\"닉네임은 필수 입력 값입니다.\"}"))),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "{\"status\":404,\"message\":\"존재하지 않는 회원입니다.\"}")))
    })
    @PutMapping
    public ResponseEntity<?> updateUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody @Valid UserReq.UpdateInfo request) {
        userService.update(userDetails.getUserId(), request);
        return ResponseEntity.ok("요청 성공");
    }
}

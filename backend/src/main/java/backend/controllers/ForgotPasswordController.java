package backend.controllers;

import backend.dto.requests.ResetPasswordRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.exceptions.UserVerificationFailedException;
import backend.exceptions.*;
import backend.services.AuthService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/mywallet/auth/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private AuthService authService;

    @GetMapping("/verifyEmail")
    public ResponseEntity<ApiResponseDto<?>> verifyEmail(
            @RequestParam("email") String email
    ) throws UserNotFoundException, UserServiceLogicException {

        return authService.verifyEmailAndSendForgotPasswordVerificationEmail(email);
    }

    @GetMapping("/verifyCode")
    public ResponseEntity<ApiResponseDto<?>> verifyCode(
            @RequestParam("code") String code
    ) throws UserVerificationFailedException, UserServiceLogicException {

        return authService.verifyForgotPasswordVerification(code);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponseDto<?>> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDto resetPasswordDto
    ) throws UserNotFoundException, UserServiceLogicException {

        return authService.resetPassword(resetPasswordDto);
    }

    @GetMapping("/resendEmail")
    public ResponseEntity<ApiResponseDto<?>> resendEmail(
            @RequestParam("email") String email
    ) throws UserNotFoundException, UserServiceLogicException {

        return authService.verifyEmailAndSendForgotPasswordVerificationEmail(email);
    }
}
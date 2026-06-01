package backend.controllers;

import backend.dto.requests.SignUpRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.UserAlreadyExistsException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.exceptions.UserVerificationFailedException;
import backend.exceptions.*;

import backend.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/mywallet/auth")
public class SignUpController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<?>> registerUser(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto
    ) throws MessagingException,
            UnsupportedEncodingException,
            UserAlreadyExistsException,
            UserServiceLogicException {

        return authService.save(signUpRequestDto);
    }

    @GetMapping("/signup/verify")
    public ResponseEntity<ApiResponseDto<?>> verifyUserRegistration(
            @RequestParam("code") String code
    ) throws UserVerificationFailedException {

        return authService.verifyRegistrationVerification(code);
    }

    @GetMapping("/signup/resend")
    public ResponseEntity<ApiResponseDto<?>> resendVerificationCode(
            @RequestParam("email") String email
    ) throws UserNotFoundException,
            MessagingException,
            UnsupportedEncodingException,
            UserServiceLogicException {

        return authService.resendVerificationCode(email);
    }
}
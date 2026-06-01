package backend.services;

import backend.dto.responses.ApiResponseDto;
import backend.dto.requests.ResetPasswordRequestDto;
import backend.dto.requests.SignUpRequestDto;
import backend.exceptions.UserAlreadyExistsException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.exceptions.UserVerificationFailedException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface AuthService {

    ResponseEntity<ApiResponseDto<?>> save(
            SignUpRequestDto signUpRequestDto
    ) throws MessagingException,
            UnsupportedEncodingException,
            UserAlreadyExistsException,
            UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> resendVerificationCode(
            String email
    ) throws MessagingException,
            UnsupportedEncodingException,
            UserNotFoundException,
            UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> verifyEmailAndSendForgotPasswordVerificationEmail(
            String email
    ) throws UserServiceLogicException,
            UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> verifyForgotPasswordVerification(
            String code
    ) throws UserVerificationFailedException,
            UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> verifyRegistrationVerification(
            String code
    ) throws UserVerificationFailedException;

    ResponseEntity<ApiResponseDto<?>> resetPassword(
            ResetPasswordRequestDto resetPasswordDto
    ) throws UserNotFoundException,
            UserServiceLogicException;
}
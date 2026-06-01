package backend.controllers;

import backend.dto.requests.ResetPasswordRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.RoleNotFoundException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;

import backend.services.AuthService;
import backend.services.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mywallet/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> getAllUsers(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("searchKey") String searchKey
    ) throws RoleNotFoundException, UserServiceLogicException {

        return userService.getAllUsers(pageNumber, pageSize, searchKey);
    }

    @DeleteMapping("/disable")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> disableUser(
            @RequestParam("userId") long userId
    ) throws UserNotFoundException, UserServiceLogicException {

        return userService.enableOrDisableUser(userId);
    }

    @PutMapping("/enable")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> enableUser(
            @RequestParam("userId") long userId
    ) throws UserNotFoundException, UserServiceLogicException {

        return userService.enableOrDisableUser(userId);
    }

    @PostMapping("/settings/changePassword")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> changePassword(
            @RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto
    ) throws UserNotFoundException, UserServiceLogicException {

        return authService.resetPassword(resetPasswordRequestDto);
    }

    @PostMapping("/settings/profileImg")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> uploadProfileImg(
            @RequestParam("email") String email,
            @RequestParam("file") MultipartFile file
    ) throws UserNotFoundException, UserServiceLogicException {

        return userService.uploadProfileImg(email, file);
    }

    @GetMapping("/settings/profileImg")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> getProfileImg(
            @RequestParam("email") String email
    ) throws UserNotFoundException, UserServiceLogicException, IOException {

        return userService.getProfileImg(email);
    }

    @DeleteMapping("/settings/profileImg")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> deleteProfileImg(
            @RequestParam("email") String email
    ) throws UserNotFoundException, UserServiceLogicException, IOException {

        return userService.deleteProfileImg(email);
    }
}
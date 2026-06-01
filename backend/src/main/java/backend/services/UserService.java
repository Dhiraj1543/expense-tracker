package backend.services;

import backend.dto.responses.ApiResponseDto;
import backend.exceptions.RoleNotFoundException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByEmail(String email) throws UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> getAllUsers(
            int pageNumber,
            int pageSize,
            String searchKey
    ) throws RoleNotFoundException, UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> enableOrDisableUser(
            long userId
    ) throws UserNotFoundException, UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> uploadProfileImg(
            String email,
            MultipartFile file
    ) throws UserServiceLogicException, UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> getProfileImg(
            String email
    ) throws UserNotFoundException, IOException, UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> deleteProfileImg(
            String email
    ) throws UserServiceLogicException, UserNotFoundException;
}
package backend.services.impls;

import backend.dto.responses.ApiResponseDto;
import backend.dto.responses.PageResponseDto;
import backend.dto.responses.UserResponseDto;
import backend.enums.ApiResponseStatus;
import backend.enums.ETransactionType;
import backend.exceptions.RoleNotFoundException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.exceptions.*;
import backend.factories.RoleFactory;
import backend.models.User;
import backend.repository.TransactionRepository;
import backend.repository.TransactionTypeRepository;
import backend.repository.UserRepository;
import backend.services.NotificationService;
import backend.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final RoleFactory roleFactory;
    private final TransactionRepository transactionRepository;
    private final TransactionTypeRepository transactionTypeRepository;

    @Value("${app.user.profile.upload.dir}")
    private String userProfileUploadDir;

    @Override
    public ResponseEntity<ApiResponseDto<?>> getAllUsers(
            int pageNumber,
            int pageSize,
            String searchKey
    ) throws RoleNotFoundException, UserServiceLogicException {

        try {

            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<User> users = userRepository.findAll(
                    pageable,
                    roleFactory.getInstance("user").getId(),
                    searchKey
            );

            List<UserResponseDto> userResponseDtoList = users.stream()
                    .map(this::userToUserResponseDto)
                    .toList();

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            new PageResponseDto<>(
                                    userResponseDtoList,
                                    users.getTotalPages(),
                                    users.getTotalElements()
                            )
                    )
            );

        } catch (Exception e) {

            log.error("Failed to fetch users", e);

            throw new UserServiceLogicException(
                    "Failed to fetch users. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> enableOrDisableUser(
            long userId
    ) throws UserNotFoundException, UserServiceLogicException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id " + userId
                        )
                );

        try {

            user.setEnabled(!user.isEnabled());

            userRepository.save(user);

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            "User updated successfully!"
                    )
            );

        } catch (Exception e) {

            log.error("Failed to enable/disable user", e);

            throw new UserServiceLogicException(
                    "Failed to update user. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> uploadProfileImg(
            String email,
            MultipartFile file
    ) throws UserServiceLogicException, UserNotFoundException {

        if (!existsByEmail(email)) {
            throw new UserNotFoundException(
                    "User not found with email " + email
            );
        }

        try {

            User user = findByEmail(email);

            // ❗ Get file extension safely
            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new UserServiceLogicException("Invalid file name");
            }

            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String newFileName = user.getUsername() + extension;

            // ✅ FIX: ensure directory exists
            Path uploadPath = Paths.get(userProfileUploadDir).toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetLocation = uploadPath.resolve(newFileName);

            // optional: delete old file if exists
            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // store relative path (better than full system path)
            user.setProfileImgUrl(targetLocation.toString());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.CREATED,
                            "Profile image updated successfully!"
                    )
            );

        } catch (Exception e) {

            log.error("Failed to upload profile image", e);

            throw new UserServiceLogicException(
                    "Failed to upload profile image. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getProfileImg(
            String email
    ) throws UserNotFoundException, IOException,
            UserServiceLogicException {

        if (!existsByEmail(email)) {

            throw new UserNotFoundException(
                    "User not found with email " + email
            );
        }

        try {

            User user = findByEmail(email);

            if (user.getProfileImgUrl() == null) {

                return ResponseEntity.ok(
                        new ApiResponseDto<>(
                                ApiResponseStatus.SUCCESS,
                                HttpStatus.OK,
                                null
                        )
                );
            }

            Path profileImgPath = Paths.get(
                    user.getProfileImgUrl()
            );

            byte[] imageBytes =
                    Files.readAllBytes(profileImgPath);

            String base64Image = Base64.getEncoder()
                    .encodeToString(imageBytes);

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            base64Image
                    )
            );

        } catch (Exception e) {

            log.error("Failed to fetch profile image", e);

            throw new UserServiceLogicException(
                    "Failed to fetch profile image. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> deleteProfileImg(
            String email
    ) throws UserServiceLogicException, UserNotFoundException {

        if (!existsByEmail(email)) {

            throw new UserNotFoundException(
                    "User not found with email " + email
            );
        }

        try {

            User user = findByEmail(email);

            if (user.getProfileImgUrl() == null) {

                return ResponseEntity.ok(
                        new ApiResponseDto<>(
                                ApiResponseStatus.SUCCESS,
                                HttpStatus.OK,
                                "No profile image found!"
                        )
                );
            }

            File file = new File(user.getProfileImgUrl());

            if (file.exists() && file.delete()) {

                user.setProfileImgUrl(null);

                userRepository.save(user);

                return ResponseEntity.ok(
                        new ApiResponseDto<>(
                                ApiResponseStatus.SUCCESS,
                                HttpStatus.OK,
                                "Profile image removed successfully!"
                        )
                );
            }

            throw new UserServiceLogicException(
                    "Failed to remove profile image!"
            );

        } catch (Exception e) {

            log.error("Failed to delete profile image", e);

            throw new UserServiceLogicException(
                    "Failed to delete profile image. Try again later!"
            );
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findByEmail(String email)
            throws UserNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email " + email
                        )
                );
    }

    private UserResponseDto userToUserResponseDto(User user) {

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),

                transactionRepository.findTotalByUserAndTransactionType(
                        user.getId(),
                        transactionTypeRepository
                                .findByTransactionTypeName(
                                        ETransactionType.TYPE_EXPENSE
                                )
                                .getTransactionTypeId(),
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                ),

                transactionRepository.findTotalByUserAndTransactionType(
                        user.getId(),
                        transactionTypeRepository
                                .findByTransactionTypeName(
                                        ETransactionType.TYPE_INCOME
                                )
                                .getTransactionTypeId(),
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                ),

                transactionRepository.findTotalNoOfTransactionsByUser(
                        user.getId(),
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                )
        );
    }
}
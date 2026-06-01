package backend.services.impls;

import backend.dto.requests.TransactionRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.dto.responses.PageResponseDto;
import backend.dto.responses.TransactionResponseDto;
import backend.enums.ApiResponseStatus;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.TransactionNotFoundException;
import backend.exceptions.TransactionServiceLogicException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.*;
import backend.models.Transaction;
import backend.repository.TransactionRepository;
import backend.services.CategoryService;
import backend.services.TransactionService;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<ApiResponseDto<?>> addTransaction(
            TransactionRequestDto transactionRequestDto)
            throws UserNotFoundException,
            CategoryNotFoundException,
            TransactionServiceLogicException {

        try {

            Transaction transaction =
                    transactionRequestDtoToTransaction(transactionRequestDto);

            transactionRepository.save(transaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.CREATED,
                            "Transaction has been successfully recorded!"
                    )
            );

        } catch (Exception e) {

            log.error("Error while adding transaction", e);

            throw new TransactionServiceLogicException(
                    "Failed to record transaction. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getTransactionsByUser(
            String email,
            int pageNumber,
            int pageSize,
            String searchKey,
            String sortField,
            String sortDirec,
            String transactionType
    ) throws TransactionServiceLogicException {

        try {

            Sort.Direction direction =
                    "DESC".equalsIgnoreCase(sortDirec)
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;

            Pageable pageable = PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by(direction, sortField)
            );

            Page<Transaction> transactions =
                    transactionRepository.findByUser(
                            email,
                            pageable,
                            searchKey,
                            transactionType
                    );

            if (transactions.getTotalElements() == 0) {

                return ResponseEntity.ok(
                        new ApiResponseDto<>(
                                ApiResponseStatus.SUCCESS,
                                HttpStatus.OK,
                                new PageResponseDto<>(
                                        Collections.emptyList(),
                                        0,
                                        0L
                                )
                        )
                );
            }

            List<TransactionResponseDto> transactionResponseDtoList =
                    transactions.stream()
                            .map(this::transactionToTransactionResponseDto)
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            new PageResponseDto<>(
                                    groupTransactionsByDate(
                                            transactionResponseDtoList
                                    ),
                                    transactions.getTotalPages(),
                                    transactions.getTotalElements()
                            )
                    )
            );

        } catch (Exception e) {

            log.error("Error while fetching user transactions", e);

            throw new TransactionServiceLogicException(
                    "Failed to fetch transactions. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getTransactionById(
            Long transactionId)
            throws TransactionNotFoundException {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException(
                                "Transaction not found with id : "
                                        + transactionId
                        )
                );

        return ResponseEntity.ok(
                new ApiResponseDto<>(
                        ApiResponseStatus.SUCCESS,
                        HttpStatus.OK,
                        transactionToTransactionResponseDto(transaction)
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> updateTransaction(
            Long transactionId,
            TransactionRequestDto transactionRequestDto
    ) throws TransactionNotFoundException,
            UserNotFoundException,
            CategoryNotFoundException,
            TransactionServiceLogicException {

        try {

            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() ->
                            new TransactionNotFoundException(
                                    "Transaction not found with id : "
                                            + transactionId
                            )
                    );

            transaction.setAmount(transactionRequestDto.getAmount());
            transaction.setDate(transactionRequestDto.getDate());
            transaction.setDescription(transactionRequestDto.getDescription());

            transaction.setUser(
                    userService.findByEmail(
                            transactionRequestDto.getUserEmail()
                    )
            );

            transaction.setCategory(
                    categoryService.getCategoryById(
                            transactionRequestDto.getCategoryId()
                    )
            );

            transactionRepository.save(transaction);

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            "Transaction has been successfully updated!"
                    )
            );

        } catch (Exception e) {

            log.error("Error while updating transaction", e);

            throw new TransactionServiceLogicException(
                    "Failed to update transaction. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> deleteTransaction(
            Long transactionId)
            throws TransactionNotFoundException,
            TransactionServiceLogicException {

        if (!transactionRepository.existsById(transactionId)) {

            throw new TransactionNotFoundException(
                    "Transaction not found with id : " + transactionId
            );
        }

        try {

            transactionRepository.deleteById(transactionId);

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            "Transaction deleted successfully!"
                    )
            );

        } catch (Exception e) {

            log.error("Error while deleting transaction", e);

            throw new TransactionServiceLogicException(
                    "Failed to delete transaction. Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getAllTransactions(
            int pageNumber,
            int pageSize,
            String searchKey
    ) throws TransactionServiceLogicException {

        try {

            Pageable pageable = PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by(Sort.Direction.DESC, "transactionId")
            );

            Page<Transaction> transactions =
                    transactionRepository.findAll(pageable, searchKey);

            if (transactions.getTotalElements() == 0) {

                return ResponseEntity.ok(
                        new ApiResponseDto<>(
                                ApiResponseStatus.SUCCESS,
                                HttpStatus.OK,
                                new PageResponseDto<>(
                                        Collections.emptyList(),
                                        0,
                                        0L
                                )
                        )
                );
            }

            List<TransactionResponseDto> transactionResponseDtoList =
                    transactions.stream()
                            .map(this::transactionToTransactionResponseDto)
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            new PageResponseDto<>(
                                    transactionResponseDtoList,
                                    transactions.getTotalPages(),
                                    transactions.getTotalElements()
                            )
                    )
            );

        } catch (Exception e) {

            log.error("Failed to fetch transactions", e);

            throw new TransactionServiceLogicException(
                    "Failed to fetch transactions. Try again later!"
            );
        }
    }

    private Transaction transactionRequestDtoToTransaction(
            TransactionRequestDto transactionRequestDto)
            throws UserNotFoundException, CategoryNotFoundException {

        return new Transaction(
                userService.findByEmail(
                        transactionRequestDto.getUserEmail()
                ),
                categoryService.getCategoryById(
                        transactionRequestDto.getCategoryId()
                ),
                transactionRequestDto.getDescription(),
                transactionRequestDto.getAmount(),
                transactionRequestDto.getDate()
        );
    }

    private TransactionResponseDto transactionToTransactionResponseDto(
            Transaction transaction) {

        return new TransactionResponseDto(
                transaction.getTransactionId(),
                transaction.getCategory().getCategoryId(),
                transaction.getCategory().getCategoryName(),
                transaction.getCategory()
                        .getTransactionType()
                        .getTransactionTypeId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getUser().getEmail()
        );
    }

    private Map<String, List<TransactionResponseDto>> groupTransactionsByDate(
            List<TransactionResponseDto> transactionResponseDtoList) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        return transactionResponseDtoList.stream()
                .collect(Collectors.groupingBy(t -> {

                    if (t.getDate().equals(today)) {
                        return "Today";
                    }

                    if (t.getDate().equals(yesterday)) {
                        return "Yesterday";
                    }

                    return t.getDate().toString();

                }))
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> {

                    if (entry1.getKey().equals("Today")) return -1;
                    if (entry2.getKey().equals("Today")) return 1;

                    if (entry1.getKey().equals("Yesterday")) return -1;
                    if (entry2.getKey().equals("Yesterday")) return 1;

                    return entry2.getKey()
                            .compareTo(entry1.getKey());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}
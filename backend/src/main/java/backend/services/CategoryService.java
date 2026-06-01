package backend.services;

import backend.dto.requests.CategoryRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.CategoryAlreadyExistsException;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.CategoryServiceLogicException;
import backend.exceptions.TransactionTypeNotFoundException;
import backend.models.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    ResponseEntity<ApiResponseDto<?>> getCategories();

    boolean existsCategory(int id);

    Category getCategoryById(int id)
            throws CategoryNotFoundException;

    ResponseEntity<ApiResponseDto<?>> addNewCategory(
            CategoryRequestDto categoryRequestDto
    ) throws TransactionTypeNotFoundException,
            CategoryServiceLogicException,
            CategoryAlreadyExistsException;

    ResponseEntity<ApiResponseDto<?>> updateCategory(
            int categoryId,
            CategoryRequestDto categoryRequestDto
    ) throws CategoryNotFoundException,
            TransactionTypeNotFoundException,
            CategoryServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> enableOrDisableCategory(
            int categoryId
    ) throws CategoryServiceLogicException,
            CategoryNotFoundException;
}
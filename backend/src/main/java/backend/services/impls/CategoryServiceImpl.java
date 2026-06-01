package backend.services.impls;

import backend.dto.responses.ApiResponseDto;
import backend.dto.requests.CategoryRequestDto;
import backend.enums.ApiResponseStatus;
import backend.exceptions.CategoryAlreadyExistsException;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.CategoryServiceLogicException;
import backend.exceptions.TransactionTypeNotFoundException;
import backend.models.Category;
import backend.repository.CategoryRepository;
import backend.services.CategoryService;
import backend.services.TransactionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionTypeService transactionTypeService;

    @Override
    public ResponseEntity<ApiResponseDto<?>> getCategories() {

        return ResponseEntity.ok(
                new ApiResponseDto<>(
                        ApiResponseStatus.SUCCESS,
                        HttpStatus.OK,
                        categoryRepository.findAll()
                )
        );
    }

    @Override
    public boolean existsCategory(int id) {

        return categoryRepository.existsById(id);
    }

    @Override
    public Category getCategoryById(int id)
            throws CategoryNotFoundException {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id " + id
                        )
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> addNewCategory(
            CategoryRequestDto categoryRequestDto
    ) throws TransactionTypeNotFoundException,
            CategoryServiceLogicException,
            CategoryAlreadyExistsException {

        if (categoryRepository.existsByCategoryNameAndTransactionType(
                categoryRequestDto.getCategoryName(),
                transactionTypeService.getTransactionById(
                        categoryRequestDto.getTransactionTypeId()
                )
        )) {

            throw new CategoryAlreadyExistsException(
                    "Category already exists!"
            );
        }

        Category category = new Category(
                categoryRequestDto.getCategoryName(),
                transactionTypeService.getTransactionById(
                        categoryRequestDto.getTransactionTypeId()
                ),
                true
        );

        try {

            categoryRepository.save(category);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.CREATED,
                            "Category has been successfully added!"
                    ));

        } catch (Exception e) {

            log.error(
                    "Failed to add new category: {}",
                    e.getMessage()
            );

            throw new CategoryServiceLogicException(
                    "Failed to add new category: Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> updateCategory(
            int categoryId,
            CategoryRequestDto categoryRequestDto
    ) throws CategoryNotFoundException,
            TransactionTypeNotFoundException,
            CategoryServiceLogicException {

        Category category = getCategoryById(categoryId);

        category.setCategoryName(
                categoryRequestDto.getCategoryName()
        );

        category.setTransactionType(
                transactionTypeService.getTransactionById(
                        categoryRequestDto.getTransactionTypeId()
                )
        );

        try {

            categoryRepository.save(category);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.CREATED,
                            "Category has been successfully updated!"
                    ));

        } catch (Exception e) {

            log.error(
                    "Failed to update category: {}",
                    e.getMessage()
            );

            throw new CategoryServiceLogicException(
                    "Failed to update category: Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> enableOrDisableCategory(
            int categoryId
    ) throws CategoryServiceLogicException,
            CategoryNotFoundException {

        Category category = getCategoryById(categoryId);

        try {

            category.setEnabled(!category.isEnabled());

            categoryRepository.save(category);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            "Category has been updated successfully!"
                    ));

        } catch (Exception e) {

            log.error(
                    "Failed to enable/disable category: {}",
                    e.getMessage()
            );

            throw new CategoryServiceLogicException(
                    "Failed to update category: Try again later!"
            );
        }
    }
}
package backend.controllers;

import backend.dto.requests.CategoryRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.CategoryAlreadyExistsException;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.CategoryServiceLogicException;
import backend.exceptions.TransactionTypeNotFoundException;

import backend.services.CategoryService;
import backend.services.TransactionTypeService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mywallet/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionTypeService transactionTypeService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> getAllCategories() {

        return categoryService.getCategories();
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> addNewCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ) throws CategoryServiceLogicException,
            TransactionTypeNotFoundException,
            CategoryAlreadyExistsException {

        return categoryService.addNewCategory(categoryRequestDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> updateCategory(
            @RequestParam("categoryId") int categoryId,
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ) throws CategoryServiceLogicException,
            CategoryNotFoundException,
            TransactionTypeNotFoundException {

        return categoryService.updateCategory(categoryId, categoryRequestDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> disableOrEnableCategory(
            @RequestParam("categoryId") int categoryId
    ) throws CategoryServiceLogicException,
            CategoryNotFoundException {

        return categoryService.enableOrDisableCategory(categoryId);
    }
}
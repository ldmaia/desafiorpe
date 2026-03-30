package br.com.rpe.product.adapters.in.rest;

import br.com.rpe.product.application.port.in.CreateProductUseCase;
import br.com.rpe.product.application.port.in.GetProductByIdUseCase;
import br.com.rpe.product.application.port.in.UpdateProductStatusUseCase;
import br.com.rpe.product.domain.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final UpdateProductStatusUseCase updateProductStatusUseCase;
    private final ProductRestMapper productRestMapper;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            UpdateProductStatusUseCase updateProductStatusUseCase,
            ProductRestMapper productRestMapper
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.updateProductStatusUseCase = updateProductStatusUseCase;
        this.productRestMapper = productRestMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create product")
    @ApiResponse(responseCode = "201", description = "Product created")
    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema()))
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        Product product = createProductUseCase.create(new CreateProductUseCase.Command(
                request.name(),
                request.status()
        ));
        return productRestMapper.toResponse(product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema()))
    public ProductResponse getById(
            @Parameter(
                    description = "Product identifier",
                    required = true,
                    example = "8c7e7b8a-4c6c-4d27-9a9c-7f2f0d4d9c11",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("id") UUID id
    ) {
        return productRestMapper.toResponse(getProductByIdUseCase.getById(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update product status")
    public ProductResponse updateStatus(
            @Parameter(
                    description = "Product identifier",
                    required = true,
                    example = "8c7e7b8a-4c6c-4d27-9a9c-7f2f0d4d9c11",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateProductStatusRequest request
    ) {
        return productRestMapper.toResponse(updateProductStatusUseCase.updateStatus(
                id,
                new UpdateProductStatusUseCase.Command(request.status())
        ));
    }
}

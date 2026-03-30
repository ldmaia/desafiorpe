package br.com.rpe.product.application.service;

import br.com.rpe.product.application.port.in.CreateProductUseCase;
import br.com.rpe.product.application.port.in.GetProductByIdUseCase;
import br.com.rpe.product.application.port.in.UpdateProductStatusUseCase;
import br.com.rpe.product.application.port.out.ProductRepository;
import br.com.rpe.product.domain.exception.ProductNotFoundException;
import br.com.rpe.product.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProductService implements CreateProductUseCase, GetProductByIdUseCase, UpdateProductStatusUseCase {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(CreateProductUseCase.Command command) {
        return productRepository.save(buildNewProduct(command));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product updateStatus(UUID id, UpdateProductStatusUseCase.Command command) {
        Product existing = getById(id);
        return productRepository.save(buildUpdatedProduct(existing, command));
    }

    private Product buildNewProduct(CreateProductUseCase.Command command) {
        return new Product(
                null,
                command.name(),
                command.status(),
                null,
                null
        );
    }

    private Product buildUpdatedProduct(Product existing, UpdateProductStatusUseCase.Command command) {
        return new Product(
                existing.id(),
                existing.name(),
                command.status(),
                existing.createdAt(),
                existing.updatedAt()
        );
    }
}

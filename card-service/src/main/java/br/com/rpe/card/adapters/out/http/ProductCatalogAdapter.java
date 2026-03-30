package br.com.rpe.card.adapters.out.http;

import br.com.rpe.card.application.port.out.ProductCatalogPort;
import br.com.rpe.card.domain.exception.ProductUnavailableException;
import br.com.rpe.card.domain.model.ProductSnapshot;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductCatalogAdapter implements ProductCatalogPort {

    private final ProductServiceFeignClient feignClient;
    private final ProductServiceMapper productServiceMapper;

    public ProductCatalogAdapter(ProductServiceFeignClient feignClient, ProductServiceMapper productServiceMapper) {
        this.feignClient = feignClient;
        this.productServiceMapper = productServiceMapper;
    }

    @Override
    public ProductSnapshot getById(UUID productId) {
        try {
            return fetchProduct(productId);
        } catch (FeignException.NotFound exception) {
            throw new ProductUnavailableException("Produto informado nao existe.");
        } catch (FeignException exception) {
            throw new ProductUnavailableException("Nao foi possivel consultar o produto no momento.");
        }
    }

    private ProductSnapshot fetchProduct(UUID productId) {
        return productServiceMapper.toSnapshot(feignClient.getById(productId));
    }
}

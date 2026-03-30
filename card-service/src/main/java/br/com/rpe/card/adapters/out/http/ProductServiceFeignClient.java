package br.com.rpe.card.adapters.out.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service", url = "${app.clients.product-service.base-url}")
public interface ProductServiceFeignClient {

    @GetMapping("/api/v1/products/{id}")
    ProductServiceResponse getById(@PathVariable("id") UUID id);
}

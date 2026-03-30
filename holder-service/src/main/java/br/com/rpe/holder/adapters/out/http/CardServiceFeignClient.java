package br.com.rpe.holder.adapters.out.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "card-service",
        url = "${app.clients.card-service.base-url}"
)
public interface CardServiceFeignClient {

    @GetMapping("/api/v1/cards/by-holder/{holderId}")
    CardServiceResponse getByHolderId(@PathVariable("holderId") UUID holderId);
}

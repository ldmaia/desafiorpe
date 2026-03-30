package br.com.rpe.holder.adapters.out.http;

import br.com.rpe.holder.application.port.out.CardQueryPort;
import br.com.rpe.holder.domain.exception.AggregationUnavailableException;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CardServiceClient implements CardQueryPort {

    private final CardServiceFeignClient feignClient;
    private final CardServiceMapper cardServiceMapper;

    public CardServiceClient(CardServiceFeignClient feignClient, CardServiceMapper cardServiceMapper) {
        this.feignClient = feignClient;
        this.cardServiceMapper = cardServiceMapper;
    }

    @Override
    public Optional<CardProjection> findByHolderId(UUID holderId) {
        try {
            return findProjection(holderId);
        } catch (FeignException.NotFound exception) {
            return Optional.empty();
        } catch (FeignException exception) {
            throw new AggregationUnavailableException("Nao foi possivel consultar o cartao no momento.");
        }
    }

    private Optional<CardProjection> findProjection(UUID holderId) {
        CardServiceResponse response = feignClient.getByHolderId(holderId);
        return Optional.of(cardServiceMapper.toProjection(response));
    }
}

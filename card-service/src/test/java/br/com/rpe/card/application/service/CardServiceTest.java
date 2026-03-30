package br.com.rpe.card.application.service;

import br.com.rpe.card.application.port.in.ProcessCardIssuanceUseCase;
import br.com.rpe.card.application.port.out.CardRepository;
import br.com.rpe.card.application.port.out.ProductCachePort;
import br.com.rpe.card.application.port.out.ProductCatalogPort;
import br.com.rpe.card.domain.exception.CardAlreadyExistsException;
import br.com.rpe.card.domain.model.Card;
import br.com.rpe.card.domain.model.ProductSnapshot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ProductCatalogPort productCatalogPort;

    @Mock
    private ProductCachePort productCachePort;

    @InjectMocks
    private CardService cardService;

    @Test
    void shouldCreateCardAndCacheProductWhenCacheMissOccurs() {
        UUID holderId = UUID.fromString("e405471a-b9a9-468c-bbc6-a4cebc14c2ff");
        UUID productId = UUID.randomUUID();
        ProcessCardIssuanceUseCase.Command command = new ProcessCardIssuanceUseCase.Command(
                holderId,
                productId,
                "Lucas Silva",
                "12345678901",
                LocalDate.of(1990, 5, 10)
        );
        ProductSnapshot productSnapshot = new ProductSnapshot(productId, "Black", "ATIVO");

        when(cardRepository.existsByHolderId(holderId)).thenReturn(false);
        when(productCachePort.findById(productId)).thenReturn(Optional.empty());
        when(productCatalogPort.getById(productId)).thenReturn(productSnapshot);

        cardService.process(command);

        verify(productCatalogPort).getById(productId);
        verify(productCachePort).save(productSnapshot);

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());
        Card cardToPersist = cardCaptor.getValue();
        assertThat(cardToPersist.id()).isNull();
        assertThat(cardToPersist.holderId()).isEqualTo(holderId);
        assertThat(cardToPersist.productId()).isEqualTo(productId);
        assertThat(cardToPersist.brand()).isEqualTo("VISA");
        assertThat(cardToPersist.maskedNumber()).endsWith("c2ff");
    }

    @Test
    void shouldRejectDuplicateCardIssuance() {
        UUID holderId = UUID.randomUUID();
        ProcessCardIssuanceUseCase.Command command = new ProcessCardIssuanceUseCase.Command(
                holderId,
                UUID.randomUUID(),
                "Lucas Silva",
                "12345678901",
                LocalDate.of(1990, 5, 10)
        );

        when(cardRepository.existsByHolderId(holderId)).thenReturn(true);

        assertThatThrownBy(() -> cardService.process(command))
                .isInstanceOf(CardAlreadyExistsException.class)
                .hasMessageContaining(holderId.toString());

        verify(productCatalogPort, never()).getById(any());
        verify(cardRepository, never()).save(any());
    }
}

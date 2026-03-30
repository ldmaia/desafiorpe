package br.com.rpe.holder.application.service;

import br.com.rpe.holder.application.port.in.CreateHolderUseCase;
import br.com.rpe.holder.application.port.out.CardIssuancePublisher;
import br.com.rpe.holder.application.port.out.CardQueryPort;
import br.com.rpe.holder.application.port.out.HolderRepository;
import br.com.rpe.holder.domain.exception.CpfAlreadyExistsException;
import br.com.rpe.holder.domain.model.Holder;
import br.com.rpe.holder.domain.model.HolderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolderServiceTest {

    @Mock
    private HolderRepository holderRepository;

    @Mock
    private CardIssuancePublisher cardIssuancePublisher;

    @Mock
    private CardQueryPort cardQueryPort;

    @InjectMocks
    private HolderService holderService;

    @Test
    void shouldPersistHolderAndPublishIssuanceRequest() {
        CreateHolderUseCase.Command command = new CreateHolderUseCase.Command(
                "Lucas Silva",
                "12345678901",
                LocalDate.of(1990, 5, 10),
                HolderStatus.ATIVO,
                UUID.randomUUID()
        );
        UUID holderId = UUID.randomUUID();
        Holder savedHolder = new Holder(
                holderId,
                command.name(),
                command.cpf(),
                command.birthDate(),
                command.status(),
                command.productId(),
                Instant.now(),
                Instant.now()
        );

        when(holderRepository.existsByCpf(command.cpf())).thenReturn(false);
        when(holderRepository.save(any(Holder.class))).thenReturn(savedHolder);

        Holder result = holderService.create(command);

        ArgumentCaptor<Holder> holderCaptor = ArgumentCaptor.forClass(Holder.class);
        verify(holderRepository).save(holderCaptor.capture());
        Holder holderToPersist = holderCaptor.getValue();
        assertThat(holderToPersist.id()).isNull();
        assertThat(holderToPersist.name()).isEqualTo(command.name());
        assertThat(holderToPersist.cpf()).isEqualTo(command.cpf());
        assertThat(holderToPersist.productId()).isEqualTo(command.productId());

        ArgumentCaptor<CardIssuancePublisher.CardIssuanceRequested> eventCaptor =
                ArgumentCaptor.forClass(CardIssuancePublisher.CardIssuanceRequested.class);
        verify(cardIssuancePublisher).publish(eventCaptor.capture());
        CardIssuancePublisher.CardIssuanceRequested publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.holderId()).isEqualTo(holderId);
        assertThat(publishedEvent.productId()).isEqualTo(command.productId());
        assertThat(publishedEvent.holderName()).isEqualTo(command.name());
        assertThat(publishedEvent.cpf()).isEqualTo(command.cpf());
        assertThat(publishedEvent.birthDate()).isEqualTo(command.birthDate());

        assertThat(result).isEqualTo(savedHolder);
    }

    @Test
    void shouldRejectDuplicateCpf() {
        CreateHolderUseCase.Command command = new CreateHolderUseCase.Command(
                "Lucas Silva",
                "12345678901",
                LocalDate.of(1990, 5, 10),
                HolderStatus.ATIVO,
                UUID.randomUUID()
        );

        when(holderRepository.existsByCpf(command.cpf())).thenReturn(true);

        assertThatThrownBy(() -> holderService.create(command))
                .isInstanceOf(CpfAlreadyExistsException.class)
                .hasMessageContaining(command.cpf());

        verify(holderRepository, never()).save(any());
        verify(cardIssuancePublisher, never()).publish(any());
    }
}

package br.com.rpe.card.adapters.in.sqs;

import br.com.rpe.card.application.port.in.ProcessCardIssuanceUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardIssuanceListenerTest {

    @Mock
    private ProcessCardIssuanceUseCase processCardIssuanceUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private CardIssuanceListener cardIssuanceListener;

    @BeforeEach
    void setUp() {
        cardIssuanceListener = new CardIssuanceListener(processCardIssuanceUseCase, objectMapper);
    }

    @Test
    void shouldDeserializeMessageAndDelegateToUseCase() throws Exception {
        CardIssuanceMessage message = new CardIssuanceMessage(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Lucas Silva",
                "12345678901",
                LocalDate.of(1990, 5, 10)
        );

        cardIssuanceListener.consume(objectMapper.writeValueAsString(message));

        ArgumentCaptor<ProcessCardIssuanceUseCase.Command> commandCaptor =
                ArgumentCaptor.forClass(ProcessCardIssuanceUseCase.Command.class);
        verify(processCardIssuanceUseCase).process(commandCaptor.capture());
        ProcessCardIssuanceUseCase.Command command = commandCaptor.getValue();
        assertThat(command.holderId()).isEqualTo(message.holderId());
        assertThat(command.productId()).isEqualTo(message.productId());
        assertThat(command.holderName()).isEqualTo(message.holderName());
        assertThat(command.cpf()).isEqualTo(message.cpf());
        assertThat(command.birthDate()).isEqualTo(message.birthDate());
    }
}

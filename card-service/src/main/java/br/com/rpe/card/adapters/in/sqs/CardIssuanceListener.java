package br.com.rpe.card.adapters.in.sqs;

import br.com.rpe.card.application.port.in.ProcessCardIssuanceUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CardIssuanceListener {

    private final ProcessCardIssuanceUseCase processCardIssuanceUseCase;
    private final ObjectMapper objectMapper;

    public CardIssuanceListener(ProcessCardIssuanceUseCase processCardIssuanceUseCase, ObjectMapper objectMapper) {
        this.processCardIssuanceUseCase = processCardIssuanceUseCase;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${app.sqs.card-issuance-queue}")
    public void consume(String payload) throws Exception {
        CardIssuanceMessage message = objectMapper.readValue(payload, CardIssuanceMessage.class);
        processCardIssuanceUseCase.process(new ProcessCardIssuanceUseCase.Command(
                message.holderId(),
                message.productId(),
                message.holderName(),
                message.cpf(),
                message.birthDate()
        ));
    }
}

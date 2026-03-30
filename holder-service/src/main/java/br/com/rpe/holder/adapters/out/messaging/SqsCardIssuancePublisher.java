package br.com.rpe.holder.adapters.out.messaging;

import br.com.rpe.holder.application.port.out.CardIssuancePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SqsCardIssuancePublisher implements CardIssuancePublisher {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;
    private final String queueName;

    public SqsCardIssuancePublisher(
            SqsTemplate sqsTemplate,
            ObjectMapper objectMapper,
            @Value("${app.sqs.card-issuance-queue}") String queueName
    ) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
        this.queueName = queueName;
    }

    @Override
    public void publish(CardIssuanceRequested event) {
        try {
            sqsTemplate.send(queueName, objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Nao foi possivel serializar a solicitacao de emissao do cartao.", exception);
        }
    }
}

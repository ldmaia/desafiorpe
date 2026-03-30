package br.com.rpe.card.integration;

import br.com.rpe.card.adapters.in.sqs.CardIssuanceListener;
import br.com.rpe.card.adapters.in.sqs.CardIssuanceMessage;
import br.com.rpe.card.adapters.out.persistence.CardJpaRepository;
import br.com.rpe.card.application.port.out.ProductCachePort;
import br.com.rpe.card.application.port.out.ProductCatalogPort;
import br.com.rpe.card.domain.model.ProductSnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CardQueueConsumptionIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CardIssuanceListener cardIssuanceListener;

    @Autowired
    private CardJpaRepository cardJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductCatalogPort productCatalogPort;

    @MockBean
    private ProductCachePort productCachePort;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        cardJpaRepository.deleteAll();
    }

    @Test
    void shouldConsumeQueueMessagePersistCardAndExposeApi() throws Exception {
        UUID holderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        ProductSnapshot productSnapshot = new ProductSnapshot(productId, "Black", "ATIVO");
        CardIssuanceMessage message = new CardIssuanceMessage(
                holderId,
                productId,
                "Lucas Silva",
                "12345678901",
                LocalDate.of(1990, 5, 10)
        );

        when(productCachePort.findById(any())).thenReturn(Optional.of(productSnapshot));

        cardIssuanceListener.consume(objectMapper.writeValueAsString(message));

        assertThat(cardJpaRepository.findByHolderId(holderId)).isPresent();

        given()
                .when()
                .get("/api/v1/cards/by-holder/{holderId}", holderId)
                .then()
                .statusCode(200)
                .body("holderId", org.hamcrest.Matchers.equalTo(holderId.toString()))
                .body("brand", org.hamcrest.Matchers.equalTo("VISA"))
                .body("status", org.hamcrest.Matchers.equalTo("ATIVO"))
                .body("product.id", org.hamcrest.Matchers.equalTo(productId.toString()))
                .body("product.name", org.hamcrest.Matchers.equalTo("Black"))
                .body("product.status", org.hamcrest.Matchers.equalTo("ATIVO"));
    }
}

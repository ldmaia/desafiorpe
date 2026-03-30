package br.com.rpe.holder.integration;

import br.com.rpe.holder.adapters.out.persistence.HolderJpaRepository;
import br.com.rpe.holder.application.port.out.CardIssuancePublisher;
import br.com.rpe.holder.application.port.out.CardQueryPort;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HolderApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private HolderJpaRepository holderJpaRepository;

    @MockBean
    private CardIssuancePublisher cardIssuancePublisher;

    @MockBean
    private CardQueryPort cardQueryPort;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        holderJpaRepository.deleteAll();
    }

    @Test
    void shouldAuthenticateAndCreateHolder() {
        String token = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "admin", "password", "admin123"))
                .when()
                .post("/api/v1/auth/token")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");

        String cpf = "12345678901";
        String holderId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "name", "Lucas Silva",
                        "cpf", cpf,
                        "birthDate", "1990-05-10",
                        "status", "ATIVO",
                        "productId", UUID.randomUUID().toString()
                ))
                .when()
                .post("/api/v1/holders")
                .then()
                .statusCode(201)
                .body("name", org.hamcrest.Matchers.equalTo("Lucas Silva"))
                .body("cpf", org.hamcrest.Matchers.equalTo(cpf))
                .body("status", org.hamcrest.Matchers.equalTo("ATIVO"))
                .extract()
                .path("id");

        assertThat(holderJpaRepository.existsByCpf(cpf)).isTrue();
        assertThat(holderJpaRepository.findById(UUID.fromString(holderId))).isPresent();

        ArgumentCaptor<CardIssuancePublisher.CardIssuanceRequested> eventCaptor =
                ArgumentCaptor.forClass(CardIssuancePublisher.CardIssuanceRequested.class);
        verify(cardIssuancePublisher).publish(eventCaptor.capture());
        CardIssuancePublisher.CardIssuanceRequested publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.holderId()).isEqualTo(UUID.fromString(holderId));
        assertThat(publishedEvent.cpf()).isEqualTo(cpf);
    }
}

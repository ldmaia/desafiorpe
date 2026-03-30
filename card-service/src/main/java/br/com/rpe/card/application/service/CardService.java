package br.com.rpe.card.application.service;

import br.com.rpe.card.application.port.in.GetCardByHolderIdUseCase;
import br.com.rpe.card.application.port.in.ProcessCardIssuanceUseCase;
import br.com.rpe.card.application.port.out.CardRepository;
import br.com.rpe.card.application.port.out.ProductCachePort;
import br.com.rpe.card.application.port.out.ProductCatalogPort;
import br.com.rpe.card.domain.exception.CardAlreadyExistsException;
import br.com.rpe.card.domain.exception.CardNotFoundException;
import br.com.rpe.card.domain.exception.ProductUnavailableException;
import br.com.rpe.card.domain.model.Card;
import br.com.rpe.card.domain.model.CardDetailsView;
import br.com.rpe.card.domain.model.CardStatus;
import br.com.rpe.card.domain.model.ProductSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CardService implements ProcessCardIssuanceUseCase, GetCardByHolderIdUseCase {

    private static final String DEFAULT_BRAND = "VISA";

    private final CardRepository cardRepository;
    private final ProductCatalogPort productCatalogPort;
    private final ProductCachePort productCachePort;

    public CardService(
            CardRepository cardRepository,
            ProductCatalogPort productCatalogPort,
            ProductCachePort productCachePort
    ) {
        this.cardRepository = cardRepository;
        this.productCatalogPort = productCatalogPort;
        this.productCachePort = productCachePort;
    }

    @Override
    public void process(ProcessCardIssuanceUseCase.Command command) {
        if (cardRepository.existsByHolderId(command.holderId())) {
            throw new CardAlreadyExistsException(command.holderId());
        }

        ProductSnapshot product = getProduct(command.productId());
        Card card = buildCard(command, product);
        cardRepository.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDetailsView getByHolderId(UUID holderId) {
        Card card = cardRepository.findByHolderId(holderId)
                .orElseThrow(() -> new CardNotFoundException(holderId));

        ProductSnapshot product = getProduct(card.productId());
        return buildCardDetails(card, product);
    }

    private ProductSnapshot getProduct(UUID productId) {
        ProductSnapshot product = productCachePort.findById(productId)
                .orElseGet(() -> fetchAndCacheProduct(productId));
        validateActiveProduct(product);
        return product;
    }

    private ProductSnapshot fetchAndCacheProduct(UUID productId) {
        ProductSnapshot product = productCatalogPort.getById(productId);
        productCachePort.save(product);
        return product;
    }

    private void validateActiveProduct(ProductSnapshot product) {
        if (!"ATIVO".equals(product.status())) {
            throw new ProductUnavailableException("Produto informado nao esta ativo para emissao.");
        }
    }

    private Card buildCard(ProcessCardIssuanceUseCase.Command command, ProductSnapshot product) {
        return new Card(
                null,
                command.holderId(),
                product.id(),
                generateMaskedNumber(command.holderId()),
                DEFAULT_BRAND,
                CardStatus.ATIVO,
                null,
                null
        );
    }

    private CardDetailsView buildCardDetails(Card card, ProductSnapshot product) {
        return new CardDetailsView(
                card.id(),
                card.holderId(),
                card.maskedNumber(),
                card.brand(),
                card.status(),
                product
        );
    }

    private String generateMaskedNumber(UUID holderId) {
        String digits = holderId.toString().replace("-", "");
        String suffix = digits.substring(digits.length() - 4);
        return "**** **** **** " + suffix;
    }
}

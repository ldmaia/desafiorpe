package br.com.rpe.card.adapters.in.rest;

import br.com.rpe.card.application.port.in.GetCardByHolderIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@Tag(name = "Cards")
public class CardController {

    private final GetCardByHolderIdUseCase getCardByHolderIdUseCase;
    private final CardRestMapper cardRestMapper;

    public CardController(GetCardByHolderIdUseCase getCardByHolderIdUseCase, CardRestMapper cardRestMapper) {
        this.getCardByHolderIdUseCase = getCardByHolderIdUseCase;
        this.cardRestMapper = cardRestMapper;
    }

    @GetMapping("/by-holder/{holderId}")
    @Operation(summary = "Busca cartao por holderId")
    public CardResponse getByHolderId(
            @Parameter(description = "Identificador do portador", required = true, example = "0d6b0c9d-6a93-4a34-bf7a-a3316f2f43f9")
            @PathVariable("holderId") UUID holderId
    ) {
        return cardRestMapper.toResponse(getCardByHolderIdUseCase.getByHolderId(holderId));
    }
}

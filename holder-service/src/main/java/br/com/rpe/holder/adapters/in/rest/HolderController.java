package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.application.port.in.CreateHolderUseCase;
import br.com.rpe.holder.application.port.in.GetAggregatedHolderUseCase;
import br.com.rpe.holder.application.port.in.GetHolderByIdUseCase;
import br.com.rpe.holder.domain.model.Holder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/holders")
@Tag(name = "Holders")
@SecurityRequirement(name = "bearerAuth")
public class HolderController {

    private final CreateHolderUseCase createHolderUseCase;
    private final GetHolderByIdUseCase getHolderByIdUseCase;
    private final GetAggregatedHolderUseCase getAggregatedHolderUseCase;
    private final HolderRestMapper holderRestMapper;

    public HolderController(
            CreateHolderUseCase createHolderUseCase,
            GetHolderByIdUseCase getHolderByIdUseCase,
            GetAggregatedHolderUseCase getAggregatedHolderUseCase,
            HolderRestMapper holderRestMapper
    ) {
        this.createHolderUseCase = createHolderUseCase;
        this.getHolderByIdUseCase = getHolderByIdUseCase;
        this.getAggregatedHolderUseCase = getAggregatedHolderUseCase;
        this.holderRestMapper = holderRestMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra portador e dispara solicitacao de emissao")
    public HolderResponse create(@Valid @RequestBody CreateHolderRequest request) {
        Holder holder = createHolderUseCase.create(new CreateHolderUseCase.Command(
                request.name(),
                request.cpf(),
                request.birthDate(),
                request.status(),
                request.productId()
        ));
        return holderRestMapper.toResponse(holder);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca portador por id")
    public HolderResponse getById(
            @Parameter(description = "Identificador do portador", required = true, example = "0d6b0c9d-6a93-4a34-bf7a-a3316f2f43f9")
            @PathVariable("id") UUID id
    ) {
        return holderRestMapper.toResponse(getHolderByIdUseCase.getById(id));
    }

    @GetMapping("/{id}/full")
    @Operation(summary = "Busca objeto agregado de portador, cartao e produto")
    public AggregatedHolderResponse getAggregated(
            @Parameter(description = "Identificador do portador", required = true, example = "0d6b0c9d-6a93-4a34-bf7a-a3316f2f43f9")
            @PathVariable("id") UUID id
    ) {
        return holderRestMapper.toAggregatedResponse(getAggregatedHolderUseCase.getAggregated(id));
    }
}

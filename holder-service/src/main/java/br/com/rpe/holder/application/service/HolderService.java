package br.com.rpe.holder.application.service;

import br.com.rpe.holder.application.port.in.CreateHolderUseCase;
import br.com.rpe.holder.application.port.in.GetAggregatedHolderUseCase;
import br.com.rpe.holder.application.port.in.GetHolderByIdUseCase;
import br.com.rpe.holder.application.port.out.CardIssuancePublisher;
import br.com.rpe.holder.application.port.out.CardQueryPort;
import br.com.rpe.holder.application.port.out.HolderRepository;
import br.com.rpe.holder.domain.exception.AggregationUnavailableException;
import br.com.rpe.holder.domain.exception.CpfAlreadyExistsException;
import br.com.rpe.holder.domain.exception.HolderNotFoundException;
import br.com.rpe.holder.domain.model.AggregatedHolderView;
import br.com.rpe.holder.domain.model.Holder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class HolderService implements CreateHolderUseCase, GetHolderByIdUseCase, GetAggregatedHolderUseCase {

    private final HolderRepository holderRepository;
    private final CardIssuancePublisher cardIssuancePublisher;
    private final CardQueryPort cardQueryPort;

    public HolderService(
            HolderRepository holderRepository,
            CardIssuancePublisher cardIssuancePublisher,
            CardQueryPort cardQueryPort
    ) {
        this.holderRepository = holderRepository;
        this.cardIssuancePublisher = cardIssuancePublisher;
        this.cardQueryPort = cardQueryPort;
    }

    @Override
    public Holder create(CreateHolderUseCase.Command command) {
        if (holderRepository.existsByCpf(command.cpf())) {
            throw new CpfAlreadyExistsException(command.cpf());
        }

        Holder saved = holderRepository.save(new Holder(
                null,
                command.name(),
                command.cpf(),
                command.birthDate(),
                command.status(),
                command.productId(),
                null,
                null
        ));

        cardIssuancePublisher.publish(new CardIssuancePublisher.CardIssuanceRequested(
                saved.id(),
                saved.productId(),
                saved.name(),
                saved.cpf(),
                saved.birthDate()
        ));

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Holder getById(UUID id) {
        return holderRepository.findById(id)
                .orElseThrow(() -> new HolderNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public AggregatedHolderView getAggregated(UUID holderId) {
        Holder holder = getById(holderId);
        CardQueryPort.CardProjection cardProjection = cardQueryPort.findByHolderId(holderId)
                .orElseThrow(() -> new AggregationUnavailableException("Cartao ainda nao encontrado para o portador informado."));

        return new AggregatedHolderView(
                holder.id(),
                holder.name(),
                holder.cpf(),
                holder.birthDate(),
                holder.status(),
                holder.productId(),
                new AggregatedHolderView.CardDetails(
                        cardProjection.cardId(),
                        cardProjection.maskedNumber(),
                        cardProjection.brand(),
                        cardProjection.status()
                ),
                cardProjection.product(),
                holder.createdAt(),
                holder.updatedAt()
        );
    }
}

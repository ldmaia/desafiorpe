package br.com.rpe.holder.adapters.out.persistence;

import br.com.rpe.holder.application.port.out.HolderRepository;
import br.com.rpe.holder.domain.model.Holder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class HolderRepositoryAdapter implements HolderRepository {

    private final HolderJpaRepository holderJpaRepository;
    private final HolderPersistenceMapper mapper;

    public HolderRepositoryAdapter(HolderJpaRepository holderJpaRepository, HolderPersistenceMapper mapper) {
        this.holderJpaRepository = holderJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Holder save(Holder holder) {
        return mapper.toDomain(holderJpaRepository.save(mapper.toEntity(holder)));
    }

    @Override
    public Optional<Holder> findById(UUID id) {
        return holderJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return holderJpaRepository.existsByCpf(cpf);
    }
}

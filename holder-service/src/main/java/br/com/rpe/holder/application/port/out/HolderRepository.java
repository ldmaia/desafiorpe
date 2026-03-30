package br.com.rpe.holder.application.port.out;

import br.com.rpe.holder.domain.model.Holder;

import java.util.Optional;
import java.util.UUID;

public interface HolderRepository {

    Holder save(Holder holder);

    Optional<Holder> findById(UUID id);

    boolean existsByCpf(String cpf);
}

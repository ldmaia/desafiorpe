package br.com.rpe.holder.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HolderJpaRepository extends JpaRepository<HolderJpaEntity, UUID> {

    boolean existsByCpf(String cpf);
}

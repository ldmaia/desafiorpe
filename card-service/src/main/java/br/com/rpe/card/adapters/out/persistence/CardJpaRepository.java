package br.com.rpe.card.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardJpaRepository extends JpaRepository<CardJpaEntity, UUID> {

    Optional<CardJpaEntity> findByHolderId(UUID holderId);

    boolean existsByHolderId(UUID holderId);
}

package br.com.rpe.holder.adapters.out.persistence;

import br.com.rpe.holder.domain.model.Holder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HolderPersistenceMapper {

    HolderJpaEntity toEntity(Holder holder);

    Holder toDomain(HolderJpaEntity entity);
}

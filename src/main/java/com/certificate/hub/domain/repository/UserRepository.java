package com.certificate.hub.domain.repository;

import com.certificate.hub.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}

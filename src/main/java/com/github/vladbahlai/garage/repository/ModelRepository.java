package com.github.vladbahlai.garage.repository;

import com.github.vladbahlai.garage.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    boolean existsByName(String name);

    Optional<Model> findByName(String name);

}

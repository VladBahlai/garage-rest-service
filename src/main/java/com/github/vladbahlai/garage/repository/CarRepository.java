package com.github.vladbahlai.garage.repository;

import com.github.vladbahlai.garage.model.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select car from Car car left join fetch car.categories")
    List<Car> findAllEagerFetch(Pageable pageable);

    @Query("select car from Car car left join fetch car.categories where car.id = ?1")
    Optional<Car> findByIdEagerFetch(Long id);

    @Query("select car from Car car left join fetch car.categories where car.brand.name = ?1 and car.year between ?2 and ?3 order by car.id")
    List<Car> findCarsByBrandNameAndYearRange(String brandName, LocalDate minYear, LocalDate maxYear, Pageable pageable);
}

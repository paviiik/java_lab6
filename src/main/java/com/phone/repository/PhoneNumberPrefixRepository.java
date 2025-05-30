package com.phone.repository;

import com.phone.model.PhoneNumberPrefix;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhoneNumberPrefixRepository extends JpaRepository<PhoneNumberPrefix, Long> {

    List<PhoneNumberPrefix> findByCountryCode(String countryCode);

    @Query("SELECT p FROM PhoneNumberPrefix p WHERE p.country.name = :name")
    List<PhoneNumberPrefix> findByCountryName(@Param("name") String name);

    boolean existsByPrefixAndCountryCode(String prefix, String countryCode);
}

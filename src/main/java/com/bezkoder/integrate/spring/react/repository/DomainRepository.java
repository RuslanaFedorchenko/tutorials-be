package com.bezkoder.integrate.spring.react.repository;

import com.bezkoder.integrate.spring.react.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Long> {
}

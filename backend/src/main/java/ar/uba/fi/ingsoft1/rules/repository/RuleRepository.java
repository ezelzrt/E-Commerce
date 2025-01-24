package ar.uba.fi.ingsoft1.rules.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository  extends JpaRepository<AbstracRule, Long> {

    List<AbstracRule> findByState(Boolean state);

}


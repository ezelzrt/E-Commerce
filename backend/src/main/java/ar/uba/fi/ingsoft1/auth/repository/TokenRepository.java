package ar.uba.fi.ingsoft1.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.uba.fi.ingsoft1.user.User;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllValidIsFalseOrRevokedIsFalseTokensByUser(User user);
}
package com.github.tainaluiz.pedidos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.tainaluiz.pedidos.domain.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Cidade obj WHERE obj.estado.id = :estadoId ORDER BY obj.nome")
	List<Cidade> findCidades(@Param("estadoId") Long estadoId);
}

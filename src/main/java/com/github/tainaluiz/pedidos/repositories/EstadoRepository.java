package com.github.tainaluiz.pedidos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.tainaluiz.pedidos.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

	@Transactional(readOnly = true)
	List<Estado> findAllByOrderByNome();

}

package com.credibanco.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credibanco.app.entities.Card;

public interface CardRepository extends JpaRepository<Card, String> {
	
}

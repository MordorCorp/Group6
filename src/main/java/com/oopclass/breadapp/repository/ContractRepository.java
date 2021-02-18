package com.oopclass.breadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.Contract;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

	//Contract findByEmail(String email);
}

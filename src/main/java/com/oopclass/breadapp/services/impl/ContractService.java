package com.oopclass.breadapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Contract;
import com.oopclass.breadapp.repository.ContractRepository;
import com.oopclass.breadapp.services.IContractService;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@Service
public class ContractService implements IContractService {
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Override
	public Contract save(Contract entity) {
		return contractRepository.save(entity);
	}

	@Override
	public Contract update(Contract entity) {
		return contractRepository.save(entity);
	}

	@Override
	public void delete(Contract entity) {
		contractRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		contractRepository.deleteById(id);
	}

	@Override
	public Contract find(Long id) {
		return contractRepository.findById(id).orElse(null);
	}

	@Override
	public List<Contract> findAll() {
		return contractRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Contract> contracts) {
		contractRepository.deleteInBatch(contracts);
	}
	
}

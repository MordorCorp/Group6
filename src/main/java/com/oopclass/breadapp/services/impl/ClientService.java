package com.oopclass.breadapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Client;
import com.oopclass.breadapp.repository.ClientRepository;
import com.oopclass.breadapp.services.IClientService;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@Service
public class ClientService implements IClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public Client save(Client entity) {
		return clientRepository.save(entity);
	}

	@Override
	public Client update(Client entity) {
		return clientRepository.save(entity);
	}

	@Override
	public void delete(Client entity) {
		clientRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		clientRepository.deleteById(id);
	}

	@Override
	public Client find(Long id) {
		return clientRepository.findById(id).orElse(null);
	}

	@Override
	public List<Client> findAll() {
		return clientRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Client> clients) {
		clientRepository.deleteInBatch(clients);
	}
	
}

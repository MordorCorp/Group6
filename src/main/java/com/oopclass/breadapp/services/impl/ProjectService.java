package com.oopclass.breadapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Project;
import com.oopclass.breadapp.repository.ProjectRepository;
import com.oopclass.breadapp.services.IProjectService;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@Service
public class ProjectService implements IProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public Project save(Project entity) {
		return projectRepository.save(entity);
	}

	@Override
	public Project update(Project entity) {
		return projectRepository.save(entity);
	}

	@Override
	public void delete(Project entity) {
		projectRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		projectRepository.deleteById(id);
	}

	@Override
	public Project find(Long id) {
		return projectRepository.findById(id).orElse(null);
	}

	@Override
	public List<Project> findAll() {
		return projectRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Project> projects) {
		projectRepository.deleteInBatch(projects);
	}
	
}

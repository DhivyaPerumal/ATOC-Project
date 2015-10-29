package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.repository.ProjectDAO;

@Service("ProjectService")
@Transactional
public class ProjectService {

	private static final Integer String = null;
	@Autowired
	public ProjectDAO projectDAO;

	public int getProjectFilterCount(Project project) {
		return projectDAO.getProjectFilterCount(project);
	}

	public List getPaginationList(Project project, int start, int limit) {
		return projectDAO.getPaginationList(project, start, limit);

	}

	public List getAllProjectList(Project project) {
		return projectDAO.getAllProjectList(project);
	}

	
	
	
	public void saveProject(Project project) {
		projectDAO.saveProject(project);
	}

	public Project saveProjectWithReturn(Project project) {
		return projectDAO.saveProjectWithReturn(project);
	}

	public EmailJob saveEmailJobWithReturn(EmailJob emailJob)
	{
		return projectDAO.saveEmailJobWithReturn(emailJob);
	}
	public void removeProject(Project project) {
		projectDAO.removeProject(project);

	}

	public Project getProjectnById(Integer Id) {
		return projectDAO.getProjectnById(Id);
	}
	
	public EmailJob getEmailJobById(Integer Id)
	{
		return projectDAO.getEmailJobById(Id);
	}

	public Project getProjectBySearchParam(Project project) {
		return projectDAO.getProjectBySearchParam(project);
	}
}

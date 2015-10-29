package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;






import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.SeleniumEclipseProject;
import com.bpa.qaproduct.entity.Role;
import com.bpa.qaproduct.entity.SeleniumProjectJar;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.repository.SeleniumEclipseProjectDAO;


@Service("SeleniumEclipseProjectService")
@Transactional
public class SeleniumEclipseProjectService {

	@Autowired
	public SeleniumEclipseProjectDAO seleniumEclipseProjectDAO;

	public SeleniumProjectJar getSeleniumProjectJarById(Integer Id) {
		return seleniumEclipseProjectDAO.getSeleniumProjectJarById(Id);
	}
	
	public void saveSeleniumProjectJar(SeleniumProjectJar seleniumProjectJar) {
		seleniumEclipseProjectDAO.saveSeleniumProjectJar(seleniumProjectJar);
	}
	public int getSeleniumProjectJarCount(SeleniumProjectJar seleniumProjectJar) {
		return seleniumEclipseProjectDAO.getSeleniumProjectJarCount(seleniumProjectJar);
	}
	public List getAllSeleniumProjectJarList(SeleniumProjectJar seleniumProjectJar) {
		return seleniumEclipseProjectDAO.getAllSeleniumProjectJarList(seleniumProjectJar);
	}
	public void saveSeleniumEclipseProject(SeleniumEclipseProject seleniumEclipseProject) {
		seleniumEclipseProjectDAO.saveSeleniumEclipseProject(seleniumEclipseProject);
	}
	public int getSeleniumEclipseProjectFilterCount(SeleniumEclipseProject seleniumEclipseProject) 
	{
		return seleniumEclipseProjectDAO.getSeleniumEclipseProjectFilterCount(seleniumEclipseProject);
	}
	public List getSeleniumEclipseProjectList(int start, int limit,
			SeleniumEclipseProject seleniumEclipseProject) 
	{
		return seleniumEclipseProjectDAO.getSeleniumEclipseProjectList(start,limit,seleniumEclipseProject);
	}
	public SeleniumEclipseProject getSeleniumEclipseProjectById(int projectJarId)
	{
		return seleniumEclipseProjectDAO.getSeleniumEclipseProjectById(projectJarId);
	}
	
	public SeleniumProjectJar getSeleniumEclipseJarById(int jarId)
	{
		return seleniumEclipseProjectDAO.getSeleniumEclipseJarById(jarId);
	}
	public SeleniumProjectJar saveConfigJar(SeleniumProjectJar seleniumProjectJar) {
		return seleniumEclipseProjectDAO.saveConfigJar(seleniumProjectJar);
	}
	public void removeSeleniumEclipseProject(SeleniumEclipseProject seleniumEclipseProject)
	{
		seleniumEclipseProjectDAO.removeSeleniumEclipseProject(seleniumEclipseProject);
	}
	public SeleniumProjectJar getJarBySearchParam(SeleniumProjectJar seleniumProjectJar) {
		return seleniumEclipseProjectDAO.getJarBySearchParam(seleniumProjectJar);
	}
	

}

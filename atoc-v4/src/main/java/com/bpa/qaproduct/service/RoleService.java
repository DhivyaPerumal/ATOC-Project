package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Role;
import com.bpa.qaproduct.repository.RoleDAO;

@Service("RoleService")
@Transactional
public class RoleService {

	@Autowired
	public RoleDAO roleDAO;

	public int getRoleFilterCount(Role role) {
		return roleDAO.getRoleFilterCount(role);
	}

	public List getRoleList(Role role,int start, int limit) {
		return roleDAO.getRoleList(role,start,limit);
	}

	public List getAllRoleList(Role role) {
		return roleDAO.getAllRoleList(role);
	}

	public void saveRole(Role role) {
		roleDAO.saveRole(role);
	}

	public void removeRole(Role role) {
		roleDAO.removeRole(role);
	}

	public Role getRoleById(Integer Id) {
		return roleDAO.getRoleById(Id);
	}

	public Role getRoleBySearchParam(Role role) {
		return roleDAO.getRoleBySearchParam(role);
	}	

}

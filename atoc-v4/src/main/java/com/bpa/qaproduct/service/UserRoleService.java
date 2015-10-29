package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.repository.UserRoleDAO;

@Service("UserRoleService")
@Transactional
public class UserRoleService {

	@Autowired
	public UserRoleDAO userRoleDAO;

	public int getUserRoleFilterCount(UserRole userRole) {
		return userRoleDAO.getUserRoleFilterCount(userRole);
	}

	public List getUserRoleList(UserRole userRole,int start,int limit) {
		return userRoleDAO.getUserRoleList(userRole,start,limit);
	}

	public List getAllUserRoleList(UserRole userRole) {
		return userRoleDAO.getAllUserRoleList(userRole);
	}

	public UserRole saveUserRole(UserRole userRole) {

		return userRoleDAO.saveUserRole(userRole);
	}

	public UserRole saveUserRoleWithReturn(UserRole userRole) {
		return userRoleDAO.saveUserRoleWithReturn(userRole);
	}

	public UserRole searchbyUser(UserRole userRole) {
		return userRoleDAO.searchbyUser(userRole);
	}

	public void removeUserRole(UserRole userRole) {
		userRoleDAO.removeUserRole(userRole);
	}

	public UserRole getUserRoleById(Integer Id) {
		return userRoleDAO.getUserRoleById(Id);
	}	

}

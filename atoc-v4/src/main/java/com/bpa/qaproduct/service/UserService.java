package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.repository.UserDAO;

@Service("UserService")
@Transactional
public class UserService {

	@Autowired
	public UserDAO userDAO;

	public int getUserFilterCount(User user) {
		return userDAO.getUserFilterCount(user);
	}

	public List getUserList(User user) {
		return userDAO.getUserList(user);
	}

	public int getUserRoleFilterCount(UserRole userRole) {
		return userDAO.getUserRoleFilterCount(userRole);
	}

	public List getUserRoleList(UserRole userRole,int start, int limit) {
		return userDAO.getUserRoleList(userRole,start,limit);
	}

	
	public List getAllUserList(User user) {
		return userDAO.getAllUserList(user);
	}

	public void saveUser(User user) {
		userDAO.saveUser(user);
	}

	public User saveUserObjWithReturn(User user) {
		return userDAO.saveUserObjWithReturn(user);
	}
    
	public EmailJob saveForgotEmailObject(EmailJob emailJob)
	{
		return userDAO.saveEmailJobWithReturnForgot(emailJob);
	}
	/*public EmailJob saveOrUpdateEmailJob(EmailJob emailJob, byte[] byteArray)
	{
		return userDAO.saveOrUpdateEmailJob(emailJob, byteArray);
	}*/
	
	public EmailJob saveOrUpdateEmailJob(EmailJob emailJob)
	{
		return userDAO.saveOrUpdateEmailJob(emailJob);
	}

	
	public User saveOnlyObj(User user) {
		return userDAO.saveOnlyObj(user);
	}

	public void removeUser(User user) {
		userDAO.removeUser(user);
	}

	public User getUserById(Integer Id) {
		return userDAO.getUserById(Id);
	}

	public User getUserBySearchParameter(User searchUser) {
		return userDAO.getUserBySearchParameter(searchUser);
	}
	
	}

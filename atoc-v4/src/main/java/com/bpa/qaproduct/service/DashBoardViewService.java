package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.DashBoardView;
import com.bpa.qaproduct.repository.DashBoardViewDAO;

@Service("DashBoardViewService")
@Transactional
public class DashBoardViewService {
	@Autowired
	public DashBoardViewDAO dashBoardViewDAO;

	public int getDashBoardViewFilterCount(DashBoardView dashBoardView) {
		return dashBoardViewDAO.getDashBoardViewFilterCount(dashBoardView);
	}

	

	public List getAllDashBoardViewList(DashBoardView dashBoardView) {
		return dashBoardViewDAO.getAllDashBoardViewList(dashBoardView);
	}

	public DashBoardView getDashBoardViewById(Integer Id) {
		return dashBoardViewDAO.getDashBoardViewById(Id);
	}
}

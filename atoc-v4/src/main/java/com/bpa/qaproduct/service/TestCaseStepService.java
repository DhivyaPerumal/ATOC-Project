package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestCaseStep;
import com.bpa.qaproduct.repository.TestCaseStepDAO;


@Service("TestCaseStepService")
@Transactional
public class TestCaseStepService {
	
	@Autowired
	public TestCaseStepDAO testCaseStepDAO;
	
	public int getTestCaseStepFilterCount(TestCaseStep testCaseStep){
		return testCaseStepDAO.getTestCaseStepFilterCount(testCaseStep);
	}
	public List getTestCaseStepList(TestCaseStep testCaseStep,int start,int limit)
	{
		return testCaseStepDAO.getTestCaseStepList( testCaseStep,start,limit);
	}
	public List getAllTestCaseStepList(TestCaseStep testCaseStep)
	{
		return testCaseStepDAO.getAllTestCaseStepList(testCaseStep);
	}
	public void saveTestCaseStep(TestCaseStep testCaseStep){
		testCaseStepDAO.saveTestCaseStep(testCaseStep);
	}
	
	public void removeTestCaseStep(TestCaseStep testCaseStep){
		testCaseStepDAO.removeTestCaseStep(testCaseStep);
	}
	public TestCaseStep getTestCaseStepById(Integer Id){
		return testCaseStepDAO.getTestCaseStepById(Id);
	}

}

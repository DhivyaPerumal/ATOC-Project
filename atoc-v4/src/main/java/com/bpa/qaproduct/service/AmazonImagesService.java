package com.bpa.qaproduct.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.AmazonImages;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.repository.AmazonImagesDAO;

@Service("AmazonImagesService")
@Transactional
public class AmazonImagesService {

	@Autowired
	private AmazonImagesDAO amazonImagesDAO;

	public AmazonImages getAmiDetailsByName(AmazonImages amazonImages) {

		return amazonImagesDAO.getAmiDetailsByName(amazonImages);
	}
	
	public AmazonImages getAmazonImgById(Integer Id) {
		return amazonImagesDAO.getAmazonImgById(Id);
	}
	
	public int getAmazonFilterCount(AmazonImages amazonImages) {
		return amazonImagesDAO.getAmazonFilterCount(amazonImages);
	}
	
	public List getAllAmazonImgList(AmazonImages amazonImages) {
		return amazonImagesDAO.getAllAmazonImgList(amazonImages);
	}
	
}

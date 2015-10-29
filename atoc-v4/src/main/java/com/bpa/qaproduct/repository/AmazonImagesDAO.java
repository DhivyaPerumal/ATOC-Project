package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.AmazonImages;
import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;

@Repository("AmazonImagesDAO")
public class AmazonImagesDAO extends QatAbstractDao {

	public AmazonImagesDAO() {
		super();
	}

	@Autowired
	private SessionFactory sessionFactory;

	private DetachedCriteria createAmazonImagesCriteria(AmazonImages amazonImages,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AmazonImages.class);
		
		return criteria;
	}

	public AmazonImages getAmiDetailsByName(AmazonImages amazonImages) {

		DetachedCriteria criteria = createAmazonImagesCriteria(amazonImages,"");
		if (amazonImages.getOSName() != null) {
			
			criteria.add(Restrictions.like("OSName",
					"%" + amazonImages.getOSName() + "%").ignoreCase());
		}
		if (amazonImages.getBrowser() != null) {
			criteria.add(Restrictions.like("browser","%" +  amazonImages.getBrowser() + "%")
					.ignoreCase());
		}
		if (amazonImages.getBrowserVersion() != null) {
			criteria.add(Restrictions.like("browserVersion",
					"%" +amazonImages.getBrowserVersion() + "%").ignoreCase());
		}
		return (AmazonImages) super.getUniqueObject(
				AmazonImages.class, criteria);
	}
	
	public AmazonImages getAmazonImgById(Integer Id) {
		return (AmazonImages) super.find(AmazonImages.class, Id);
	}
	
	public int getAmazonFilterCount(AmazonImages amazonImages) {
		DetachedCriteria criteria = createAmazonImagesCriteria(amazonImages,"");
		criteria.add(Restrictions.eq("amazonImagesId", amazonImages.getAmazonImagesId()));

		return super.getObjectListCount(AmazonImages.class, criteria);
	}

	public List getAllAmazonImgList(
			AmazonImages amazonImages) {
		DetachedCriteria criteria = createAmazonImagesCriteria(amazonImages,"");
		criteria.addOrder(Order.desc("amazonImagesId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getAllObjectList(AmazonImages.class, criteria);
	}
}

package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Project;

@Repository("EmailJobDAO")
public class EmailJobDAO extends QatAbstractDao {

	public EmailJobDAO() 
		 {
		super();
	}
	
	private DetachedCriteria createEmailPendingStatus(EmailJob emailJob,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailJob.class);
		
		
			logger.info("Email Status is not null"+emailJob.getEmailType());
			if(emailJob.getEmailType().equalsIgnoreCase("PROJECT CREATION")){
				logger.info("Email Status is Pending");
			
				logger.info("In last pending");
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working"+emailJob.getEmailType());
			 
			}
			
			
			if(emailJob.getEmailType().equalsIgnoreCase("FORGOT_PASSWORD_EMAIL")){
				logger.info("Email type is forgot password");
			
				logger.info("In last pending");
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working  for forgot password"+emailJob.getEmailType());
			 
			}
			if(emailJob.getEmailType().equalsIgnoreCase("REGISTRAIOTN REQUEST")){
				logger.info("Email type is Registration");
			
				
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working  for Registration request"+emailJob.getEmailType());
			 
			}
			
			
			
			if(emailJob.getEmailType().equalsIgnoreCase("REGISTRAIOTN_REQUEST_REJECT")){
				logger.info("Email type is Registration is Rejected");
			
				
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working  for Registration request Reject"+emailJob.getEmailType());
			 
			}
			
			
			if(emailJob.getEmailType().equalsIgnoreCase("REGISTRAIOTN_REQUEST_APPROVE")){
				logger.info("Email type is Registration is Approved");
			
				
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working  for Registration request Approved"+emailJob.getEmailType());
			 
			}
			
			if(emailJob.getEmailType().equalsIgnoreCase("ADD_ORG_LOGIN_CREDENTIALS")){
				logger.info("org User Registratation"+emailJob.getEmailType());
			
				
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working email type org user"+emailJob.getEmailType());
			 
			}
			if(emailJob.getEmailType().equalsIgnoreCase("SELENIUM_JAR_CREATION")){
				logger.info("Selenium jar creation"+emailJob.getEmailType());
			
				
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working email type org user"+emailJob.getEmailType());
			 
			}
			if(emailJob.getEmailType().equalsIgnoreCase("ADD_MASTER_USER")){
				logger.info("Selenium jar creation"+emailJob.getEmailType());
			
				
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			    logger.info("Email Status working email type org user"+emailJob.getEmailType());
			 
			}
	/*	if (emailJob.getEmailType() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("emailType", ""+emailJob.getEmailType()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("emailType",
						"%" + emailJob.getEmailType() + "%").ignoreCase());
			}
		}
		
		if (emailJob.getFrom() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("from", ""+emailJob.getFrom()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("from",
						"%" + emailJob.getFrom() + "%").ignoreCase());
			}
		}	
		
		if (emailJob.getTo() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("to", ""+emailJob.getTo()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("to",
						"%" + emailJob.getTo() + "%").ignoreCase());
			}
		}*/
		return criteria;
		
	}
	private DetachedCriteria createAllEmailJobType(EmailJob emailJob,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailJob.class);
		return criteria;
	}
		
		public List getAllEmailPendingStatus(EmailJob emailJob) {
			DetachedCriteria criteria = createEmailPendingStatus(emailJob,"");
			criteria.addOrder(Order.desc("emailjobid"));
			
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			return super.getAllObjectList(EmailJob.class, criteria);
		}
		
		
		public List getAllEmailPendingStatusWithoutType(EmailJob emailJob) {
			DetachedCriteria criteria = createAllEmailJobType(emailJob,"");
			criteria.addOrder(Order.desc("emailjobid"));
			
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			return super.getAllObjectList(EmailJob.class, criteria);
		}
		
		public void removeemailJobValue(EmailJob emailJob) {
			super.delete(emailJob);
		}


		public EmailJob getProjectnById(Integer Id) {
			return (EmailJob) super.find(EmailJob.class, Id);
		}
}

package com.bpa.qaproduct.repository;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;




import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.SeleniumEclipseProject;
import com.bpa.qaproduct.entity.SeleniumProjectJar;
import com.bpa.qaproduct.util.DateHelper;

@Repository("SeleniumEclipseProjectDAO")
public class SeleniumEclipseProjectDAO extends QatAbstractDao {

	public SeleniumEclipseProjectDAO() {
		super();
	}

	
	
	private DetachedCriteria createSeleniumProjectJarCriteria(
			SeleniumProjectJar seleniumProjectJar) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SeleniumProjectJar.class);
		
		return criteria;
	}

	public SeleniumProjectJar getSeleniumProjectJarById(Integer Id) {
		return (SeleniumProjectJar) super.find(SeleniumProjectJar.class, Id);
	}
	public void saveSeleniumProjectJar(SeleniumProjectJar seleniumProjectJar) {
		super.saveOrUpdate(seleniumProjectJar);
	}	
	public int getSeleniumProjectJarCount(SeleniumProjectJar seleniumProjectJar) {
		DetachedCriteria criteria = createSeleniumProjectJarCriteria(seleniumProjectJar);
		return super.getObjectListCount(SeleniumProjectJar.class, criteria);
	}

	public List getAllSeleniumProjectJarList(SeleniumProjectJar seleniumProjectJar) {
		DetachedCriteria criteria = createSeleniumProjectJarCriteria(seleniumProjectJar);
		return super.getAllObjectList(SeleniumProjectJar.class, criteria);
	}
	
	
	public void saveSeleniumEclipseProject(SeleniumEclipseProject seleniumEclipseProject) {
		super.saveOrUpdate(seleniumEclipseProject);
	}
	public SeleniumProjectJar saveConfigJar(SeleniumProjectJar seleniumProjectJar) {
		return (SeleniumProjectJar) super.saveOrUpdatewithReturn(seleniumProjectJar);
	}

	
	private DetachedCriteria createSeleniumEclipseProjectCriteria(SeleniumEclipseProject seleniumEclipseProject,String matchType) 
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(SeleniumEclipseProject.class);
		
		if (seleniumEclipseProject.getProjectName() != null) {
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("projectName", ""+seleniumEclipseProject.getProjectName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("projectName",
						"%" + seleniumEclipseProject.getProjectName() + "%").ignoreCase());
			}
			
		}
		
		if (seleniumEclipseProject.getCreatedOn() != null) {
			
			Date fromDate = DateHelper.getDateWithoutTime(seleniumEclipseProject.getCreatedOn());
			/*Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(searchProject.getCreatedOn()));
			criteria.add(Restrictions.ge("createdOn", fromDate));
			criteria.add(Restrictions.le("createdOn", toDate));*/
			criteria.add(Restrictions.eq("createdOn",new java.sql.Timestamp(fromDate.getTime())));
			//criteria.add(Restrictions.eq("createdOn", project.getCreatedOn()));
		}
		
		if (seleniumEclipseProject.getUpdatedOn() != null) {
			
			Date fromDate = DateHelper.getDateWithoutTime(seleniumEclipseProject.getUpdatedOn());
			Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(seleniumEclipseProject.getUpdatedOn()));
			criteria.add(Restrictions.ge("updatedOn", fromDate));
			criteria.add(Restrictions.le("updatedOn", toDate));
			
			//criteria.add(Restrictions.eq("createdOn", project.getCreatedOn()));
		}
		
		
		return criteria;
	}
	
	

	public int getSeleniumEclipseProjectFilterCount(SeleniumEclipseProject seleniumEclipseProject) 
	{
		
		DetachedCriteria criteria = createSeleniumEclipseProjectCriteria(seleniumEclipseProject,"");
		return super.getObjectListCount(SeleniumEclipseProject.class, criteria);
	}

	public List getSeleniumEclipseProjectList(int start, int limit,
			SeleniumEclipseProject seleniumEclipseProject) 
	{
		DetachedCriteria criteria = createSeleniumEclipseProjectCriteria(seleniumEclipseProject,"");
		criteria.addOrder(Order.desc("projectJarId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getObjectListByRangeByValue(SeleniumEclipseProject.class, criteria,
				start, limit);
	}

	

	public SeleniumEclipseProject getSeleniumEclipseProjectById(int projectJarId) 
	{
		return (SeleniumEclipseProject) super.find(SeleniumEclipseProject.class, projectJarId);
	}
	
	public SeleniumProjectJar getSeleniumEclipseJarById(int jarId) 
	{
		return (SeleniumProjectJar) super.find(SeleniumEclipseProject.class, jarId);
	}

	public void removeSeleniumEclipseProject(SeleniumEclipseProject seleniumEclipseProject) 
	{
		super.delete(seleniumEclipseProject);
	}
	public SeleniumProjectJar getJarBySearchParam(SeleniumProjectJar seleniumProjectJar) {
		DetachedCriteria criteria = createSeleniumProjectJarCriteria(seleniumProjectJar);
		if(seleniumProjectJar.getJarName() != null){
			criteria.add(Restrictions.eq("jarName", seleniumProjectJar.getJarName()).ignoreCase());
		}
		
		return (SeleniumProjectJar) super.getUniqueObject(SeleniumProjectJar.class,
				criteria);
	}
	

	
	
}

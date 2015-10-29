package com.bpa.qaproduct.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.util.DateHelper;

@Repository("ProjectDAO")
public class ProjectDAO extends QatAbstractDao {

	public ProjectDAO() {
		super();
	}

	private DetachedCriteria createProjectCriteria(Project project,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Project.class);
		if (project.getProjectName() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("projectName", ""+project.getProjectName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("projectName",
						"%" + project.getProjectName() + "%").ignoreCase());
			}
			
			
		}
		if (project.getProjectJarPath() != null) {
			criteria.add(Restrictions.like("projectJarPath",
					"%" + project.getProjectJarPath() + "%").ignoreCase());
		}
		if (project.getCreatedOn() != null) {
			
			Date fromDate = DateHelper.getDateWithoutTime(project.getCreatedOn());
			Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(project.getCreatedOn()));
			criteria.add(Restrictions.ge("createdOn", fromDate));
			criteria.add(Restrictions.le("createdOn", toDate));
			
			//criteria.add(Restrictions.eq("createdOn", project.getCreatedOn()));
		}
		if (project.getNotes() != null) {
			criteria.add(Restrictions.like("notes",
					"%" + project.getNotes() + "%").ignoreCase());
		}
		if (project.getStartDate() != null) {
			criteria.add(Restrictions.like("startDate",
					"%" + project.getStartDate() + "%").ignoreCase());
		}
		if (project.getEndDate() != null) {
			criteria.add(Restrictions.like("endDate",
					"%" + project.getEndDate() + "%").ignoreCase());
		}
		if (project.getIsActive() != null) {
			/*criteria.add(Restrictions.like("isActive",
					"%" + project.getIsActive() + "%").ignoreCase());*/
			
			criteria.add(Restrictions.eq("isActive", "" + project.getIsActive()
					+ ""));
		}
		if (project.getOrganization() != null)
		{
			if(project.getOrganization().getOrganizationId() != null)
			{
				criteria.add(Restrictions.eq("organization", project.getOrganization()));
			}
			else
			{
				if (project.getOrganization().getOrganizationName() != null)
				{
					if (matchType.equalsIgnoreCase("exact"))
					{
						criteria.createAlias("project.getOrganization()","organization")
						.add(Restrictions.eq("organization.organizationName",""+project.getOrganization().getOrganizationName()+"").ignoreCase());
					}
					else
					{
						criteria.createAlias("project.getOrganization()","organization")
						.add(Restrictions.like("organization.organizationName","%"+project.getOrganization().getOrganizationName()+"%").ignoreCase());
					}
					
				}
			}
		}

		return criteria;

	}

	public int getProjectFilterCount(Project project) {
		DetachedCriteria criteria = createProjectCriteria(project,"");
		criteria.add(Restrictions.eq("organization", project.getOrganization()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("testSuites", FetchMode.SELECT);
		return super.getObjectListCount(Project.class, criteria);

	}

	public List getAllProjectList(Project project) {
		DetachedCriteria criteria = createProjectCriteria(project,"");
		criteria.addOrder(Order.desc("projectId"));
		criteria.add(Restrictions.eq("organization", project.getOrganization()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("testSuites", FetchMode.SELECT);
		return super.getAllObjectList(Project.class, criteria);
	}
	
	
	

	public List getPaginationList(Project project, int start, int limit) {
		DetachedCriteria criteria = createProjectCriteria(project,"");
		criteria.addOrder(Order.asc("projectId"));
		criteria.add(Restrictions.eq("organization", project.getOrganization()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("testSuites", FetchMode.SELECT); // We expect few projects will not have test suites so we are using select.
		return super.getObjectListByRangeByValue(Project.class, criteria,
				start, limit);

	}
	
	

	public void saveProject(Project project) {
		super.saveOrUpdate(project);
	}

	public Project saveProjectWithReturn(Project project) {
		return (Project) super.saveOrUpdatewithReturn(project);
	}

	public EmailJob saveEmailJobWithReturn(EmailJob emailJob)
	{
		return (EmailJob) super.saveOrUpdate(emailJob);
	}
	public void removeProject(Project project) {
		super.delete(project);
	}

	public Project getProjectnById(Integer Id) {
		return (Project) super.find(Project.class, Id);
	}
	
	public EmailJob getEmailJobById(Integer Id)
	{
		return (EmailJob) super.find(EmailJob.class, Id);
	}
	public Project getProjectBySearchParam(Project project) {
		DetachedCriteria criteria = createProjectCriteria(project,"exact");
		return (Project) super.getUniqueObject(Project.class,
				criteria);
	}
		
}

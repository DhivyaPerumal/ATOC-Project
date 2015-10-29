package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.OrganizationContactDetail;

@Repository("OrganizationContactDetailDAO")
public class OrganizationContactDetailDAO extends QatAbstractDao {

	public OrganizationContactDetailDAO() {
		super();
	}

	private DetachedCriteria createOrganizationContactDetailCriteria(
			OrganizationContactDetail orContactDetail) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(OrganizationContactDetail.class);
		if (orContactDetail.getContactName() != null) {
			criteria.add(Restrictions.like("contactName",
					"%" + orContactDetail.getContactName() + "%").ignoreCase());
		}
		if (orContactDetail.getContactEmail() != null) {
			criteria.add(Restrictions.like("contactEmail",
					"%" + orContactDetail.getContactEmail() + "%").ignoreCase());
		}
		if (orContactDetail.getContactPhone() != null) {
			criteria.add(Restrictions.like("contactPhone",
					"%" + orContactDetail.getContactPhone() + "%").ignoreCase());
		}

		if (orContactDetail.getContactDesignation() != null) {
			criteria.add(Restrictions.like("contactDesignation",
					"%" + orContactDetail.getContactDesignation() + "%")
					.ignoreCase());
		}
		if (orContactDetail.getIsActive() != null) {
			/*criteria.add(Restrictions.like("isActive",
					"%" + orContactDetail.getIsActive() + "%").ignoreCase());*/
			criteria.add(Restrictions.eq("isActive", "" + orContactDetail.getIsActive()
					+ ""));
		}
		if (orContactDetail.getOrganization() != null) {
			criteria.add(Restrictions.like("organization",
					"%" + orContactDetail.getOrganization() + "%").ignoreCase());
		}
		return criteria;

	}

	public int getOrganizationContactDetailFilterCount(
			OrganizationContactDetail orContactDetail) {
		DetachedCriteria criteria = createOrganizationContactDetailCriteria(orContactDetail);
		return super.getObjectListCount(OrganizationContactDetail.class,
				criteria);
	}

	public List getOrganizationContactDetailList(
			OrganizationContactDetail orContactDetail,int start,int limit) {
		DetachedCriteria criteria = createOrganizationContactDetailCriteria(orContactDetail);
		criteria.addOrder(Order.desc("OrgContactDetailId"));
		return super.getObjectListByRangeByValue(OrganizationContactDetail.class, criteria, start, limit);
	}

	public List getAllOrganizationContactDetailList(
			OrganizationContactDetail orContactDetail) {
		DetachedCriteria criteria = createOrganizationContactDetailCriteria(orContactDetail);
		criteria.addOrder(Order.desc("OrgContactDetailId"));
		return super
				.getAllObjectList(OrganizationContactDetail.class, criteria);
	}

	public void saveOrganizationContactDetail(
			OrganizationContactDetail orContactDetail) {
		super.saveOrUpdate(orContactDetail);
	}

	public void removeOrganizationContactDetail(
			OrganizationContactDetail orContactDetail) {
		super.delete(orContactDetail);
	}

	public OrganizationContactDetail getOrganizationContactDetailById(Integer Id) {
		return (OrganizationContactDetail) super.find(
				OrganizationContactDetail.class, Id);
	}

}

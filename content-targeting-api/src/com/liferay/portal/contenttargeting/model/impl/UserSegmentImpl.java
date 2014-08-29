/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.contenttargeting.model.impl;

import com.liferay.portal.contenttargeting.api.model.Rule;
import com.liferay.portal.contenttargeting.model.RuleInstance;
import com.liferay.portal.contenttargeting.service.RuleInstanceLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;

import java.util.List;
import java.util.Locale;

/**
 * The extended model implementation for the UserSegment service. Represents a row in the &quot;ContentTargeting_UserSegment&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portal.contenttargeting.model.UserSegment} interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 */
public class UserSegmentImpl extends UserSegmentBaseImpl {

	public UserSegmentImpl() {
	}

	public long getAssetCategoryId(long groupId) {
		long assetCategoryId = getAssetCategoryId();

		try {
			Group group = GroupLocalServiceUtil.getGroup(groupId);

			if (group.isStagingGroup()) {
				AssetCategory liveAssetCategory =
					AssetCategoryLocalServiceUtil.getAssetCategory(
						assetCategoryId);

				AssetCategory stagingAssetCategory =
					AssetCategoryLocalServiceUtil.
						getAssetCategoryByUuidAndGroupId(
							liveAssetCategory.getUuid(), group.getGroupId());

				assetCategoryId = stagingAssetCategory.getCategoryId();
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Category can not be found for groupId " + groupId);
			}
		}

		return assetCategoryId;
	}

	public String getNameWithGroupName(Locale locale, long groupId) {
		String name = getName(locale);

		if (groupId != getGroupId()) {
			try {
				Group group = GroupLocalServiceUtil.getGroup(getGroupId());

				StringBundler sb = new StringBundler(5);

				sb.append(name);
				sb.append(StringPool.SPACE);
				sb.append(StringPool.OPEN_PARENTHESIS);
				sb.append(group.getDescriptiveName(locale));
				sb.append(StringPool.CLOSE_PARENTHESIS);

				name = sb.toString();
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Group can not be found for groupId " + getGroupId());
				}
			}
		}

		return name;
	}

	public List<RuleInstance> getRuleInstances() throws SystemException {
		return RuleInstanceLocalServiceUtil.getRuleInstances(
			getUserSegmentId());
	}

	public boolean isRuleEnabled(Rule rule) throws Exception {
		if (rule.isInstantiable()) {
			return true;
		}

		if (RuleInstanceLocalServiceUtil.getRuleInstancesCount(
				rule.getRuleKey(), getUserSegmentId()) > 0) {

			return false;
		}

		return true;
	}

	private static Log _log = LogFactoryUtil.getLog(UserSegmentImpl.class);

}
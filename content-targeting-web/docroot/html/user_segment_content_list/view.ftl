<#--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
-->

<#include "../init.ftl" />
<#include "../macros.ftl" />

<#if assetEntries?has_content>
	<#if portletDisplayTemplateHtml??>
		${portletDisplayTemplateHtml}
	<#else>
		<#list assetEntries as assetEntry>
			<div class="asset-entry">
				<@renderAssetEntry assetEntry=assetEntry displayStyle="abstracts" showEditLink=true />
			</div>
		</#list>
	</#if>
<#else>
	<div class="alert alert-info">
		<@liferay_ui["message"] key="there-is-no-content-categorized-for-your-user-segments" />
	</div>
</#if>
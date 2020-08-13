/**
 * Copyright (c) 2016 Xtivia, Inc. All rights reserved.
 *
 * This file is part of the Xtivia Services Framework (XSF) library.
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
package com.xtivia.sgdxp2.filter;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.xtivia.sgdxp2.annotation.OrgRole;
import com.xtivia.sgdxp2.exception.SgDxpRestException;

@Component(
	property = {
			"osgi.jaxrs.extension=true",
			"osgi.jaxrs.name=SgDxp2.Filter.OrgRole",
			"osgi.jaxrs.application.select=(type=sgdxp2)"
		},
	scope = ServiceScope.PROTOTYPE)
public class OrgRoleFilter extends AbstractSecurityFilter implements ContainerRequestFilter {

	@Override
	protected void annotationCheck() {
		final OrgRole annotation = getAnnotation(OrgRole.class);

		if (annotation != null) {
			boolean inOrgRole = false;

			final String organizationName = annotation.organization();
			final String[] roleNames = annotation.role();

			Organization foundOrganization = null;
			final User user = getUser();

			if (user != null) {
				try {
					final List<Organization> organizations = user.getOrganizations();

					for (final Organization organization : organizations) {
						if (organization.getName().equals(organizationName)) {
							foundOrganization = organization;
							break;
						}
					}

					if (foundOrganization != null) {
						final List<Role> roles = RoleLocalServiceUtil.getUserGroupRoles(user.getUserId(),
								foundOrganization.getGroupId());
						for (final Role role : roles) {
							if (Arrays.stream(roleNames).anyMatch(role.getName()::equals)) {
								inOrgRole = true;
							}
						}
					}
				} catch (final PortalException e) {
					// OK
				}
			}

			if (!inOrgRole) {
				_log.info("User does not have role '{}' for Organization {}", Arrays.toString(roleNames),
						organizationName);
				throw new SgDxpRestException("User does not have role '" + Arrays.toString(roleNames)
						+ "' for Organization " + organizationName, Status.BAD_REQUEST);
			}
		}
	}
}
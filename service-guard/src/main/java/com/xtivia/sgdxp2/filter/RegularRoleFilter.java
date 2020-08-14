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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.xtivia.sgdxp2.annotation.RegularRole;
import com.xtivia.sgdxp2.exception.SgDxpRestException;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Xtivia.SgDxp2.Filter.RegularRole",
		"osgi.jaxrs.application.select=(type=sgdxp2)"
	},
	scope = ServiceScope.PROTOTYPE)
public class RegularRoleFilter extends AbstractSecurityFilter implements ContainerRequestFilter {

	@Override
	protected void annotationCheck() {
		final RegularRole annotation = getAnnotation(RegularRole.class);

		if (annotation != null) {
			boolean inRole = false;

			final String[] roleNames = annotation.value();

			final User user = getUser();

			if (user != null) {
				try {
					final List<String> roleNameList = Arrays.asList(roleNames);
					for (final String roleName : roleNameList) {
						if (RoleLocalServiceUtil.hasUserRole(user.getUserId(), user.getCompanyId(), roleName, true)) {
							inRole = true;
						}
					}
				} catch (final PortalException e) {
					// OK
				}
			}

			if (!inRole) {
				_log.info("User does not have roles '{}'", Arrays.toString(roleNames));
				throw new SgDxpRestException("User does not have roles '" + Arrays.toString(roleNames),
						Status.BAD_REQUEST);
			}
		}
	}
}
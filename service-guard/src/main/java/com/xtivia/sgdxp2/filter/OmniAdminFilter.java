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

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.xtivia.sgdxp2.annotation.OmniAdmin;
import com.xtivia.sgdxp2.exception.SgDxpRestException;

@Component(
	property = {
			"osgi.jaxrs.extension=true",
			"osgi.jaxrs.name=Xtivia.SgDxp2.Filter.OmniAdin",
			"osgi.jaxrs.application.select=(type=sgdxp2)"
		},
	scope = ServiceScope.PROTOTYPE)
public class OmniAdminFilter extends AbstractSecurityFilter implements ContainerRequestFilter {

	@Override
	protected void annotationCheck() {
		final OmniAdmin annotation = getAnnotation(OmniAdmin.class);

		if (annotation != null) {
			boolean isOmniAdmin = false;

			final User user = getUser();

			if (user != null) {
				try {
					final PermissionChecker permissionChecker = PermissionCheckerFactoryUtil
							.create(PortalUtil.getUser(httpRequest));
					PermissionThreadLocal.setPermissionChecker(permissionChecker);

					isOmniAdmin = permissionChecker.isOmniadmin();
				} catch (final PortalException e) {
					// OK
				}
			}

			if (!isOmniAdmin) {
				_log.info("User is not an OmniAdmin");
				throw new SgDxpRestException("User is not an Omnidmin", Status.BAD_REQUEST);
			}
		}
	}
}
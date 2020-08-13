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

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.Validator;
import com.xtivia.sgdxp2.annotation.ResourceAuthorized;
import com.xtivia.sgdxp2.exception.SgDxpRestException;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=SgDxp2.Filter.ResourceAuthorized",
		"osgi.jaxrs.application.select=(type=sgdxp2)"
	},
	scope = ServiceScope.PROTOTYPE)
public class ResourceAuthorizedFilter extends AbstractSecurityFilter implements ContainerRequestFilter {

	private boolean hasAccess(ResourceAuthorized annotation) {
		boolean result = false;
		final String name = annotation.name();
		final String actionId = annotation.actionId();

		if (name != null && actionId != null) {
			final PermissionChecker permissionChecker = PermissionCheckerFactoryUtil.create(getUser());
			PermissionThreadLocal.setPermissionChecker(permissionChecker);

			try {
				result = permissionChecker.hasPermission(0L, name, 0L, actionId);
			} catch (final Exception e) {
				_log.error(e.getMessage(), e);
			}
		} else {
			_log.warn(
					"ResourceAuthorized filter configured with null resource name or action ID for class={}, method={}",
					resourceInfo.getResourceClass().getName(), resourceInfo.getResourceMethod().getName());
		}

		return result;
	}

	@Override
	protected void annotationCheck() {
		final ResourceAuthorized resourceAuthorized = getAnnotation(ResourceAuthorized.class);

		if (Validator.isNotNull(resourceAuthorized) && !hasAccess(resourceAuthorized)) {
			_log.warn("ResourceAuthorized filter blocked access for class={}, method={}",
					resourceInfo.getResourceClass().getName(), resourceInfo.getResourceMethod().getName());
			throw new SgDxpRestException("User does not have correct permissions", Status.FORBIDDEN);
		}
	}
}
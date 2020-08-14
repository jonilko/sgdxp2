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

import com.xtivia.sgdxp2.annotation.Authenticated;
import com.xtivia.sgdxp2.exception.SgDxpRestException;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Xtivia.SgDxp2.Filter.Authenticated",
		"osgi.jaxrs.application.select=(type=sgdxp2)"
	},
	scope = ServiceScope.PROTOTYPE)
public class AuthenticatedFilter extends AbstractSecurityFilter implements ContainerRequestFilter {

	@Override
	protected void annotationCheck() {
		if (getAnnotation(Authenticated.class) != null && getUser() == null) {
			_log.info("User is not authenticated");
			throw new SgDxpRestException("User is not authenticated", Status.UNAUTHORIZED);
		}
	}
}

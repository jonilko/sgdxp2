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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.liferay.portal.kernel.model.User;
import com.xtivia.sgdxp2.annotation.Authorized;
import com.xtivia.sgdxp2.core.SgDxpContext;
import com.xtivia.sgdxp2.core.SgDxpResourceContext;
import com.xtivia.sgdxp2.exception.SgDxpRestException;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Xtivia.SgDxp2.Filter.Authorized",
		"osgi.jaxrs.application.select=(type=sgdxp2)"
	},
	scope = ServiceScope.PROTOTYPE)
public class AuthorizedFilter extends AbstractSecurityFilter implements ContainerRequestFilter {

	@Override
	protected void annotationCheck() {
		final Authorized annotation = getAnnotation(Authorized.class);

		if (annotation != null) {
			boolean isAuthorized = false;
			final User user = getUser();
			if (user != null) {
				try {
					final SgDxpContext ctx = new SgDxpResourceContext(httpRequest, uriInfo.getPathParameters(),
							resourceInfo);

					final Method method = app.getClass().getMethod("authorize", SgDxpContext.class);
					isAuthorized = (boolean) method.invoke(app, ctx);
				} catch (final NoSuchMethodException | SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					_log.error("No authorize() method defined in Application for @Authorized annotation use");
					throw new SgDxpRestException(
							"No authorize() method defined in Application for @Authorized annotation use",
							Status.UNAUTHORIZED);
				}
			}

			if (!isAuthorized) {
				_log.info("User is not Authorized to execute method");
				throw new SgDxpRestException("User is not Authorized to execute method", Status.UNAUTHORIZED);
			}
		}
	}
}
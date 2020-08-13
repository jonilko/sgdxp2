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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.utils.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PortalUtil;

abstract class AbstractSecurityFilter implements ContainerRequestFilter {

	protected Logger _log = LoggerFactory.getLogger(getClass());

	@Context
	protected Application app;

	@Context
	protected HttpServletRequest httpRequest;

	@Context
	protected ResourceInfo resourceInfo;

	@Context
	protected UriInfo uriInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (_log.isDebugEnabled()) {
			_log.debug("Executing for class={}, method={}", resourceInfo.getResourceClass().getName(),
					resourceInfo.getResourceMethod().getName());
		}

		annotationCheck();

		if (_log.isDebugEnabled()) {
			_log.debug("Success for class={}, method={}", resourceInfo.getResourceClass().getName(),
					resourceInfo.getResourceMethod().getName());
		}
	}

	protected abstract void annotationCheck();

	@SuppressWarnings("unchecked")
	protected <T> T getAnnotation(Class<? extends Annotation> annotationClass) {
		Annotation annotation = resourceInfo.getResourceMethod().getAnnotation(annotationClass);

		if (annotation == null) {
			final Method interfaceMethod = AnnotationUtils.getAnnotatedMethod(annotationClass,
					resourceInfo.getResourceMethod());
			annotation = interfaceMethod.getAnnotation(annotationClass);
		}

		return (T) annotation;
	}

	protected User getUser() {
		User user = null;

		try {
			user = PortalUtil.getUser(httpRequest);
			if (user.isDefaultUser()) {
				user = null;
			}
		} catch (final PortalException e) {
			e.printStackTrace();
		}

		return user;
	}
}

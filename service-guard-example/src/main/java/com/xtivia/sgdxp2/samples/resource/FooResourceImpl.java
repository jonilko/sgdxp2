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
package com.xtivia.sgdxp2.samples.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.xtivia.sgdxp2.exception.SgDxpRestException;
import com.xtivia.sgdxp2.samples.resource.api.FooResourceApi;

@Component(
	property = {
		"osgi.jaxrs.resource=true",
		"osgi.jaxrs.name=SgDxp2.Sample.FooResource"
	},
	scope = ServiceScope.PROTOTYPE,
	service = FooResourceApi.class)
public class FooResourceImpl implements FooResourceApi {

	@Reference
	private UserLocalService _userLocalService;

	@Override
	public Response getAllFoos(SecurityContext securityContext, HttpServletRequest servletRequest,
			ServiceContext serviceContext, User user) {
		try {
			System.out.println("list all FOOS");
			System.out.println("Context User: " + user);
			System.out.println("Context ServiceContext: " + serviceContext);

			final StringBuilder result = new StringBuilder();

			for (final User user1 : _userLocalService.getUsers(-1, -1)) {
				result.append(user1.isDefaultUser() ? "Guest" : user1.getFullName());
				result.append("\n");
			}

			return Response.ok("{}", MediaType.APPLICATION_JSON).build();
		} catch (final SgDxpRestException e) {
			throw e;
		} catch (final Exception e) {
			throw new SgDxpRestException(e.getMessage(), e, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response getFoo(long fooId, SecurityContext securityContext, HttpServletRequest servletRequest,
			ServiceContext serviceContext, User user) {
		try {
			System.out.println("get fooId " + fooId);
			System.out.println("Context User: " + user);
			System.out.println("Context ServiceContext: " + serviceContext);

			return Response.ok("{}", MediaType.APPLICATION_JSON).build();
		} catch (final SgDxpRestException e) {
			throw e;
		} catch (final Exception e) {
			throw new SgDxpRestException(e.getMessage(), e, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}

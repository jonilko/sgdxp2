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
package com.xtivia.sgdxp2.samples.resource.api;

import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.xtivia.sgdxp2.annotation.Authenticated;
import com.xtivia.sgdxp2.annotation.Authorized;
import com.xtivia.sgdxp2.annotation.OmniAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/bars")
public interface BarResourceApi {
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Authenticated
	@OmniAdmin
	@Authorized
	@RequiresScope("READ")
	Response getAllBars(@Context SecurityContext securityContext, @Context HttpServletRequest servletRequest,
			@Context ServiceContext serviceContext, @Context User user);

	@GET
	@Path("/{barId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Authenticated
	@OmniAdmin
	@Authorized
	@RequiresScope("WRITE")
	Response getBar(@PathParam("barId") long barId, @Context SecurityContext securityContext, @Context HttpServletRequest servletRequest,
			@Context ServiceContext serviceContext, @Context User user);
}

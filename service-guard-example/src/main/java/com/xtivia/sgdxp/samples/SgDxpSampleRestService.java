/**
 * Copyright 2000-present Liferay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xtivia.sgdxp.samples;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.xtivia.sgdxp2.core.SgDxp2Authorizer;
import com.xtivia.sgdxp2.core.SgDxp2Context;

import java.util.Calendar;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Xtivia
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/sgdxp_samples",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Xtivia.SgDxp2.Sample",
		"auth.verifier.guest.allowed=true",
		"oauth2.scopechecker.type=none",
		"liferay.access.control.disable=true",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=jaxb-json)",
		"auth.verifier.auth.verifier.PortalSessionAuthVerifier.check.csrf.token=false"
	},
	service = Application.class)
public class SgDxpSampleRestService extends Application implements SgDxp2Authorizer {

	private static final Log _log = LogFactoryUtil.getLog(SgDxpSampleRestService.class);

	@Activate
	@Modified
	public void activate() {
		if (_log.isInfoEnabled()) {
			_log.info("SgDxpSampleRestService activated");
		}
	}

	/*
	 * A somewhat nonsensical method that simply authorizes based on whether or not the current minute is even A real
	 * world example would do something more meaningful using the values available in the suppplied context
	 */
	@Override
	public boolean authorize(SgDxp2Context context) {
		if (_log.isInfoEnabled()) {
			_log.info("authorize");
		}

		final Calendar now = Calendar.getInstance();

		if (now.get(Calendar.MINUTE) % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
}
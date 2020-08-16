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
package com.xtivia.sgdxp2.core;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SgDxp2BaseContext extends HashMap<Object, Object> implements SgDxp2Context {

	private static Logger _logger = LoggerFactory.getLogger(SgDxp2BaseContext.class);

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T find(String key) {
		final Object entity = get(key);

		if (entity != null) {
			try {
				return (T) entity;
			} catch (final ClassCastException e) {
				_logger.error("Class cast exception when accessing key=" + key, e);
			}
		}

		return null;
	}

	@Override
	public String toString() {
		forEach((k, v) -> {
			System.out.println("Key: " + k + ", Value: " + v);
		});

		return this.keySet().toString();
	}

}

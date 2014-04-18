/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *   Copyright (C) 2011 NIC Chile Research Labs, Universidad de Chile.
 *
 *   Adkintun is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Adkintun is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with Adkintun.  If not, see <http://www.gnu.org/licenses/>.
 */
package cl.niclabs.tscrypto.common.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Convenient log4j interface utility.
 * 
 * @author vramiro
 * @author fcifuentes
 */
public final class TSLogger {
	public final static Logger node = Logger.getLogger("cl.niclabs.tscrypto.node");
	public final static Logger sd = Logger.getLogger("cl.niclabs.threshsig.sd");
	public final static Logger keyDealer = Logger.getLogger("cl.niclabs.threshsig.keyDealer");
    public final static Logger methodCollector = Logger.getLogger("cl.niclabs.cb.methodCollector");

	static {
		init();
	}

	public static void init() {
		PropertyConfigurator.configure(System.getProperty("log4j.configuration"));

		if ((node == null) || (sd == null) || (keyDealer == null) || (methodCollector == null)) {
			new Error(
					"No configuration file for logging properties: log4j.configuration");
		}
	}

}

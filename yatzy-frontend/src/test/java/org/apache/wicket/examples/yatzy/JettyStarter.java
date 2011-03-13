package org.apache.wicket.examples.yatzy;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStarter {

	public static void main(String[] args) throws Exception {
		System.setProperty("wicket.configuration", "development");
		System.setProperty("yatzy.applicationContext", "/org/apache/wicket/examples/yatzy/applicationContext.xml");
		Server server = new Server(8080);
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
	}

}

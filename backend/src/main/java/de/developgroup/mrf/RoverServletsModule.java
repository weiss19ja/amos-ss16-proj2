package de.developgroup.mrf;

import com.google.inject.servlet.ServletModule;

import de.developgroup.mrf.server.servlet.RoverServlet;

public class RoverServletsModule extends ServletModule {
	@Override
	protected void configureServlets() {
		// bind all Servlets here
		serve("/rover").with(RoverServlet.class);
	}
}

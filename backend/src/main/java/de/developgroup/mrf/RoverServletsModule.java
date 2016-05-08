package de.developgroup.mrf;

import com.google.inject.servlet.ServletModule;
import de.developgroup.mrf.servlets.example.CameraServlet;
import de.developgroup.mrf.servlets.example.ExampleServlet;

public class RoverServletsModule extends ServletModule {
    @Override
    protected void configureServlets() {
        // bind all Servlets here
        serve("/example").with(ExampleServlet.class);
        serve("/camera").with(CameraServlet.class);
    }
}

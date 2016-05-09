package de.developgroup.mrf.controllers;

import com.google.inject.Singleton;

@Singleton
public class ExampleControllerImpl implements ExampleController {
    @Override
    public String handlePing(int sqn) {
        return "pong " + (sqn + 1);
    }
}

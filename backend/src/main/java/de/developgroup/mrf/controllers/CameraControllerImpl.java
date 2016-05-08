package de.developgroup.mrf.controllers;

import com.google.inject.Singleton;

@Singleton
public class CameraControllerImpl implements CameraController {
    @Override
    public String handlePing(int sqn) {
        return "Camera pong " + (sqn + 1);
    }
}

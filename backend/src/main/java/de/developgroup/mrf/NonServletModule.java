package de.developgroup.mrf;

import com.google.inject.AbstractModule;
import com.pi4j.io.gpio.GpioController;

import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerImpl;
import de.developgroup.mrf.rover.collision.CollisionControllerMock;
import de.developgroup.mrf.rover.gpio.GpioControllerMockProvider;
import de.developgroup.mrf.rover.gpio.GpioControllerProvider;
import de.developgroup.mrf.server.controller.*;
import de.developgroup.mrf.server.handler.*;
import de.developgroup.mrf.server.socket.RoverSocket;

public class NonServletModule extends AbstractModule {

	private boolean useMocks = false;

	public NonServletModule() {
	}

	public NonServletModule(boolean useMocks) {
		this.useMocks = useMocks;
	}

	@Override
	protected void configure() {
		// here you can list all bindings of non-servlet classes needed in the
		// backend

		bind(RoverHandler.class).to(RoverHandlerImpl.class);

		if (useMocks) {
			// use mocking classes that need no rover hardware
			bind(CollisionController.class).to(CollisionControllerMock.class);
			bind(DriveController.class).to(DriveControllerMock.class);
			bind(HeadController.class).to(HeadControllerMock.class);
			bind(GpioController.class).toProvider(
					GpioControllerMockProvider.class);
//			 bind(CameraSnapshotController.class).to(
//					 CameraSnapshotControllerMock.class);
			bind(CameraSnapshotController.class).to(
					CameraSnapshotControllerImpl.class);
		} else {
			// use actual classes with hardware control
			bind(CollisionController.class).to(CollisionControllerImpl.class);
			bind(DriveController.class).to(DriveControllerImpl.class);
			bind(HeadController.class).to(HeadControllerImpl.class);
			bind(GpioController.class).toProvider(GpioControllerProvider.class);
			bind(CameraSnapshotController.class).to(
					CameraSnapshotControllerImpl.class);
		}

		bind(LoggingCommunicationController.class).to(
				LoggingCommunicationControllerImpl.class);
		bind(NotificationHandler.class).to(NotificationHandlerImpl.class);
		bind(SingleDriverHandler.class).to(SingleDriverHandlerImpl.class);
		requestStaticInjection(RoverSocket.class);
		requestStaticInjection(Main.class);
	}
}

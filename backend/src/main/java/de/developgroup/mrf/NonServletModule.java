/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.pi4j.io.gpio.GpioController;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import de.developgroup.mrf.rover.collision.*;
import de.developgroup.mrf.rover.gpio.GpioControllerMockProvider;
import de.developgroup.mrf.rover.gpio.GpioControllerProvider;
import de.developgroup.mrf.rover.pcf8591.*;
import de.developgroup.mrf.server.controller.*;
import de.developgroup.mrf.server.handler.*;
import de.developgroup.mrf.server.socket.RoverSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class NonServletModule extends AbstractModule {

	private static Logger LOGGER = LoggerFactory.getLogger(NonServletModule.class);

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

			// use mocked IRSensors that do essentially nothing
			install(new FactoryModuleBuilder()
					.implement(IRSensor.class, IRSensorMock.class)
					.build(IRSensorFactory.class));

		} else {
			// use actual classes with hardware control
			bind(CollisionController.class).to(CollisionControllerImpl.class);
			bind(DriveController.class).to(DriveControllerImpl.class);
			bind(HeadController.class).to(HeadControllerImpl.class);
			bind(GpioController.class).toProvider(GpioControllerProvider.class);
			bind(CameraSnapshotController.class).to(
					CameraSnapshotControllerImpl.class);

			// acquire the i2c device for the PCF8591 a/d converter
			// this looks ugly but is the only way to get it into Guice
			try {
				bind(I2CDevice.class)
						.annotatedWith(PCF8591Device.class)
						.toInstance(I2CFactory
								.getInstance(I2CBus.BUS_1)
								.getDevice(0x48)
						);
			} catch (IOException e) {
				LOGGER.error("Fatal error while setting up Guice:");
				LOGGER.error("Failed to get i2c dev 0x48 as PCF8591 a/d converter");
			}
			bind(PCF8591ADConverter.class).to(PCF8591ADConverterImpl.class);

			// use actual IRSensorImpls that require hardware
			install(new FactoryModuleBuilder()
					.implement(IRSensor.class, IRSensorImpl.class)
					.build(IRSensorFactory.class));
		}

		bind(LoggingCommunicationController.class).to(
				LoggingCommunicationControllerImpl.class);
		bind(NotificationHandler.class).to(NotificationHandlerImpl.class);
		bind(SingleDriverHandler.class).to(SingleDriverHandlerImpl.class);

		requestStaticInjection(RoverSocket.class);
		requestStaticInjection(Main.class);
	}
}

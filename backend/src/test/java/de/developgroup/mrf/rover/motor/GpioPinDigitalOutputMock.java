/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.motor;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class GpioPinDigitalOutputMock implements GpioPinDigitalOutput {

	private PinState state = PinState.LOW;

	@Override
	public boolean isHigh() {
		return state == PinState.HIGH;
	}

	@Override
	public boolean isLow() {
		return state == PinState.LOW;
	}

	@Override
	public PinState getState() {
		return state;
	}

	@Override
	public boolean isState(PinState state) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GpioProvider getProvider() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pin getPin() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTag(Object tag) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getTag() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setProperty(String key, String value) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean hasProperty(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProperty(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, String> getProperties() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeProperty(String key) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void clearProperties() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void export(PinMode mode) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void export(PinMode mode, PinState defaultState) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void unexport() {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isExported() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMode(PinMode mode) {
		throw new UnsupportedOperationException();

	}

	@Override
	public PinMode getMode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isMode(PinMode mode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPullResistance(PinPullResistance resistance) {
		throw new UnsupportedOperationException();

	}

	@Override
	public PinPullResistance getPullResistance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPullResistance(PinPullResistance resistance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<GpioPinListener> getListeners() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(GpioPinListener... listener) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void addListener(List<? extends GpioPinListener> listeners) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean hasListener(GpioPinListener... listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeListener(GpioPinListener... listener) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void removeListener(List<? extends GpioPinListener> listeners) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void removeAllListeners() {
		throw new UnsupportedOperationException();

	}

	@Override
	public GpioPinShutdown getShutdownOptions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setShutdownOptions(GpioPinShutdown options) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setShutdownOptions(Boolean unexport) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setShutdownOptions(Boolean unexport, PinState state) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void high() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void low() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void toggle() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Future<?> blink(long delay) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> blink(long delay, PinState blinkState) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> blink(long delay, long duration) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> blink(long delay, long duration, PinState blinkState) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, Callable<Void> callback) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, boolean blocking) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, boolean blocking, Callable<Void> callback) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, PinState pulseState) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, PinState pulseState, boolean blocking) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(PinState state) {
		this.state = state;
	}

	@Override
	public void setState(boolean state) {
		throw new UnsupportedOperationException();
	}

}

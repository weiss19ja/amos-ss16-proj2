/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.Collection;

/**
 * Auto-generated class for mocking a GpioController in testing mode on developer PCs.
 * TODO can this be done by a mocking framework?
 */
public class GpioControllerMock implements GpioController {
    @Override
    public void export(PinMode pinMode, PinState pinState, GpioPin... gpioPins) {

    }

    @Override
    public void export(PinMode pinMode, GpioPin... gpioPins) {

    }

    @Override
    public boolean isExported(GpioPin... gpioPins) {
        return false;
    }

    @Override
    public void unexport(GpioPin... gpioPins) {

    }

    @Override
    public void unexportAll() {

    }

    @Override
    public void setMode(PinMode pinMode, GpioPin... gpioPins) {

    }

    @Override
    public PinMode getMode(GpioPin gpioPin) {
        return null;
    }

    @Override
    public boolean isMode(PinMode pinMode, GpioPin... gpioPins) {
        return false;
    }

    @Override
    public void setPullResistance(PinPullResistance pinPullResistance, GpioPin... gpioPins) {

    }

    @Override
    public PinPullResistance getPullResistance(GpioPin gpioPin) {
        return null;
    }

    @Override
    public boolean isPullResistance(PinPullResistance pinPullResistance, GpioPin... gpioPins) {
        return false;
    }

    @Override
    public void high(GpioPinDigitalOutput... gpioPinDigitalOutputs) {

    }

    @Override
    public boolean isHigh(GpioPinDigital... gpioPinDigitals) {
        return false;
    }

    @Override
    public void low(GpioPinDigitalOutput... gpioPinDigitalOutputs) {

    }

    @Override
    public boolean isLow(GpioPinDigital... gpioPinDigitals) {
        return false;
    }

    @Override
    public void setState(PinState pinState, GpioPinDigitalOutput... gpioPinDigitalOutputs) {

    }

    @Override
    public void setState(boolean b, GpioPinDigitalOutput... gpioPinDigitalOutputs) {

    }

    @Override
    public boolean isState(PinState pinState, GpioPinDigital... gpioPinDigitals) {
        return false;
    }

    @Override
    public PinState getState(GpioPinDigital gpioPinDigital) {
        return null;
    }

    @Override
    public void toggle(GpioPinDigitalOutput... gpioPinDigitalOutputs) {

    }

    @Override
    public void pulse(long l, GpioPinDigitalOutput... gpioPinDigitalOutputs) {

    }

    @Override
    public void setValue(double v, GpioPinAnalogOutput... gpioPinAnalogOutputs) {

    }

    @Override
    public double getValue(GpioPinAnalog gpioPinAnalog) {
        return 0;
    }

    @Override
    public void addListener(GpioPinListener gpioPinListener, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void addListener(GpioPinListener[] gpioPinListeners, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void removeListener(GpioPinListener gpioPinListener, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void removeListener(GpioPinListener[] gpioPinListeners, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void addTrigger(GpioTrigger gpioTrigger, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void addTrigger(GpioTrigger[] gpioTriggers, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void removeTrigger(GpioTrigger gpioTrigger, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void removeTrigger(GpioTrigger[] gpioTriggers, GpioPinInput... gpioPinInputs) {

    }

    @Override
    public void removeAllTriggers() {

    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider gpioProvider, Pin pin, String s, PinMode pinMode, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider gpioProvider, Pin pin, PinMode pinMode, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider gpioProvider, Pin pin, String s, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider gpioProvider, Pin pin, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String s, PinMode pinMode, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode pinMode, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String s, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider gpioProvider, Pin pin, String s, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider gpioProvider, Pin pin, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider gpioProvider, Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider gpioProvider, Pin pin) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String s, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, PinPullResistance pinPullResistance) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider gpioProvider, Pin pin, String s, PinState pinState) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider gpioProvider, Pin pin, PinState pinState) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider gpioProvider, Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider gpioProvider, Pin pin) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String s, PinState pinState) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, PinState pinState) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin) {
        return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider gpioProvider, Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider gpioProvider, Pin pin) {
        return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider gpioProvider, Pin pin, String s, double v) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider gpioProvider, Pin pin, double v) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider gpioProvider, Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider gpioProvider, Pin pin) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String s, double v) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, double v) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider gpioProvider, Pin pin, String s, int i) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider gpioProvider, Pin pin, int i) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider gpioProvider, Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider gpioProvider, Pin pin) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String s, int i) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, int i) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String s) {
        return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin) {
        return null;
    }

    @Override
    public GpioPin provisionPin(GpioProvider gpioProvider, Pin pin, String s, PinMode pinMode, PinState pinState) {
        return null;
    }

    @Override
    public GpioPin provisionPin(GpioProvider gpioProvider, Pin pin, String s, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPin provisionPin(GpioProvider gpioProvider, Pin pin, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPin provisionPin(Pin pin, String s, PinMode pinMode) {
        return null;
    }

    @Override
    public GpioPin provisionPin(Pin pin, PinMode pinMode) {
        return null;
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown gpioPinShutdown, GpioPin... gpioPins) {

    }

    @Override
    public void setShutdownOptions(Boolean aBoolean, GpioPin... gpioPins) {

    }

    @Override
    public void setShutdownOptions(Boolean aBoolean, PinState pinState, GpioPin... gpioPins) {

    }

    @Override
    public void setShutdownOptions(Boolean aBoolean, PinState pinState, PinPullResistance pinPullResistance, GpioPin... gpioPins) {

    }

    @Override
    public void setShutdownOptions(Boolean aBoolean, PinState pinState, PinPullResistance pinPullResistance, PinMode pinMode, GpioPin... gpioPins) {

    }

    @Override
    public Collection<GpioPin> getProvisionedPins() {
        return null;
    }

    @Override
    public void unprovisionPin(GpioPin... gpioPins) {

    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public void shutdown() {

    }
}

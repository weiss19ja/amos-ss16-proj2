package de.developgroup.mrf.rover.pcf8591;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Interface to tell Guice which exact I2CDevice should be injected into the PCF8591ADConverterImpl.
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface PCF8591Device {
}

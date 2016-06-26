/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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

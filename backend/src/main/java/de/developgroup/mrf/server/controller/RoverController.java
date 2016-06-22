/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import java.io.IOException;

public interface RoverController {

	void init() throws IOException;

	void close() throws IOException;

	String takeSnapshot() throws IOException;

	int openVideoStream() throws IOException;

	void setSpeed(int speed) throws IOException;

	void setTurnRate(int rate) throws IOException;

	void setPan(int pan) throws IOException;

	void setTilt(int tilt) throws IOException;

	void stop() throws IOException;

}
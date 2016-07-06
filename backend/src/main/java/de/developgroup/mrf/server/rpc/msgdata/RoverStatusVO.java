/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.rpc.msgdata;

public class RoverStatusVO {

	public Integer maxSpeedValue;
	public Integer currentDriverId;
	public Boolean isKillswitchEnabled;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RoverStatusVO that = (RoverStatusVO) o;

		if (maxSpeedValue != null ? !maxSpeedValue.equals(that.maxSpeedValue) : that.maxSpeedValue != null)
			return false;
		if (currentDriverId != null ? !currentDriverId.equals(that.currentDriverId) : that.currentDriverId != null)
			return false;
		return isKillswitchEnabled != null ? isKillswitchEnabled.equals(that.isKillswitchEnabled) : that.isKillswitchEnabled == null;

	}

	@Override
	public int hashCode() {
		int result = maxSpeedValue != null ? maxSpeedValue.hashCode() : 0;
		result = 31 * result + (currentDriverId != null ? currentDriverId.hashCode() : 0);
		result = 31 * result + (isKillswitchEnabled != null ? isKillswitchEnabled.hashCode() : 0);
		return result;
	}
}

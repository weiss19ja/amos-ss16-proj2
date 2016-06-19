package de.developgroup.mrf.server.rpc.msgdata;

public class RoverStatusVO {

	public Integer currentDriverId;
	public Boolean isKillswitchEnabled;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentDriverId == null) ? 0 : currentDriverId.hashCode());
		result = prime
				* result
				+ ((isKillswitchEnabled == null) ? 0 : isKillswitchEnabled
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoverStatusVO other = (RoverStatusVO) obj;
		if (currentDriverId == null) {
			if (other.currentDriverId != null)
				return false;
		} else if (!currentDriverId.equals(other.currentDriverId))
			return false;
		if (isKillswitchEnabled == null) {
			if (other.isKillswitchEnabled != null)
				return false;
		} else if (!isKillswitchEnabled.equals(other.isKillswitchEnabled))
			return false;
		return true;
	}

}

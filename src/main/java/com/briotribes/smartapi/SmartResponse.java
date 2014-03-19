package com.briotribes.smartapi;

import java.util.Map;

public class SmartResponse {

	private String responseid;

	private boolean isError = false;

	private String message;

	private Map response;

	public String getResponseid() {
		return responseid;
	}

	public void setResponseid(String responseid) {
		this.responseid = responseid;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map getResponse() {
		return response;
	}

	public void setResponse(Map response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "SmartResponse [responseid=" + responseid + ", isError="
				+ isError + ", message=" + message + ", response=" + response
				+ "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((responseid == null) ? 0 : responseid.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SmartResponse))
			return false;
		SmartResponse other = (SmartResponse) obj;
		if (responseid == null) {
			if (other.responseid != null)
				return false;
		} else if (!responseid.equals(other.responseid))
			return false;
		return true;
	}

}

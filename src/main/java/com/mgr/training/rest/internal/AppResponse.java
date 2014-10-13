package com.mgr.training.rest.internal;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;

public class AppResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	public Meta meta = new Meta();
	private Object result;

	public void setError(int code, String msg) {
		meta.setCode(code);
		meta.setError(msg);
		result = msg;
	}

	public void setError(int code, Throwable error) {
		this.setError(code, error.getMessage());
		meta.setErrorDetail(Throwables.getStackTraceAsString(error));
	}

	public void setError(int code, Object msg) {
		meta.setCode(code);
		result = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass()).add("result", result).add("meta", meta).toString();
	}

	private class Meta implements Serializable {
		private static final long serialVersionUID = 1L;
		private int code = HttpServletResponse.SC_OK;
		private String error;
		private String errorDetail;
		@JsonIgnore
		private transient Stopwatch stopwatch;

		public Meta() {
			this.stopwatch = Stopwatch.createStarted();
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getResponseTime() {
			return stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + " ms";
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getErrorDetail() {
			return errorDetail;
		}

		public void setErrorDetail(String errorDetail) {
			this.errorDetail = errorDetail;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this.getClass())
					.add("code", code)
					.add("responseTime", getResponseTime())
					.add("error", error)
					.add("errorDetail", errorDetail)
					.toString();
		}
	}
}

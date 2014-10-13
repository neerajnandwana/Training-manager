package com.mgr.training.util;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.google.common.base.Function;

public class Transforms {

	private Transforms() {
	}

	public static final Function<String, Metric> VALUE_TO_METRIC = new Function<String, Metric>() {
		@Override
		public Gauge<String> apply(final String value) {
			return new Gauge<String>() {
				@Override
				public String getValue() {
					return value;
				};
			};
		}
	};
}

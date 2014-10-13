package com.mgr.training.metrics.metricset;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mgr.training.util.Const;

/**
 * A set of gauges for the Hibernate getStatistics().
 */
@Singleton
public class HibernateStatisticsMetricSet implements MetricSet {
	private final Provider<Session> sessionProvider;

	@Inject
	public HibernateStatisticsMetricSet(Provider<Session> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	public Statistics currentSessionStatistics() {
		return sessionProvider.get().getSessionFactory().getStatistics();
	}

	@Override
	public Map<String, Metric> getMetrics() {
		final Map<String, Metric> gauges = new HashMap<String, Metric>();

		gauges.put("StartTime", new Gauge<String>() {
			@Override
			public String getValue() {
				Date date = new Date(currentSessionStatistics().getStartTime());
				return Const.ISO_DATETIME_FORMAT.format(date);
			}
		});
		gauges.put("ConnectCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getConnectCount();
			}
		});
		gauges.put("FlushCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getFlushCount();
			}
		});
		gauges.put("PrepareStatementCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getPrepareStatementCount();
			}
		});
		gauges.put("CloseStatementCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getCloseStatementCount();
			}
		});
		gauges.put("OptimisticFailureCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getOptimisticFailureCount();
			}
		});
		addCollectionStatistics(gauges);
		addEntityStatistics(gauges);
		addNaturalIdStatistics(gauges);
		addQueryStatistics(gauges);
		addSessionStatistics(gauges);
		addTransactionStatistics(gauges);
		return Collections.unmodifiableMap(gauges);
	}

	private void addTransactionStatistics(final Map<String, Metric> gauges) {
		gauges.put("TransactionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getTransactionCount();
			}
		});
		gauges.put("SuccessfulTransactionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getSuccessfulTransactionCount();
			}
		});
	}

	private void addSessionStatistics(final Map<String, Metric> gauges) {
		gauges.put("SessionCloseCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getSessionCloseCount();
			}
		});
		gauges.put("SessionOpenCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getSessionOpenCount();
			}
		});
	}

	private void addQueryStatistics(final Map<String, Metric> gauges) {
		gauges.put("Queries", new Gauge<String[]>() {
			@Override
			public String[] getValue() {
				return currentSessionStatistics().getQueries();
			}
		});
		gauges.put("QueryCacheHitCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getQueryCacheHitCount();
			}
		});
		gauges.put("QueryCacheMissCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getQueryCacheMissCount();
			}
		});
		gauges.put("QueryCachePutCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getQueryCachePutCount();
			}
		});
		gauges.put("QueryExecutionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getQueryExecutionCount();
			}
		});
		gauges.put("QueryExecutionMaxTime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getQueryExecutionMaxTime();
			}
		});
		gauges.put("QueryExecutionMaxTimeQueryString", new Gauge<String>() {
			@Override
			public String getValue() {
				return currentSessionStatistics().getQueryExecutionMaxTimeQueryString();
			}
		});
	}

	private void addNaturalIdStatistics(final Map<String, Metric> gauges) {
		gauges.put("NaturalIdCacheHitCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getNaturalIdCacheHitCount();
			}
		});
		gauges.put("NaturalIdCacheMissCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getNaturalIdCacheMissCount();
			}
		});
		gauges.put("NaturalIdCachePutCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getNaturalIdCachePutCount();
			}
		});
		gauges.put("NaturalIdQueryExecutionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getNaturalIdQueryExecutionCount();
			}
		});
		gauges.put("NaturalIdQueryExecutionMaxTime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getNaturalIdQueryExecutionMaxTime();
			}
		});
		gauges.put("NaturalIdQueryExecutionMaxTimeRegion", new Gauge<String>() {
			@Override
			public String getValue() {
				return currentSessionStatistics().getNaturalIdQueryExecutionMaxTimeRegion();
			}
		});
		gauges.put("NaturalIdCachePutCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getNaturalIdCachePutCount();
			}
		});
	}

	private void addEntityStatistics(final Map<String, Metric> gauges) {
		gauges.put("EntityNames", new Gauge<String[]>() {
			@Override
			public String[] getValue() {
				return currentSessionStatistics().getEntityNames();
			}
		});
		gauges.put("EntityDeleteCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getEntityDeleteCount();
			}
		});
		gauges.put("EntityFetchCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getEntityFetchCount();
			}
		});
		gauges.put("EntityInsertCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getEntityInsertCount();
			}
		});
		gauges.put("EntityLoadCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getEntityLoadCount();
			}
		});
		gauges.put("EntityUpdateCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getEntityUpdateCount();
			}
		});
	}

	private void addCollectionStatistics(final Map<String, Metric> gauges) {
		gauges.put("CollectionFetchCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getCollectionFetchCount();
			}
		});
		gauges.put("CollectionLoadCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getCollectionLoadCount();
			}
		});
		gauges.put("CollectionRecreateCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getCollectionRecreateCount();
			}
		});
		gauges.put("CollectionRemoveCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getCollectionRemoveCount();
			}
		});
		gauges.put("CollectionUpdateCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return currentSessionStatistics().getCollectionUpdateCount();
			}
		});
	}

}

package com.mgr.training.metrics;

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
 * A set of gauges for the Hibernate statistics.
 */
@Singleton
public class HibernateStatisticsMetricSet implements MetricSet {
	private final Provider<Session> sessionProvider;

	@Inject
	public HibernateStatisticsMetricSet(Provider<Session> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Override
	public Map<String, Metric> getMetrics() {
		final Map<String, Metric> gauges = new HashMap<String, Metric>();

		gauges.put("StartTime", new Gauge<String>() {
			@Override
			public String getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				Date date = new Date(statistics.getStartTime());
				return Const.ISO_DATETIME_FORMAT.format(date);
			}
		});
		gauges.put("ConnectCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getConnectCount();
			}
		});
		gauges.put("FlushCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getFlushCount();
			}
		});
		gauges.put("PrepareStatementCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getPrepareStatementCount();
			}
		});
		gauges.put("CloseStatementCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getCloseStatementCount();
			}
		});
		gauges.put("OptimisticFailureCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getOptimisticFailureCount();
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
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getTransactionCount();
			}
		});
		gauges.put("SuccessfulTransactionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getSuccessfulTransactionCount();
			}
		});
	}

	private void addSessionStatistics(final Map<String, Metric> gauges) {
		gauges.put("SessionCloseCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getSessionCloseCount();
			}
		});
		gauges.put("SessionOpenCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getSessionOpenCount();
			}
		});
	}

	private void addQueryStatistics(final Map<String, Metric> gauges) {
		gauges.put("Queries", new Gauge<String[]>() {
			@Override
			public String[] getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueries();
			}
		});
		gauges.put("QueryCacheHitCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueryCacheHitCount();
			}
		});
		gauges.put("QueryCacheMissCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueryCacheMissCount();
			}
		});
		gauges.put("QueryCachePutCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueryCachePutCount();
			}
		});
		gauges.put("QueryExecutionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueryExecutionCount();
			}
		});
		gauges.put("QueryExecutionMaxTime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueryExecutionMaxTime();
			}
		});
		gauges.put("QueryExecutionMaxTimeQueryString", new Gauge<String>() {
			@Override
			public String getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getQueryExecutionMaxTimeQueryString();
			}
		});
	}

	private void addNaturalIdStatistics(final Map<String, Metric> gauges) {
		gauges.put("NaturalIdCacheHitCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdCacheHitCount();
			}
		});
		gauges.put("NaturalIdCacheMissCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdCacheMissCount();
			}
		});
		gauges.put("NaturalIdCachePutCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdCachePutCount();
			}
		});
		gauges.put("NaturalIdQueryExecutionCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdQueryExecutionCount();
			}
		});
		gauges.put("NaturalIdQueryExecutionMaxTime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdQueryExecutionMaxTime();
			}
		});
		gauges.put("NaturalIdQueryExecutionMaxTimeRegion", new Gauge<String>() {
			@Override
			public String getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdQueryExecutionMaxTimeRegion();
			}
		});
		gauges.put("NaturalIdCachePutCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getNaturalIdCachePutCount();
			}
		});
	}

	private void addEntityStatistics(final Map<String, Metric> gauges) {
		gauges.put("EntityNames", new Gauge<String[]>() {
			@Override
			public String[] getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getEntityNames();
			}
		});
		gauges.put("EntityDeleteCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getEntityDeleteCount();
			}
		});
		gauges.put("EntityFetchCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getEntityFetchCount();
			}
		});
		gauges.put("EntityInsertCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getEntityInsertCount();
			}
		});
		gauges.put("EntityLoadCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getEntityLoadCount();
			}
		});
		gauges.put("EntityUpdateCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getEntityUpdateCount();
			}
		});
	}

	private void addCollectionStatistics(final Map<String, Metric> gauges) {
		gauges.put("CollectionFetchCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getCollectionFetchCount();
			}
		});
		gauges.put("CollectionLoadCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getCollectionLoadCount();
			}
		});
		gauges.put("CollectionRecreateCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getCollectionRecreateCount();
			}
		});
		gauges.put("CollectionRemoveCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getCollectionRemoveCount();
			}
		});
		gauges.put("CollectionUpdateCount", new Gauge<Long>() {
			@Override
			public Long getValue() {
				final Statistics statistics = sessionProvider.get().getSessionFactory().getStatistics();
				return statistics.getCollectionUpdateCount();
			}
		});
	}

}

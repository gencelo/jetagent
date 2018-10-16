package com.oguzhan.jetagent.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 * Created on Ekim, 2018
 *
 * @author pypyt
 */
@Service
public class LookupService {

	private static final String INDEX = "post";

	private final ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	public LookupService(ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public Aggregation getRoomCounts() {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(INDEX).addAggregation(AggregationBuilders.count("roomCount").field("roomCount")).build();

		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});

		return aggregations.get("roomCount");
	}

}

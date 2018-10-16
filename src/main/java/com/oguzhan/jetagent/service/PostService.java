package com.oguzhan.jetagent.service;

import com.oguzhan.jetagent.model.Post;
import com.oguzhan.jetagent.repository.PostRepository;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Slf4j
@Service
public class PostService {

	private static final String INDEX = "post";

	private final PostRepository postRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	public PostService(PostRepository postRepository, ElasticsearchTemplate elasticsearchTemplate) {
		this.postRepository = postRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public List<Post> getAllPost() {
		return postRepository.findAll();
	}

	public List<Post> searchAllField(String searchText, Long maxPrice, Long minPrice, Integer minSquareMeter, Integer maxSquareMeter,
			List<String> roomCounts) {

		//@formatter:off
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
				.withIndices(INDEX)
				.withPageable(new PageRequest(0, 30));
		//@formatter:on

		BoolQueryBuilder queryBuilder = boolQuery();

		if (!StringUtil.isNullOrEmpty(searchText)) {
			queryBuilder.must(queryStringQuery(searchText).defaultOperator(Operator.AND));
		}

		if (maxPrice != null | minPrice != null) {

			//@formatter:off
			queryBuilder.must(rangeQuery("price")
										.lte(maxPrice == null ? Long.MAX_VALUE : maxPrice).includeLower(false)
							   			.gte(minPrice == null ? 0L : minPrice).includeUpper(false));
			//@formatter:on
		}

		if (minSquareMeter != null | maxSquareMeter != null) {

			//@formatter:off
			queryBuilder.must(rangeQuery("squareMeter")
										.lte(maxSquareMeter == null ? Integer.MAX_VALUE : maxSquareMeter).includeLower(false)
							   			.gte(minSquareMeter == null ? 0 : minSquareMeter).includeUpper(false));
			//@formatter:on
		}

		if (roomCounts != null && roomCounts.size() > 0) {

			roomCounts.forEach(roomCount -> queryBuilder.should(queryStringQuery(roomCount).field("roomCount.keyword")));
		}

		SearchQuery searchQuery = builder.withQuery(queryBuilder).build();

		return elasticsearchTemplate.queryForList(searchQuery, Post.class);
	}

}

package com.oguzhan.jetagent.repository;

import com.oguzhan.jetagent.model.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on Ekim, 2018
 *
 * @author pypyt
 */
@Repository
public interface PostRepository extends ElasticsearchRepository<Post, String> {

	@Override
	List<Post> findAll();


}

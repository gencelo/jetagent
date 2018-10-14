package com.oguzhan.jetagent.controller;

import com.oguzhan.jetagent.model.Post;
import com.oguzhan.jetagent.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {

	private PostService postService;

	@GetMapping
	//@formatter:off
	private ResponseEntity<List<Post>> searchPostByText(@RequestParam(value = "q", required = false) String searchText,
													    @RequestParam(value = "min_price", required = false) Long minPrice,
														@RequestParam(value = "max_price", required = false) Long maxPrice,
														@RequestParam(value ="min_square", required = false) Integer minSquareMeter,
													    @RequestParam(value ="max_square", required = false) Integer maxSquareMeter,
														@RequestParam(value = "room_count", required = false) List<String> roomCounts){
	//@formatter:on

		final List<Post> posts = postService.searchAllField(searchText, maxPrice, minPrice, minSquareMeter, maxSquareMeter, roomCounts);
		return ResponseEntity.ok(posts);
	}

}

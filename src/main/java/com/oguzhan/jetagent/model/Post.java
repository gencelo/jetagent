package com.oguzhan.jetagent.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "post", type = "post")
public class Post {

	@Id
	private String id;

	private String title;

	private String price;

	private String roomCount;

	private String squareMeter;

	private String floor;

	private String coverPhotoAddress;

	private String location;

	private String date;
}

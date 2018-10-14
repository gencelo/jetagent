package com.oguzhan.jetagent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post", type = "post")
public class Post {

	@Id
	private String id;

	private String title;

	@Field(type = FieldType.Long)
	private Long price;

	private String currency;

	private String roomCount;

	@Field(type = FieldType.Integer)
	private Integer squareMeter;

	private String floor;

	private String coverPhotoAddress;

	private String location;

	private String date;
}

package com.oguzhan.jetagent.controller;

import com.oguzhan.jetagent.service.LookupService;
import lombok.AllArgsConstructor;
import org.elasticsearch.search.aggregations.Aggregation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on Ekim, 2018
 *
 * @author pypyt
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/lookup")
public class LookupController {

	private LookupService lookupService;

	@GetMapping("room-count")
	private Aggregation getRoomCounts() {

		final Aggregation roomCounts = lookupService.getRoomCounts();

		return roomCounts;

	}
}

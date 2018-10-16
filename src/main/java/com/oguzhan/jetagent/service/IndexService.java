package com.oguzhan.jetagent.service;

import com.oguzhan.jetagent.model.Post;
import com.oguzhan.jetagent.repository.PostRepository;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IndexService {

	private static final String URL = "https://www.emlakjet.com/satilik-konut";

	private final PostRepository postRepository;

	@Autowired
	public IndexService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	/**
	 * This method delete all data in elastic cluster afred index all post that generated from
	 * {@link #crawlPostFromUrl() crawlPostFromUrl} to the elastic
	 */
	@PostConstruct
	public void indexPosts() {

		final List<Post> posts = crawlPostFromUrl();

		postRepository.deleteAll();

		log.info("All data deleted from elastic");

		for (Post post : posts) {
			postRepository.save(post);
		}

		log.info("New data indexed to elastic");
	}

	/**
	 * @return a post list that generated from emlakjet web site using jsoup.
	 */
	private List<Post> crawlPostFromUrl() {

		List<Post> posts = new ArrayList<>();

		try {
			Document document = Jsoup.connect(URL).get();
			Elements elements = document.select(".row-listing-item");

			for (Element element : elements.subList(0, Math.min(30, elements.size()))) {

				final String priceWithCurrency = element.select(".price").text();

				//@formatter:off
				Post post = Post.builder()
						.id(getId(element))
						.title(element.select(".title").text())
						.roomCount(element.select(".room-count").text())
						.squareMeter(getSquareMeter(element.select(".square-meter").text()))
						.location(element.select(".location").text())
						.price(getPrice(priceWithCurrency))
						.currency(getCurrency(priceWithCurrency))
						.coverPhotoAddress(getCoverPhotoUrl(element))
						.date(element.select(".date").text())
						.floor(getFloor(element))
						.build();
				//@formatter:on
				posts.add(post);
			}
		}
		catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return posts;
	}

	private String getFloor(Element element) {

		final Elements properties = element.select(".properties");
		if (properties.size() > 0) {

			for (Element property : properties.get(0).children()) {

				final Elements elementsByAttribute = property.getElementsByAttribute("id-listing-apartment-floor");

				if (elementsByAttribute.size() > 0) {

					return elementsByAttribute.get(0).text();
				}
			}
		}

		return "";
	}

	private String getCoverPhotoUrl(Element element) {

		final Element coverImageElement = element.select(".cover-image").get(0);
		final String coverPhotoChild = coverImageElement.attr("style");

		if (!StringUtil.isNullOrEmpty(coverPhotoChild)) {

			try {

				return coverPhotoChild.substring(coverPhotoChild.indexOf("//"), coverPhotoChild.indexOf("')"));
			}
			catch (IndexOutOfBoundsException e) {

				log.error(e.getMessage(), e);
				return "";
			}
		}
		else {
			final String coverPhotoUrlLazyLoad = coverImageElement.attr("lazy-load");

			if (!StringUtil.isNullOrEmpty(coverPhotoUrlLazyLoad)) {
				return coverPhotoUrlLazyLoad;
			}
		}
		return "";
	}

	private String getId(Element element) {

		final Elements checkBox = element.select(".compare-checkbox");
		final String id = checkBox.attr("id");

		if (!StringUtil.isNullOrEmpty(id)) {

			return id.substring(id.lastIndexOf("_") + 1);
		}

		return String.valueOf(element.hashCode());
	}

	private Long getPrice(String priceWithCurrency) {

		if (priceWithCurrency == null)
			return 0L;

		return Long.parseLong(priceWithCurrency.replaceAll("\\D+", ""));
	}

	private String getCurrency(String priceWithCurrency) {

		if (priceWithCurrency == null) {
			return "Fiyat Sorun";
		}

		return priceWithCurrency.replaceAll("[^A-Za-z]+", "");
	}

	private Integer getSquareMeter(String squareMeterText) {

		if (squareMeterText == null) {
			return 0;
		}

		return Integer.parseInt(squareMeterText.replaceAll(" m2", ""));
	}

}

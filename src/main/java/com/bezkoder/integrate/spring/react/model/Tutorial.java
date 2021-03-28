package com.bezkoder.integrate.spring.react.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tutorials")
public class Tutorial {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "published")
	private boolean published;

	@Column(name = "link")
	private String link;

	@Column(name = "rating")
	private Integer rating;

	@OneToOne
	private Domain domain;

	@ManyToOne
	private Author author;


	public Tutorial(String title, String description, boolean published, Domain domain, String link, Integer rating, Author author) {
		this.title = title;
		this.description = description;
		this.published = published;
		this.domain = domain;
		this.link = link;
		this.rating = rating;
		this.author = author;
	}


	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean isPublished) {
		this.published = isPublished;
	}
}

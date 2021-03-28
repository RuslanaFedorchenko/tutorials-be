package com.bezkoder.integrate.spring.react.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "authors")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "degree")
	private String degree;

	@Column(name = "about")
	private String about;

	@OneToMany
	private List<Tutorial> tutorials;

	public Author(String name, String degree, String about) {
		this.name = name;
		this.degree = degree;
		this.about = about;
	}
}

package com.bezkoder.integrate.spring.react.controller;

import com.bezkoder.integrate.spring.react.model.Author;
import com.bezkoder.integrate.spring.react.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class AuthorController {

	@Autowired
	AuthorRepository authorRepository;

	@GetMapping("/authors")
	public ResponseEntity<List<Author>> getAllAuthors(@RequestParam(required = false) String title) {
		try {
			List<Author> authors = authorRepository.findAll();

			if (authors.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(authors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		try {
			Author newAuthor = authorRepository
					.save(new Author(author.getName(), author.getDegree(), author.getAbout()));
			return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/authors/{id}")
	public ResponseEntity<Author> updateAuthor(@PathVariable("id") long id, @RequestBody Author author) {
		Optional<Author> authorData = authorRepository.findById(id);

		if (authorData.isPresent()) {
			Author newAuthor = authorData.get();
			newAuthor.setAbout(author.getAbout());
			newAuthor.setDegree(author.getDegree());
			newAuthor.setName(author.getName());
			return new ResponseEntity<>(authorRepository.save(newAuthor), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}

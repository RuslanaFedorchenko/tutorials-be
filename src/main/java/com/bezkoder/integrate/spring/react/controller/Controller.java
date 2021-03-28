package com.bezkoder.integrate.spring.react.controller;

import com.bezkoder.integrate.spring.react.model.Author;
import com.bezkoder.integrate.spring.react.model.Domain;
import com.bezkoder.integrate.spring.react.model.Tutorial;
import com.bezkoder.integrate.spring.react.repository.AuthorRepository;
import com.bezkoder.integrate.spring.react.repository.DomainRepository;
import com.bezkoder.integrate.spring.react.repository.TutorialRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class Controller {

	private final TutorialRepository tutorialRepository;
	private final DomainRepository domainRepository;
	private final AuthorRepository authorRepository;


	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			if (title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		try {
			Domain domain = domainRepository.findById(tutorial.getDomain().getId()).get();
			Author author = authorRepository.findById(tutorial.getAuthor().getId()).get();
			Tutorial _tutorial = tutorialRepository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false, domain, tutorial.getLink(), tutorial.getRating(), author));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		Optional<Domain> domainData = domainRepository.findById(tutorial.getDomain().getId());

		if (tutorialData.isPresent() && domainData.isPresent()) {
			Tutorial newTutorial = tutorialData.get();
			newTutorial.setTitle(tutorial.getTitle());
			newTutorial.setDescription(tutorial.getDescription());
			newTutorial.setPublished(tutorial.isPublished());
			newTutorial.setLink(tutorial.getLink());
			newTutorial.setRating(tutorial.getRating());
			newTutorial.setDomain(domainData.get());
			return new ResponseEntity<>(tutorialRepository.save(newTutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/domains")
	public ResponseEntity<List<Domain>> getAllDomains() {

		try {
			List<Domain> domains = domainRepository.findAll();

			if (domains.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(domains, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/addAuthors")
	public String addAuthors() {
		final List<Author> authors = IntStream.range(0, 50)
				.mapToObj(num -> new Author(RandomStringUtils.randomAlphabetic(7), RandomStringUtils.randomAlphabetic(7), RandomStringUtils.randomAlphabetic(20)))
				.collect(Collectors.toList());
		authorRepository.saveAll(authors);
		return "OK";
	}

	@GetMapping("/addTutorials")
	public List<Tutorial> addTutorials() {
		final List<Author> authors = authorRepository.findAll();
		final List<Domain> domains = domainRepository.findAll();
		final List<Tutorial> tutorials = IntStream.range(0, 500)
				.mapToObj(num -> new Tutorial(RandomStringUtils.randomAlphabetic(15),
						RandomStringUtils.randomAlphabetic(35),
						RandomUtils.nextBoolean(),
						domains.get(RandomUtils.nextInt(1, 8)),
						"https://" + RandomStringUtils.randomAlphabetic(7) + ".com",
						RandomUtils.nextInt(1, 5),
						authors.get(RandomUtils.nextInt(1, 50))))
				.collect(Collectors.toList());
		return tutorialRepository.saveAll(tutorials);
	}
}

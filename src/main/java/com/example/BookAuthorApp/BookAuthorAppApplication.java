package com.example.BookAuthorApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class BookAuthorAppApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(BookAuthorAppApplication.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(BookAuthorAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Creating tables...");

		// Create the 'authors' table
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS authors (" +
				"id SERIAL PRIMARY KEY, " +
				"name VARCHAR(255) NOT NULL)");

		// Create the 'books' table
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS books (" +
				"id SERIAL PRIMARY KEY, " +
				"title VARCHAR(255) NOT NULL, " +
				"author_id INTEGER NOT NULL, " +
				"FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE)");

		// Insert sample authors
		jdbcTemplate.batchUpdate("INSERT INTO authors (name) VALUES (?)",
				List.of(new Object[]{"Author 1"}, new Object[]{"Author 2"}));

		// Insert sample books
		jdbcTemplate.batchUpdate("INSERT INTO books (title, author_id) VALUES (?, ?)",
				List.of(
						new Object[]{"Book 1", 1},
						new Object[]{"Book 2", 1},
						new Object[]{"Book 3", 2}
				));

		log.info("Tables created and sample data inserted.");
	}
}

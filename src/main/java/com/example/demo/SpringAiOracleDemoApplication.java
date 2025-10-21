package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot application for demonstrating Spring AI integration with Oracle Vector Database.
 * This application provides semantic search capabilities using OpenAI embeddings stored in Oracle Database.
 */
@SpringBootApplication
public class SpringAiOracleDemoApplication {

	/**
	 * Main entry point for the Spring Boot application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringAiOracleDemoApplication.class, args);
	}

	/**
	 * Creates a CommandLineRunner bean that initializes the vector store with sample documents on startup.
	 *
	 * @param documentService the service used to load documents into the vector store
	 * @return a CommandLineRunner that loads documents when the application starts
	 */
	@Bean
    CommandLineRunner initData(DocumentService documentService) {
        return args -> documentService.loadDocuments();
    }

}

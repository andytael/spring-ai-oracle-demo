package com.example.demo;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for loading and managing documents in the vector store.
 * Initializes sample documents with metadata and stores them with embeddings.
 */
@Service
public class DocumentService {

    private final VectorStore vectorStore;

    /**
     * Constructs a new DocumentService with the provided vector store.
     *
     * @param vectorStore the vector store for storing documents and embeddings
     */
    public DocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Loads sample documents into the vector store.
     * Each document includes metadata (category and technology) for filtering purposes.
     * Embeddings are generated automatically by the vector store using the configured OpenAI model.
     */
    public void loadDocuments() {
        // Create sample documents with metadata
        List<Document> documents = List.of(
            new Document("Spring Framework is a comprehensive framework for enterprise Java development.",
                Map.of("category", "framework", "technology", "java")),
            new Document("Oracle AI Database introduces AI-powered vector search capabilities.",
                Map.of("category", "database", "technology", "oracle")),
            new Document("Vector embeddings represent text as high-dimensional numerical vectors.",
                Map.of("category", "concept", "technology", "ai")),
            new Document("Machine learning models can understand semantic similarity using embeddings.",
                Map.of("category", "concept", "technology", "ml"))
        );

        // Store documents - embeddings are generated automatically
        vectorStore.add(documents);

        System.out.println("Successfully stored " + documents.size() + " documents");
    }
}


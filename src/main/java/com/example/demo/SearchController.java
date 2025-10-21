package com.example.demo;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for performing semantic search queries against the vector store.
 * Provides endpoints for similarity search using embeddings stored in Oracle Database.
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {

  private final VectorStore vectorStore;

  /**
   * Constructs a new SearchController with the provided vector store.
   *
   * @param vectorStore the vector store for performing similarity searches
   */
  public SearchController(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  /**
   * Performs a semantic similarity search based on the provided query.
   * The query is converted to an embedding and compared against stored document embeddings.
   *
   * @param query the search query text
   * @param topK the maximum number of results to return (default: 4)
   * @return a list of matching documents with their content and metadata
   */
  @GetMapping
  public List<Map<String, Object>> search(@RequestParam String query,
                                          @RequestParam(defaultValue = "4") int topK) {
    // Perform similarity search with topK limit
    SearchRequest request = SearchRequest.builder()
        .query(query)
        .topK(topK)
        .build();
    List<Document> results = vectorStore.similaritySearch(request);

    // Simplify the response to avoid serialization issues
    return results.stream()
        .map(doc -> Map.of(
            "content", doc.getText(),
            "metadata", convertMetadataToSerializable(doc.getMetadata())
        ))
        .toList();
  }

  /**
   * Performs a semantic similarity search with metadata filtering.
   * Note: The current implementation does not apply the category filter.
   *
   * @param query the search query text
   * @param category the category to filter by (currently unused)
   * @return a list of matching documents with their content and metadata
   */
  @GetMapping("/filtered")
  public List<Map<String, Object>> searchWithFilter(@RequestParam String query,
                                                    @RequestParam String category) {
    // Perform filtered similarity search
    List<Document> results = vectorStore.similaritySearch(query);

    // Simplify the response to avoid serialization issues
    return results.stream()
        .map(doc -> Map.of(
            "content", doc.getText(),
            "metadata", convertMetadataToSerializable(doc.getMetadata())
        ))
        .toList();
  }

  /**
   * Converts metadata map values to plain serializable types.
   * Oracle JDBC returns Oracle-specific types (e.g., OracleJsonStringImpl) that Jackson
   * cannot serialize. This method converts all values to Strings to avoid serialization errors.
   *
   * @param metadata the original metadata map with potentially non-serializable values
   * @return a new map with all values converted to Strings
   */
  private Map<String, Object> convertMetadataToSerializable(Map<String, Object> metadata) {
    return metadata.entrySet().stream()
        .collect(java.util.stream.Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue() != null ? entry.getValue().toString() : null
        ));
  }
}

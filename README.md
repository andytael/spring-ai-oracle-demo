# Spring AI Oracle Vector Database Demo

A Spring Boot application demonstrating semantic search capabilities using Spring AI, OpenAI embeddings, and Oracle Vector Database. This project showcases how to build an intelligent search system that understands the meaning behind queries, not just keyword matching.

## Features

- **Semantic Search**: Search documents by meaning rather than exact keyword matches
- **Vector Embeddings**: Automatic embedding generation using OpenAI's text-embedding-3-small model
- **Oracle Vector Store**: Leverages Oracle Database's native vector capabilities for efficient similarity search
- **RESTful API**: Simple endpoints for querying and searching documents
- **Sample Data**: Pre-loaded with technical documentation samples (frameworks, databases, AI/ML concepts)

## Technology Stack

- **Spring Boot** 3.5.6
- **Java** 21
- **Spring AI** 1.0.3
- **OpenAI API** (text-embedding-3-small model)
- **Oracle Database** with Vector capabilities
- **Oracle UCP** (Universal Connection Pool)

## Prerequisites

Before running this application, ensure you have:

1. **Java 21** or later installed
2. **Oracle Database** with vector capabilities
   - Oracle Database 23ai Free or higher recommended
   - Database user with appropriate permissions
3. **OpenAI API Key**
   - Sign up at [OpenAI Platform](https://platform.openai.com/)
4. **Maven** (or use the included Maven wrapper)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/andytael/spring-ai-oracle-demo.git
cd spring-ai-oracle-demo
```

2. Configure the application (see Configuration section below)

3. Build the project:
```bash
./mvnw clean package
```

## Configuration

Create or update `src/main/resources/application.properties` with your database and API credentials:

```properties
# Oracle Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/FREEPDB1
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password

# OpenAI Configuration (use environment variable)
spring.ai.openai.api-key=${OPENAPI_KEY}
```

**Important**: Set the `OPENAPI_KEY` environment variable before running the application:

```bash
export OPENAPI_KEY=your-openai-api-key-here
```

### Configuration Options

The application comes pre-configured with sensible defaults for the vector store:

- **Distance Metric**: COSINE (other options: EUCLIDEAN, DOT_PRODUCT, MANHATTAN)
- **Vector Dimensions**: 1536 (matches text-embedding-3-small)
- **Index Type**: IVF (Inverted File Index for optimized search)
- **Schema Auto-initialization**: Enabled
- **Table Recreation**: Enabled (useful for development, disable in production)

## Usage

### Running the Application

Start the application:
```bash
./mvnw spring-boot:run
```

The application will:
1. Connect to Oracle Database
2. Initialize the vector store schema
3. Load sample documents
4. Generate embeddings via OpenAI
5. Store vectors in Oracle Database
6. Start the REST API on port 8080

### API Endpoints

#### Semantic Search
Search documents by semantic similarity:

```bash
GET /api/search?query=<search-text>&topK=<number>
```

**Example**:
```bash
curl "http://localhost:8080/api/search?query=machine%20learning&topK=3"
```

**Response**:
```json
[
  {
    "content": "Document content here...",
    "metadata": {
      "category": "AI/ML",
      "technology": "Machine Learning"
    }
  }
]
```

#### Filtered Search
Search with category filtering:

```bash
GET /api/search/filtered?query=<search-text>&category=<category>
```

**Example**:
```bash
curl "http://localhost:8080/api/search/filtered?query=database&category=Database"
```

### How It Works

1. **Document Loading**: Sample documents are loaded on startup via `DocumentService`
2. **Embedding Generation**: Each document is sent to OpenAI to generate a 1536-dimension vector embedding
3. **Vector Storage**: Embeddings are stored in Oracle Database alongside the original content
4. **Similarity Search**: When you query, your search text is embedded and compared using cosine similarity
5. **Results**: The most semantically similar documents are returned, ranked by relevance

## Sample Data

The application includes sample documents covering:
- **Frameworks**: Spring Boot, React, Django, etc.
- **Databases**: Oracle, PostgreSQL, MongoDB, etc.
- **AI/ML**: Machine Learning, Neural Networks, NLP, etc.

Each document includes metadata (category, technology) for filtering and organization.

## Running Tests

Execute the test suite:
```bash
./mvnw test
```

Run a specific test:
```bash
./mvnw test -Dtest=SpringAiOracleDemoApplicationTests
```

## Development Notes

- **Table Recreation**: The application recreates the vector store table on each startup (controlled by `remove-existing-vector-store-table=true`). Disable this in production to preserve data.
- **Connection Pooling**: Oracle UCP is configured with 5-20 connections for optimal performance.
- **API Costs**: Each document and query generates OpenAI API calls. Monitor your usage at [OpenAI Platform](https://platform.openai.com/usage).

## Architecture

### Core Components

**DocumentService** (`src/main/java/com/example/demo/DocumentService.java`)
- Implements `CommandLineRunner` for startup initialization
- Creates sample documents with content and metadata
- Delegates to VectorStore for embedding generation and storage

**SearchController** (`src/main/java/com/example/demo/SearchController.java`)
- REST API endpoints for search operations
- Handles query processing and result formatting
- Returns simplified JSON responses

**VectorStore Integration**
- Spring AI abstracts the complexity of vector operations
- Automatic embedding generation via OpenAI
- Oracle-specific optimizations (IVF indexing, COSINE distance)

## Troubleshooting

**Connection Issues**:
- Verify Oracle Database is running and accessible
- Check connection string, username, and password
- Ensure database user has CREATE TABLE privileges

**OpenAI API Errors**:
- Confirm `OPENAPI_KEY` environment variable is set
- Verify API key is valid and has credits
- Check internet connectivity

**Schema Initialization Failures**:
- Ensure database supports vector operations (Oracle 23ai+)
- Check user permissions for schema operations

## License

This project is a demonstration application. Refer to individual component licenses (Spring Boot, Spring AI, Oracle, OpenAI) for production use.

## Contributing

This is a demo project. Feel free to fork and adapt for your needs.

## Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Oracle Database 23ai](https://www.oracle.com/database/23ai/)
- [OpenAI API Documentation](https://platform.openai.com/docs)

package tests.authors;

import clients.AuthorsClient;
import dto.Author;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Slf4j
public class AuthorTest extends BaseTest {

    @BeforeClass
    public void setup() {
        log.info("Initializing AuthorsClient");
        authorsClient = new AuthorsClient();
        log.info("AuthorsClient initialized");
    }

    @Test(description = "Validate authors schema")
    public void validateAuthorsSchema() {
        log.info("Validating authors schema");
        Response response = authorsClient.getAllAuthorsRaw();
        log.debug("Authors schema response:\n{}", response.asPrettyString());

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/authors-schema.json"));

        log.info("Authors schema is valid");
    }

    @Test(description = "Authors list should not be empty")
    public void getAllAuthorsTest() {
        log.info("Fetching list of authors");
        List<Author> authors = authorsClient.getAllAuthors();
        log.debug("Fetched authors: {}", authors);

        Assertions.assertThat(authors)
                .as("Authors list should not be empty")
                .isNotEmpty();

        log.info("Authors list contains {} entries", authors.size());
    }

    @Test(description = "Add author with all fields")
    public void addAuthorWithAllFieldsTest() {
        Author authorToBeAdded = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .lastName("Doe")
                .build();

        log.info("Adding author: {}", authorToBeAdded);
        Response addResp = authorsClient.addAuthor(authorToBeAdded);
        log.debug("Add response:\n{}", addResp.asPrettyString());
        addResp.then().statusCode(200);

        Author actualAuthor = authorsClient.getAuthor(1);
        log.debug("Fetched author after add: {}", actualAuthor);

        Assertions.assertThat(actualAuthor)
                .as("Authors should be the same")
                .isEqualTo(authorToBeAdded);

        log.info("Author added successfully");
    }

    @Test(description = "Add author without mandatory field (last name)")
    public void addAuthorNegativeTest() {
        Author authorToBeAdded = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .build();

        log.info("Attempting to add an incomplete author: {}", authorToBeAdded);
        authorsClient.addAuthor(authorToBeAdded)
                .then()
                .statusCode(400);
        log.info("API correctly rejected incomplete author");
    }

    @Test(description = "Update author test")
    public void updateAuthorTest() {
        Author author = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .lastName("Doe")
                .build();

        log.info("Creating initial author for update");
        authorsClient.addAuthor(author).then().statusCode(200);

        author.setFirstName("Bob");
        log.info("Updating author first name to '{}'", author.getFirstName());
        authorsClient.updateAuthor(1, author).then().statusCode(200);

        Author actualAuthor = authorsClient.getAuthor(1);
        log.debug("Fetched author after update: {}", actualAuthor);

        Assertions.assertThat(actualAuthor)
                .as("Author should be updated")
                .isEqualTo(author);

        log.info("Author updated successfully");
    }

    @Test(description = "Delete author test")
    public void deleteAuthorTest() {
        Author author = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .lastName("Doe")
                .build();

        log.info("Adding author to be deleted");
        authorsClient.addAuthor(author).then().statusCode(200);

        log.info("Deleting author with id={}", author.getId());
        authorsClient.deleteAuthor(1).then().statusCode(200);

        log.info("Verifying that deleting the same author again returns 404");
        authorsClient.deleteAuthor(1).then().statusCode(404);

        log.info("Author deletion verified");
    }
}

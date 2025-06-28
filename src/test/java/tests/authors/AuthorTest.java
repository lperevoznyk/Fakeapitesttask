package tests.authors;

import clients.AuthorsClient;
import dto.Author;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AuthorTest extends BaseTest {

    @BeforeClass
    public void setup() {
        authorsClient = new AuthorsClient();
    }

    @Test(description = "Validate authors schema")
    public void validateAuthorsSchema() {
        authorsClient.getAllAuthorsRaw()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/authors-schema.json"));
    }


    @Test(description = "Authors list should not be empty")
    public void getAllAuthorsTest() {
        List<Author> authors = authorsClient.getAllAuthors();
        Assertions.assertThat(authors)
                .as("Authors list should not be empty")
                .isNotEmpty();
    }

    @Test(description = "Add author with all fields")
    public void addAuthorWithAllFieldsTest() {
        Author authorToBeAdded = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .lastName("Doe")
                .build();
        authorsClient.addAuthor(authorToBeAdded)
                .then()
                .statusCode(200);
        Author actualAuthor = authorsClient.getAuthor(1);
        Assertions.assertThat(actualAuthor)
                .as("Authors should be the same")
                .isEqualTo(authorToBeAdded);
    }

    @Test(description = "Add author without mandatory field (last name)")
    public void addAuthorNegativeTest() {
        Author authorToBeAdded = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .build();
        authorsClient.addAuthor(authorToBeAdded)
                .then()
                .statusCode(400);
    }

    @Test(description = "Update author test")
    public void updateAuthorTest() {
        Author author = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .lastName("Doe")
                .build();
        authorsClient.addAuthor(author)
                .then()
                .statusCode(200);
        author.setFirstName("Bob");
        authorsClient.updateAuthor(1, author)
                .then()
                .statusCode(200);
        Author actualAuthor = authorsClient.getAuthor(1);
        Assertions.assertThat(actualAuthor)
                .as("Author should be updated")
                .isEqualTo(author);
    }

    @Test(description = "Delete author test")
    public void deleteAuthorTest() {
        Author author = Author.builder()
                .id(1)
                .idBook(2)
                .firstName("John")
                .lastName("Doe")
                .build();
        authorsClient.addAuthor(author)
                .then()
                .statusCode(200);
        authorsClient.deleteAuthor(1)
                .then()
                .statusCode(200);
        authorsClient.deleteAuthor(1)
                .then()
                .statusCode(404);
    }

}

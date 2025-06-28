package clients;

import dto.Author;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@Slf4j
public class AuthorsClient extends BaseClient {

    @Step
    @SneakyThrows
    public List<Author> getAllAuthors() {
        log.info("GET {}", CONFIG.getAuthorsPath());
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath());

        log.debug("Response body for GET all authors:\n{}", response.asPrettyString());
        return Arrays.stream(OBJECT_MAPPER.readValue(response.getBody().asString(), Author[].class)).toList();
    }

    @Step
    public Response getAllAuthorsRaw() {
        log.info("GET {} (raw)", CONFIG.getAuthorsPath());
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath());
    }

    @Step
    @SneakyThrows
    public Author getAuthor(Integer id) {
        log.info("GET {}/{}", CONFIG.getAuthorsPath(), id);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath() + "/" + id);

        log.debug("Response body for GET author {}:\n{}", id, response.asPrettyString());
        return OBJECT_MAPPER.readValue(response.getBody().asString(), Author.class);
    }

    @Step
    @SneakyThrows
    public Response addAuthor(Author author) {
        log.info("POST {}  —  payload: {}", CONFIG.getAuthorsPath(), author);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(author))
                .when()
                .post(CONFIG.getAuthorsPath());

        log.debug("Response body for POST author:\n{}", response.asPrettyString());
        return response;
    }

    @Step
    @SneakyThrows
    public Response updateAuthor(Integer id, Author author) {
        log.info("PUT {}/{}  —  payload: {}", CONFIG.getAuthorsPath(), id, author);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(author))
                .when()
                .put(CONFIG.getAuthorsPath() + "/" + id);

        log.debug("Response body for PUT author {}:\n{}", id, response.asPrettyString());
        return response;
    }

    @Step
    public Response deleteAuthor(Integer id) {
        log.info("DELETE {}/{}", CONFIG.getAuthorsPath(), id);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .delete(CONFIG.getAuthorsPath() + "/" + id);

        log.debug("Response body for DELETE author {}:\n{}", id, response.asPrettyString());
        return response;
    }
}

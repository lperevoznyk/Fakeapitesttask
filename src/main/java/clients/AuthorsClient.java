package clients;

import dto.Author;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AuthorsClient extends BaseClient {

    @Step
    @SneakyThrows
    public List<Author> getAllAuthors() {
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath());
        return Arrays.stream(OBJECT_MAPPER.readValue(response.getBody().asString(), Author[].class)).toList();
    }

    @Step
    public Response getAllAuthorsRaw() {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath());
    }

    @Step
    @SneakyThrows
    public Response addAuthor(Author author) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(author))
                .when()
                .post(CONFIG.getAuthorsPath());
    }

    @Step
    @SneakyThrows
    public Author getAuthor(Integer id) {
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath() + "/" + id);
        return OBJECT_MAPPER.readValue(response.getBody().asString(), Author.class);
    }

    @Step
    @SneakyThrows
    public Response getAuthorRaw(Integer id) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getAuthorsPath() + "/" + id);
    }

    @Step
    @SneakyThrows
    public Response updateAuthor(Integer id, Author author) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(author))
                .when()
                .put(CONFIG.getAuthorsPath() + "/" + id);
    }

    @Step
    public Response deleteAuthor(Integer id) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .delete(CONFIG.getAuthorsPath() + "/" + id);
    }

}

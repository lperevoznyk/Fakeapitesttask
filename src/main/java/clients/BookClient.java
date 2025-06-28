package clients;

import dto.Book;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@Slf4j
public class BookClient extends BaseClient {

    @Step
    @SneakyThrows
    public List<Book> getAllBooks() {
        log.info("GET {}", CONFIG.getBooksPath());
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath());

        log.debug("Response body for GET all books:\n{}", response.asPrettyString());
        return Arrays.stream(
                OBJECT_MAPPER.readValue(response.getBody().asString(), Book[].class)
        ).toList();
    }

    @Step
    public Response getAllBooksRaw() {
        log.info("GET {} (raw)", CONFIG.getBooksPath());
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath());
    }

    @Step
    @SneakyThrows
    public Book getBook(Integer id) {
        log.info("GET {}/{}", CONFIG.getBooksPath(), id);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath() + "/" + id);

        log.debug("Response body for GET book {}:\n{}", id, response.asPrettyString());
        return OBJECT_MAPPER.readValue(response.getBody().asString(), Book.class);
    }

    @Step
    public Response getBookRaw(Integer id) {
        log.info("GET {}/{} (raw)", CONFIG.getBooksPath(), id);
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath() + "/" + id);
    }

    @Step
    @SneakyThrows
    public Response addBook(Book book) {
        log.info("POST {}  —  payload: {}", CONFIG.getBooksPath(), book);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(book))
                .when()
                .post(CONFIG.getBooksPath());

        log.debug("Response body for POST book:\n{}", response.asPrettyString());
        return response;
    }

    @Step
    @SneakyThrows
    public Response updateBook(Integer id, Book book) {
        log.info("PUT {}/{}  —  payload: {}", CONFIG.getBooksPath(), id, book);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(book))
                .when()
                .put(CONFIG.getBooksPath() + "/" + id);

        log.debug("Response body for PUT book {}:\n{}", id, response.asPrettyString());
        return response;
    }

    @Step
    public Response deleteBook(Integer id) {
        log.info("DELETE {}/{}", CONFIG.getBooksPath(), id);
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .delete(CONFIG.getBooksPath() + "/" + id);

        log.debug("Response body for DELETE book {}:\n{}", id, response.asPrettyString());
        return response;
    }
}

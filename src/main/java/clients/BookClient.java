package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import dto.Book;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BookClient extends BaseClient {

    @Step
    @SneakyThrows
    public List<Book> getAllBooks() {
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath());
        return Arrays.stream(OBJECT_MAPPER.readValue(response.getBody().asString(), Book[].class)).toList();
    }

    @Step
    public Response getAllBooksRaw() {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath());
    }

    @Step
    @SneakyThrows
    public Response addBook(Book book) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(book))
                .when()
                .post(CONFIG.getBooksPath());
    }

    @Step
    @SneakyThrows
    public Book getBook(Integer id) {
        Response response = given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath() + "/" + id);
        return OBJECT_MAPPER.readValue(response.getBody().asString(), Book.class);
    }

    @Step
    @SneakyThrows
    public Response getBookRaw(Integer id) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .get(CONFIG.getBooksPath() + "/" + id);
    }

    @Step
    @SneakyThrows
    public Response updateBook(Integer id, Book book) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .body(OBJECT_MAPPER.writeValueAsString(book))
                .when()
                .put(CONFIG.getBooksPath() + "/" + id);
    }

    @Step
    public Response deleteBook(Integer id) {
        return given()
                .spec(BASE_REQUEST_SPEC)
                .when()
                .delete(CONFIG.getBooksPath() + "/" + id);
    }
}
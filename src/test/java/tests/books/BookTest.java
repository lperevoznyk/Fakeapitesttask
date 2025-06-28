package tests.books;

import clients.BookClient;
import dto.Book;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Slf4j
public class BookTest extends BaseTest {

    @BeforeClass
    public void setup() {
        log.info("Initializing BookClient");
        bookClient = new BookClient();
        log.info("BookClient initialized");
    }

    @DataProvider(name = "booksMissingOneField")
    public static Object[][] booksMissingOneField() {

        Book base = Book.builder()
                .id(1)
                .description("Amazing story")
                .title("The Lord of The Rings")
                .pageCount(1234)
                .excerpt("""
                        When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his \
                        eleventy-first birthday with a party of special magnificence, there was much talk and \
                        excitement in Hobbiton.
                        """)
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();

        return new Object[][]{
                {base.toBuilder().id(null).build()},          // missing id
                {base.toBuilder().description(null).build()}, // missing description
                {base.toBuilder().title(null).build()},       // missing title
                {base.toBuilder().pageCount(null).build()},   // missing pageCount
                {base.toBuilder().excerpt(null).build()},     // missing excerpt
                {base.toBuilder().publishDate(null).build()}  // missing publishDate
        };
    }

    @Test(description = "Validate books schema")
    public void validateBooksSchema() {
        log.info("Validating books schema");
        Response response = bookClient.getAllBooksRaw();
        log.debug("Books schema response:\n{}", response.asPrettyString());

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/books-schema.json"));

        log.info("Books schema is valid");
    }

    @Test(description = "Books list should not be empty")
    public void getAllBooksTest() {
        log.info("Fetching list of books");
        List<Book> books = bookClient.getAllBooks();
        log.debug("Fetched books: {}", books);

        Assertions.assertThat(books)
                .as("Books list should not be empty")
                .isNotEmpty();

        log.info("Books list contains {} entries", books.size());
    }

    @Test(description = "Add book with all fields")
    public void addBookWithAllFieldsTest() {
        Book bookToBeAdded = Book.builder()
                .id(1)
                .title("The Lord of The Rings")
                .description("Amazing story")
                .pageCount(1234)
                .excerpt("""
                        When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his \
                        eleventy-first birthday with a party of special magnificence, there was much talk and \
                        excitement in Hobbiton.
                        """)
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();

        log.info("Adding book: {}", bookToBeAdded);
        Response addResp = bookClient.addBook(bookToBeAdded);
        log.debug("Add response:\n{}", addResp.asPrettyString());
        addResp.then().statusCode(200);

        Book actualBook = bookClient.getBook(1);
        log.debug("Fetched book after add: {}", actualBook);

        Assertions.assertThat(actualBook)
                .as("Books should be the same")
                .isEqualTo(bookToBeAdded);

        log.info("Book added successfully");
    }

    @Test(description = "Add book without mandatory field",
            dataProvider = "booksMissingOneField")
    public void addBookNegativeTest(Book bookToBeAdded) {
        log.info("Attempting to add an incomplete book: {}", bookToBeAdded);
        bookClient.addBook(bookToBeAdded)
                .then()
                .statusCode(400);
        log.info("API correctly rejected incomplete book");
    }

    @Test(description = "Update book test")
    public void updateBookTest() {
        Book book = Book.builder()
                .id(1)
                .title("The Lord of The Rings")
                .description("Amazing story")
                .pageCount(1234)
                .excerpt("""
                        When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his \
                        eleventy-first birthday with a party of special magnificence, there was much talk and \
                        excitement in Hobbiton.
                        """)
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();

        log.info("Creating initial book for update");
        bookClient.addBook(book).then().statusCode(200);

        book.setTitle("Hobbit");
        log.info("Updating book title to '{}'", book.getTitle());
        bookClient.updateBook(1, book).then().statusCode(200);

        Book actualBook = bookClient.getBook(1);
        log.debug("Fetched book after update: {}", actualBook);

        Assertions.assertThat(actualBook)
                .as("Book should be updated")
                .isEqualTo(book);

        log.info("Book updated successfully");
    }

    @Test(description = "Delete book test")
    public void deleteBookTest() {
        Book book = Book.builder()
                .id(1)
                .title("The Lord of The Rings")
                .description("Amazing story")
                .pageCount(1234)
                .excerpt("""
                        When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his \
                        eleventy-first birthday with a party of special magnificence, there was much talk and \
                        excitement in Hobbiton.
                        """)
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();

        log.info("Adding book to be deleted");
        bookClient.addBook(book).then().statusCode(200);

        log.info("Deleting book with id={}", book.getId());
        bookClient.deleteBook(1).then().statusCode(200);

        log.info("Verifying that deleted book returns 404");
        bookClient.getBookRaw(1).then().statusCode(404);

        log.info("Book deleted successfully");
    }
}

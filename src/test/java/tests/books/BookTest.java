package tests.books;

import clients.BookClient;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import dto.Book;
import tests.BaseTest;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookTest extends BaseTest {

    @BeforeClass
    public void setup() {
        bookClient = new BookClient();
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
                {base.toBuilder().id(null).build()},            // missing id
                {base.toBuilder().description(null).build()},   // missing description
                {base.toBuilder().title(null).build()},         // missing title
                {base.toBuilder().pageCount(null).build()},     // missing pageCount
                {base.toBuilder().excerpt(null).build()},       // missing excerpt
                {base.toBuilder().publishDate(null).build()}    // missing publishDate
        };
    }

    @Test(description = "Validate books schema")
    public void validateBooksSchema() {
        bookClient.getAllBooksRaw()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/books-schema.json"));
    }


    @Test(description = "Books list should not be empty")
    public void getAllBooksTest() {
        List<Book> books = bookClient.getAllBooks();
        Assertions.assertThat(books)
                .as("Books list should not be empty")
                .isNotEmpty();
    }

    @Test(description = "Add book with all fields")
    public void addBookWithAllFieldsTest() {
        Book bookToBeAdded = Book.builder()
                .id(1)
                .title("The Lord of The Rings")
                .description("Amazing story")
                .pageCount(1234)
                .excerpt("When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his " +
                        "eleventy-first birthday with a party of special magnificence, there was much talk and " +
                        "excitement in Hobbiton.")
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();
        bookClient.addBook(bookToBeAdded)
                .then()
                .statusCode(200);
        Book actualBook = bookClient.getBook(1);
        Assertions.assertThat(actualBook)
                .as("Books should be the same")
                .isEqualTo(bookToBeAdded);
    }

    @Test(description = "Add book without mandatory field", dataProvider = "booksMissingOneField")
    public void addBookNegativeTest(Book bookToBeAdded) {
        bookClient.addBook(bookToBeAdded)
                .then()
                .statusCode(400);
    }

    @Test(description = "Update book test")
    public void updateBookTest() {
        Book book = Book.builder()
                .id(1)
                .title("The Lord of The Rings")
                .description("Amazing story")
                .pageCount(1234)
                .excerpt("When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his " +
                        "eleventy-first birthday with a party of special magnificence, there was much talk and " +
                        "excitement in Hobbiton.")
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();
        bookClient.addBook(book)
                .then()
                .statusCode(200);
        book.setTitle("Hobbit");
        bookClient.updateBook(1, book)
                .then()
                .statusCode(200);
        Book actualBook = bookClient.getBook(1);
        Assertions.assertThat(actualBook)
                .as("Book should be updated")
                .isEqualTo(book);
    }

    @Test(description = "Delete book test")
    public void deleteBookTest() {
        Book book = Book.builder()
                .id(1)
                .title("The Lord of The Rings")
                .description("Amazing story")
                .pageCount(1234)
                .excerpt("When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his " +
                        "eleventy-first birthday with a party of special magnificence, there was much talk and " +
                        "excitement in Hobbiton.")
                .publishDate("1954-07-29T14:59:55.953Z")
                .build();
        bookClient.addBook(book)
                .then()
                .statusCode(200);
        bookClient.deleteBook(1)
                .then()
                .statusCode(200);
        bookClient.getBookRaw(1)
                .then()
                .statusCode(404);
    }
}
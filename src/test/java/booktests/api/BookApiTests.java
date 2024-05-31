package booktests.api;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.List;


public class BookApiTests {
    private static ClientAndServer mockServer;
    private static final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter("openapi.json");

    @BeforeAll
    public static void setup() {
        mockServer = ClientAndServer.startClientAndServer(8080);
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.defaultParser = Parser.JSON;

        MockServerSetup.setupMocks(mockServer);
    }

    @AfterAll
    public static void tearDown() {
        mockServer.stop();
    }

    @Test
    public void testAddBooksWithValidData() throws JsonProcessingException {
        List<Book> books = List.of(
                new Book("1984", "George Orwell", 1949, "Some comment"),
                new Book("Animal Farm", "George Orwell", 1945)
        );

        String jsonBooks = BookSerializer.serializeBooks(books);

        given()
                .contentType(ContentType.JSON)
                .filter(validationFilter)
                .body(jsonBooks)
                .when()
                .post("/books")
                .then()
                .statusCode(201)
                .body("message", containsString("Books successfully added!"));
    }

    @Test
    public void testAddBooksWithMissingRequiredField() throws JsonProcessingException {
        List<Book> books = List.of(
                new Book("1984", "George Orwell", 1949, "Some comment"),
                new Book("Animal Farm", "George Orwell")
        );

        String jsonBooks = BookSerializer.serializeBooks(books);

        given()
                .contentType(ContentType.JSON)
                .body(jsonBooks)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("error", containsString("PublishYear is required"));
    }

    @Test
    public void testAddDuplicateBooks() throws JsonProcessingException {
        List<Book> books = List.of(
                new Book("1984", "George Orwell", 1949, "Some comment"),
                new Book("1984", "George Orwell", 1949, "Some comment")
        );

        String jsonBooks = BookSerializer.serializeBooks(books);

        given()
                .contentType(ContentType.JSON)
                .filter(validationFilter)
                .body(jsonBooks)
                .when()
                .post("/books")
                .then()
                .statusCode(409)
                .body("error", containsString("Duplicate entry"));
    }
}

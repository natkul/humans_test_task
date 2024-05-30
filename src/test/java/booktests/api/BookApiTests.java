package booktests.api;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import static io.restassured.RestAssured.given;

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
    public void testAddBookWithRequiredFields() {
        String bodyWithRequiredFields = "{\"name\":\"1984\",\"author\":\"George Orwell\",\"publishYear\":1949}";

        given()
                .contentType(ContentType.JSON)
                .filter(validationFilter)
                .body(bodyWithRequiredFields)
                .when()
                .post("/books")
                .then()
                .statusCode(201);
    }

    @Test
    public void testAddBookWithAllFields() {
        String bodyWithAllFields = "{\"name\":\"1984\",\"author\":\"George Orwell\",\"publishYear\":1949,\"comment\":\"Dystopian novel\"}";

        given()
                .contentType(ContentType.JSON)
                .filter(validationFilter)
                .body(bodyWithAllFields)
                .when()
                .post("/books")
                .then()
                .statusCode(201);
    }

    @Test
    public void testAddBookWithoutRequiredNameField() {
        String bodyWithoutRequiredFields = "{\"author\":\"George Orwell\",\"publishYear\":1949,\"comment\":\"Dystopian novel\"}";

        given()
                .contentType(ContentType.JSON)
                .body(bodyWithoutRequiredFields)
                .when()
                .post("/books")
                .then()
                .statusCode(400);
    }
}

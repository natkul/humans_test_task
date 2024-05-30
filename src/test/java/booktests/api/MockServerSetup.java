package booktests.api;

import org.mockserver.integration.ClientAndServer;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServerSetup {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    public static void setupMocks(ClientAndServer mockServer) {
        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/books")
                        .withBody("{\"name\":\"1984\",\"author\":\"George Orwell\",\"publishYear\":1949}")
        ).respond(
                response()
                        .withStatusCode(201)
                        .withBody("{\"message\": \"Book successfully added!\"}")
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
        );

        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/books")
                        .withBody("{\"name\":\"1984\",\"author\":\"George Orwell\",\"publishYear\":1949,\"comment\":\"Dystopian novel\"}")
        ).respond(
                response()
                        .withStatusCode(201)
                        .withBody("{\"message\": \"Book successfully added!\"}")
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
        );

        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/books")
                        .withBody("{\"author\":\"George Orwell\",\"publishYear\":1949,\"comment\":\"Dystopian novel\"}")
        ).respond(
                response()
                        .withStatusCode(400)
                        .withBody("{\"error\":\"Name is required\"}")
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
        );
    }
}

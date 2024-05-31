package booktests.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class BookSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String serializeBooks(List<Book> books) throws JsonProcessingException {
        return objectMapper.writeValueAsString(books);
    }

    public static String serializeBook(Book book) throws JsonProcessingException {
        return objectMapper.writeValueAsString(book);
    }
}

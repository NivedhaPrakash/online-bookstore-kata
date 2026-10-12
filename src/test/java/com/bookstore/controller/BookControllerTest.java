package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.exception.UnauthorizedException;
import com.bookstore.service.BookService;
import com.bookstore.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testGetAllBooks() throws Exception {
        //Given: BookService returns two books
        Book firstBook = new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );
        Book secondBook = new Book(
                2L,
                "Kalki Purana (Part-9)",
                "Kalki",
                new BigDecimal("230.00"),
                8
        );
        List<Book> expectedBooks = Arrays.asList(firstBook,secondBook);
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        //When and Then: GET /api/books
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Kalki Purana (Part-8)"))
                .andExpect(jsonPath("$[1].bookId").value(2L))
                .andExpect(jsonPath("$[1].title").value("Kalki Purana (Part-9)"));

    }

    @Test
    void testGetBookById() throws Exception {
        //Given
        Book book = new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        //When and Then
        mockMvc.perform(get("/api/books/{bookId}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.title").value("Kalki Purana (Part-8)"))
                .andExpect(jsonPath("$.author").value("Kalki"))
                .andExpect(jsonPath("$.price").value(200.00))
                .andExpect(jsonPath("$.availableStock").value(10));
    }

    @Test
    void testShouldReturnNotFound() throws Exception {
        //Given
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        //When and Then
        mockMvc.perform(get("/api/books/{bookId}",1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Book Not Found"));;
    }

    @Test
    void testShouldReturnUnauthorizedAccess() throws Exception {
        when(bookService.getBookById(1L))
                .thenThrow(new UnauthorizedException("User Not Authenticated"));

        mockMvc.perform(get("/api/books/{id}",1L))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("User Not Authenticated"));
    }

    @Test
    void testShouldReturnInternalServerError() throws Exception {
        when(bookService.getBookById(1L))
                .thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(get("/api/books/{id}",1L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected Error"));
    }
}

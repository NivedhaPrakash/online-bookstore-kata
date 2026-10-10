package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() {
        //Given: Two books are available in repository
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
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        //When: The service retrieves all books
        List<Book> actualBooks = bookService.getAllBooks();

        //Then: Both books are returned
        assertEquals(2, actualBooks.size());
        assertEquals("Kalki Purana (Part-8)", actualBooks.get(0).getTitle());

    }

    @Test
    void shouldReturnBookById(){
        //Given
        Book expectedBook =  new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );
        when(bookRepository.findById((1L))).thenReturn(Optional.of(expectedBook));

        //When
        Optional<Book> actualBook = bookService.getBookById(1L);

        //Then
        assertTrue(actualBook.isPresent());
        assertEquals(1L,actualBook.get().getBookId());
        assertEquals("Kalki Purana (Part-8)", actualBook.get().getTitle());
        assertEquals(new BigDecimal("200.00"),actualBook.get().getPrice());
    }
}

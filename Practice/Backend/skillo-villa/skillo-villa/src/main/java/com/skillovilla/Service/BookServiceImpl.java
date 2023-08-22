package com.skillovilla.Service;

import com.skillovilla.Model.Book;
import com.skillovilla.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;
    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
}

package com.skillovilla.Service;


import com.skillovilla.Exception.CustomException;
import com.skillovilla.Model.Book;
import com.skillovilla.Model.Transaction;
import com.skillovilla.Model.User;
import com.skillovilla.Repository.BookRepository;
import com.skillovilla.Repository.TransactionRepository;
import com.skillovilla.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    private BookRepository bookRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public LocalDate borrowBook(int bookId, int userId) throws Exception{

        Optional<Book> ifBook = bookRepository.findById(bookId);
        Optional<User> ifUser = userRepository.findById(userId);

        if(ifBook.isEmpty()){
            throw new CustomException("Book Not Found");
        }

        if(ifUser.isEmpty()){
            throw new CustomException("User Not Found");
        }

        Transaction transaction = new Transaction();
        transaction.setBook(bookRepository.findById(bookId).get());
        transaction.setUser(userRepository.findById(userId).get());
        transaction.setBorrowedDate(LocalDate.now());
        LocalDate assignedDate = LocalDate.now().plusDays(28);
        transaction.setAssignedReturnDate(assignedDate);

        transactionRepository.save(transaction);

        return assignedDate;
    }

    @Override
    public Float returnBook(int transactionId) throws CustomException {
        Optional<Transaction> ifTransaction = transactionRepository.findById(transactionId);

        if(ifTransaction.isEmpty()){
            throw new CustomException("Trasaction is not present");
        }
        else{
            Transaction transaction = ifTransaction.get();
            LocalDate assignedDate = transaction.getAssignedReturnDate();
            return returnPrice(assignedDate);
        }
    }

    public Float returnPrice(LocalDate assignedDate){
        Float regularPrice = 100.00f;
        Float fine = 5.00f;

        if(LocalDate.now().isAfter(assignedDate)){
            long noOfDays = LocalDate.now().until(assignedDate, ChronoUnit.DAYS);
            return regularPrice + fine*noOfDays;
        }
        else{
            return regularPrice;
        }
    }
}

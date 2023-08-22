package com.skillovilla.Controller;

import com.skillovilla.Exception.CustomException;
import com.skillovilla.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<LocalDate> borrowBook(@RequestParam int bookId, @RequestParam int userId) throws Exception {
        return new ResponseEntity<>(transactionService.borrowBook(bookId,userId), HttpStatus.CREATED);
    }

    @PostMapping("/return")
    public ResponseEntity<Float> returnBook(@RequestParam int transactionId) throws CustomException {
        return new ResponseEntity<>(transactionService.returnBook(transactionId),HttpStatus.FOUND);
    }

}

package com.skillovilla.Service;

import com.skillovilla.Exception.CustomException;

import java.time.LocalDate;

public interface TransactionService {

    public LocalDate borrowBook(int bookId,int userId) throws Exception;

    public Float returnBook(int transactionId) throws CustomException;

}

package com.skillovilla.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;
    private LocalDate borrowedDate;
    private LocalDate assignedReturnDate;

    @ManyToOne
    private Book book;
    @ManyToOne
    private User user;
}

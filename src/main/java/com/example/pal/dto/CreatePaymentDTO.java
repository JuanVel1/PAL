package com.example.pal.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreatePaymentDTO {

    private Long userID;

    private Double amount;

    private LocalDate paymentDate;
}

package com.example.pal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import com.example.pal.dto.CreatePaymentDTO;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Payment;
import com.example.pal.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService PaymentService;


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<Payment>> createPayment(@RequestBody CreatePaymentDTO createPaymentDTO) 
    {
        try 
        {
            Payment Payment = PaymentService.createPayment(createPaymentDTO);
            ResponseDTO<Payment> response = new ResponseDTO<>("Payment created successfully", Payment);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<Payment> response = new ResponseDTO<>(e.getMessage() + " ", null);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<Payment>> getAllPayments() {
        List<Payment> courses = PaymentService.getAllPayments();
        ResponseDTO<Payment> response = new ResponseDTO<>("Payments retrieved successfully", courses);
        return ResponseEntity.status(200).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Payment>> getPaymentById(@PathVariable Long id) {
        //Si el curso no existe, retornar un 404 sino retornar el curso a través del ResponseDTO
        Optional<Payment> course = PaymentService.getPaymentById(id);
        if (course.isEmpty()) {
            ResponseDTO<Payment> response = new ResponseDTO<>("Payment not found", null);
            return ResponseEntity.status(404).body(response);
        }
        ResponseDTO<Payment> response = new ResponseDTO<>("Payment retrieved successfully", course.get());
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        PaymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}

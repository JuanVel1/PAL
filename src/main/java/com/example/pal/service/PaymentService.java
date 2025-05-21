package com.example.pal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pal.dto.CreatePaymentDTO;
import com.example.pal.model.Payment;
import com.example.pal.repository.PaymentRepository;
import com.example.pal.repository.UserRepository;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository PaymentRepository;

    @Autowired
    private UserRepository userRepository;

    public Payment createPayment(CreatePaymentDTO createPaymentDTO) 
    {

        Payment Payment = new Payment();
        // Validate that the user exists
        if (!userRepository.existsById(createPaymentDTO.getUserID())) {
            throw new RuntimeException("User not found!");
        }
        // Validate that the payment amount is not negative
        if (createPaymentDTO.getAmount() < 0) {
            throw new RuntimeException("Payment amount cannot be negative!");
        }
        // Validate that the payment date is not null
        if (createPaymentDTO.getPaymentDate() == null) {
            throw new RuntimeException("Payment date cannot be null!");
        }
        // Validate that the payment date is not in the future
        if (createPaymentDTO.getPaymentDate().isAfter(java.time.LocalDate.now())) {
            throw new RuntimeException("Payment date cannot be in the future!");
        }
        // Validate that the payment date is not in the past
        if (createPaymentDTO.getPaymentDate().isBefore(java.time.LocalDate.now())) {
            throw new RuntimeException("Payment date cannot be in the past!");
        }
        // Validate that the payment amount is not zero
        if (createPaymentDTO.getAmount() == 0) {
            throw new RuntimeException("Payment amount cannot be zero!");
        }
        
        Payment.setUser(userRepository.findById(createPaymentDTO.getUserID()).orElseThrow(() -> new RuntimeException("User not found!")));
        Payment.setAmount(createPaymentDTO.getAmount());
        Payment.setPaymentDate(createPaymentDTO.getPaymentDate());

        return PaymentRepository.save(Payment);
    }

    public List<Payment> getAllPayments() {
        return PaymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return PaymentRepository.findById(id);
    }

    public void deletePayment(Long id) {
        PaymentRepository.deleteById(id);
    }
}

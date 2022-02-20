package com.sg.kata.service;

import com.sg.kata.entity.TransactionEntity;
import com.sg.kata.model.Operation;
import com.sg.kata.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    /**
     * Max overdraft authorized amount.
     */
    @Value("${authorized.overdraft.amount}")
    private Double overdraftAmount;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Double doDeposit(Double amount) {
        Assert.isTrue(amount > 0, "Deposit amount must be a positive value");
        this.transactionRepository.save(new TransactionEntity(amount, Operation.DEPOSIT));
        return getBalance();
    }

    public Double doWithdraw(Double amount) {
        Assert.isTrue(amount > 0, "Withdraw amount must be a positive value");
        validateWithdrawAmount(amount);
        this.transactionRepository.save(new TransactionEntity(-amount, Operation.WITHDRAW));
        return getBalance();
    }

    /**
     * Check that balance after withdraw doesn't exceed max overdraft .
     *
     * @param amount the amount to withdraw
     */
    private void validateWithdrawAmount(Double amount) {
        Assert.isTrue(getBalance() - amount >= -overdraftAmount, MessageFormat.format("{}€ overdraft exceed, withdraw rejected.", overdraftAmount));
    }

    private Double getBalance() {
        return StreamSupport.stream(this.transactionRepository.findAll().spliterator(), false).map(TransactionEntity::getAmount).mapToDouble(value -> value).sum();
    }

    public void displayHistory() {
        if (this.transactionRepository.count() > 0) {
            StreamSupport.stream(this.transactionRepository.findAll(Sort.by("timeStamp")).spliterator(), false).forEach(oneOperation ->
                    LOGGER.info("At {} - {} : {} €", oneOperation.getTimeStamp(), oneOperation.getOperation(), Math.abs(oneOperation.getAmount())));
            LOGGER.info("Total account balance at {} is about {} €", LocalDateTime.now(), getBalance());
        } else {
            LOGGER.info("No operation on this account");
        }
    }
}

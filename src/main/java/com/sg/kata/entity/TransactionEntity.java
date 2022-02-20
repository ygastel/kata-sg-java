package com.sg.kata.entity;

import com.sg.kata.model.Operation;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@KeySpace("transaction")
public class TransactionEntity {

    private final LocalDateTime timeStamp = LocalDateTime.now();
    @Id
    private final String id = UUID.randomUUID().toString();
    private final Double amount;
    private final Operation operation;

    public TransactionEntity(Double amount, Operation operation) {
        this.amount = amount;
        this.operation = operation;
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return timeStamp.equals(that.timeStamp) && id.equals(that.id) && amount.equals(that.amount) && operation == that.operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, id, amount, operation);
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Operation getOperation() {
        return operation;
    }


}

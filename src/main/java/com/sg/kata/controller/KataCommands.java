package com.sg.kata.controller;

import com.sg.kata.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@ShellComponent
public class KataCommands {

    private static final Logger LOGGER = LoggerFactory.getLogger(KataCommands.class);

    private final TransactionService transactionService;

    public KataCommands(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Do a deposit
     *
     * @param amount - positive amount with 2 digits max
     */
    @ShellMethod("Deposit amount to account")
    public void deposit(@ShellOption("amount to add to account") @Digits(integer = 10, fraction = 2) Double amount) {
        final var balance = this.transactionService.doDeposit(amount);
        LOGGER.info("Balance after deposit is about {} €", balance);
    }

    /**
     * Do a withdraw
     *
     * @param amount - positive amount with 2 digits max
     */
    @ShellMethod("Withdraw amount from account")
    public void withdraw(@ShellOption("amount to remove from account") @Min(0) @Digits(integer = 10, fraction = 2) Double amount) {
        final var balance = this.transactionService.doWithdraw(amount);
        LOGGER.info("Balance after withdraw is about {} €", balance);
    }

    @ShellMethod("Display account history")
    public void accountHistory() {
        this.transactionService.displayHistory();
    }
}

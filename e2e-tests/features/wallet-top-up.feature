Feature: top-up a customer wallet

  Scenario: a customer top-ups its wallet from the bank
    Given an existing customer with a bank account and a wallet
    When the customer submit a top-up request
    Then a bank transfer is requested
    And there is a pending top-up
    And there is a pending movement to the wallet
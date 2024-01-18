# payment-system
A simple payment system

TODO: 
- move to a distributed modular monolith architecture:  
  - consumer:
    - customer-service (as separate app)
    - consumer-gateway
    - customer-aggregator
  - core:
    - wallet
    - top-up
    - bank-service
  - business:
    - 

## wallet service

Keeps information about the users (business and consumer) wallets and the movements 

## top-up

Orchestrate the top-up of wallets or bank accounts or other (pay-pal)

## bank account

Keeps the information about the bank accounts connected to the users 

## consumer aggregator

aggregates the call to the other services

## business aggregator

aggregates the call to the other services

## consumer gateway

allow only consumer user to access to consumer features

## business gateway

allow only business user to access to business features

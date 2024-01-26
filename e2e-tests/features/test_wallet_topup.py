import uuid
import requests
import pytest
from pytest_bdd import scenarios
from pytest_bdd import given, when, then, scenarios, parsers

from client.customer_service_client import create_customer, find_customer_by_email

scenarios('./wallet_topup.feature')


class Customer:
    def __init__(self, id: str):
        self.id = id


@pytest.fixture
def customer():
    return Customer(str(uuid.uuid4()))


@given("an existing customer with a bank account and a wallet")
def existing_customer_with_wallet_and_bank_account():
    find_customer_by_email("e2e@example.com")
    create_customer("antessio", "antessio7@gmail.com", "IT12B1111111111")
    pass


@when("the customer submit a top-up request")
def customer_submit_top_up_request():
    pass


@then("a bank transfer is requested")
def bank_transfer_requested():
    pass


@then("there is a pending top-up")
def pending_topup():
    pass


@then("there is a pending movement to the wallet")
def pending_wallet_movement():
    pass

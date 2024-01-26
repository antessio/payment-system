import requests
import json

url = "http://localhost:3000"


def create_customer(name: str, email: str, iban: str):
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "e2e-tests"
    }

    request_body = {
        "name": name,
        "email": email,
        "iban": iban
    }
    response = requests.request("POST", "{0}/api/customers".format(url), json=request_body, headers=headers)
    return json.load(response.text)


def find_customer_by_email(email: str):
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "e2e-tests"
    }
    response = requests.request("GET", "{0}/api/customers?email={1}".format(url, email), headers=headers)
    return json.load(response.text)

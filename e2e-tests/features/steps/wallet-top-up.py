from behave import *

use_step_matcher("re")


@given("an existing customer with a bank account and a wallet")
def step_impl(context):
    """
    :type context: behave.runner.Context
    """
    raise NotImplementedError(u'STEP: Given an existing customer with a bank account and a wallet')
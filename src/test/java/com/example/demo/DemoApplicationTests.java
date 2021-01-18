package com.example.demo;

import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private KieSession session;

	@Test
	void contextLoads() {
	}

	/**
	 * NULL CHECK TESTS
	 * If the user doesn't specify a given value then the last thing I would want to do is respond with a null value.
	 * These tests verify that if no value is provided then I should respond with an initial base value.
	 * This helps ensure the stability of downstream systems.
	 * Examples would be "" for strings, 0 for ints, and 0.0 for doubles.
	 */
	@Test
	void personStateHasInitialValue() {
		Assert.check(new Person().getState().equals(""));
	}

	@Test
	void personCreditScoreHasInitialValue() {
		Assert.check(new Person().getCreditScore() == 0);
	}

	@Test
	void personNotNull() {
		Assert.checkNonNull(new Order().getPerson());
	}

	@Test
	void productNameHasInitialValue() {
		Assert.check(new Product().getName().equals(""));
	}

	@Test
	void productInterestRateStartsAt5() {
		Assert.check(new Product().getInterestRate() == 5.0);
	}

	@Test
	void initialProductIsNotDisqualified() {
		Assert.check(!new Order().getProduct().isDisqualified());
	}

	@Test
	void productNotNull() {
		Assert.checkNonNull(new Order().getProduct());
	}

	@Test
	void emptyOrder() {
		Order order = new Order();
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().getName().equals(""));
		Assert.check(order.getProduct().getInterestRate() == 5.5); // Assumes person has creditScore of 0
		Assert.check(!order.getProduct().isDisqualified());
	}

	/**
	 * DROOLS RULES UNIT TESTS
	 * These tests verify the desired functionality as specified by the business requirements.
	 */
	@Test
	void badCreditMarkup() {
		Order order = new Order().setProduct(new Product()).setPerson(new Person().setCreditScore(719));
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().getInterestRate() == 5.5);
	}

	@Test
	void goodCreditDiscount() {
		Order order = new Order().setProduct(new Product()).setPerson(new Person().setCreditScore(720));
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().getInterestRate() == 4.7);
	}

	@Test
	void floridaDisqualification() {
		Order order = new Order().setPerson(new Person().setState("FL"));
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().isDisqualified());
	}

	@Test
	void goodCreditPersonPurchasesARMProduct() {
		Order order = new Order().setProduct(new Product().setName("7-1 ARM")).setPerson(new Person().setCreditScore(720));
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().getInterestRate() == 5.2);
		Assert.check(order.getProduct().getName().equals("7-1 ARM"));
	}

	@Test
	void badCreditPersonPurchasesARMProduct() {
		Order order = new Order().setProduct(new Product().setName("7-1 ARM")).setPerson(new Person().setCreditScore(600));
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().getInterestRate() == 6.0);
		Assert.check(order.getProduct().getName().equals("7-1 ARM"));
	}

	@Test
	void goodCreditPersonPurchasesARMProductFromFlorida() {
		Order order = new Order().setProduct(new Product().setName("7-1 ARM")).setPerson(new Person().setCreditScore(600).setState("FL"));
		session.insert(order);
		session.fireAllRules();
		Assert.check(order.getProduct().getInterestRate() == 6.0);
		Assert.check(order.getProduct().getName().equals("7-1 ARM"));
		Assert.check(order.getProduct().isDisqualified());
	}

	@Test
	void personFromTexasNotDisqualified() {
		Order order = new Order().setPerson(new Person().setState("TX"));
		session.insert(order);
		session.fireAllRules();
		Assert.check(!order.getProduct().isDisqualified());
	}

}

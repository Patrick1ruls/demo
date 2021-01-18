package com.example.demo;


import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

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
		assertEquals("", new Person().getState());
	}

	@Test
	void personCreditScoreHasInitialValue() {
		assertEquals(0, new Person().getCreditScore());
	}

	@Test
	void personNotNull() {
		assertNotNull(new Order().getPerson());
	}

	@Test
	void productNameHasInitialValue() {
		assertEquals("", new Product().getName());
	}

	@Test
	void productInterestRateStartsAt5() {
		assertEquals(5.0, new Product().getInterestRate(), 0.0);
	}

	@Test
	void initialProductIsNotDisqualified() {
		assertFalse(new Order().getProduct().isDisqualified());
	}

	@Test
	void productNotNull() {
		assertNotNull(new Order().getProduct());
	}

	@Test
	void emptyOrder() {
		Order order = new Order();
		session.insert(order);
		session.fireAllRules();
		assertEquals("", order.getProduct().getName());
		assertEquals(5.5, order.getProduct().getInterestRate(), 0.0); // Assumes person has creditScore of 0
		assertFalse(order.getProduct().isDisqualified());
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
		assertEquals(5.5, order.getProduct().getInterestRate(), 0.0);
	}

	@Test
	void goodCreditDiscount() {
		Order order = new Order().setProduct(new Product()).setPerson(new Person().setCreditScore(720));
		session.insert(order);
		session.fireAllRules();
		assertEquals(4.7, order.getProduct().getInterestRate(), 0.0);
	}

	@Test
	void floridaDisqualification() {
		Order order = new Order().setPerson(new Person().setState("FL"));
		session.insert(order);
		session.fireAllRules();
		assertTrue(order.getProduct().isDisqualified());
	}

	@Test
	void goodCreditPersonPurchasesARMProduct() {
		Order order = new Order().setProduct(new Product().setName("7-1 ARM")).setPerson(new Person().setCreditScore(720));
		session.insert(order);
		session.fireAllRules();
		assertEquals(5.2, order.getProduct().getInterestRate(), 0.0);
		assertEquals("7-1 ARM", order.getProduct().getName());
	}

	@Test
	void badCreditPersonPurchasesARMProduct() {
		Order order = new Order().setProduct(new Product().setName("7-1 ARM")).setPerson(new Person().setCreditScore(600));
		session.insert(order);
		session.fireAllRules();
		assertEquals(6.0, order.getProduct().getInterestRate(), 0.0);
		assertEquals("7-1 ARM", order.getProduct().getName());
	}

	@Test
	void goodCreditPersonPurchasesARMProductFromFlorida() {
		Order order = new Order().setProduct(new Product().setName("7-1 ARM")).setPerson(new Person().setCreditScore(600).setState("FL"));
		session.insert(order);
		session.fireAllRules();
		assertEquals(6.0, order.getProduct().getInterestRate(), 0.0);
		assertEquals("7-1 ARM", order.getProduct().getName());
		assertTrue(order.getProduct().isDisqualified());
	}

	@Test
	void personFromTexasNotDisqualified() {
		Order order = new Order().setPerson(new Person().setState("TX"));
		session.insert(order);
		session.fireAllRules();
		assertFalse(order.getProduct().isDisqualified());
	}

}

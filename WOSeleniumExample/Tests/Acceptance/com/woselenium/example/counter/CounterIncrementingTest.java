package com.woselenium.example.counter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.woselenium.AcceptanceTest;
import com.woselenium.example.ExampleTestAction;
import com.woselenium.example.pages.MainPage;

public class CounterIncrementingTest extends AcceptanceTest {

	private ExampleTestAction action = action(ExampleTestAction.class);

	@BeforeClass
	public static void setBaseUrl() {
		AcceptanceTest.BASE_URL = "http://localhost/cgi-bin/WebObjects/WOSeleniumExample.woa/-42400";
	}
	
	@Test
	public void counterStartsWithZero() {
		goTo(MainPage.class, action.mainPageAction());
		
		assertThat(currentPage(MainPage.class).counterValue(), is(0));
	}

	@Test
	public void counterIncrementing() {
		goTo(MainPage.class, action.mainPageAction())
		.incrementCounter();
		
		assertThat(currentPage(MainPage.class).counterValue(), is(1));
	}
}

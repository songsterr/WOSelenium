package com.woselenium;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class IsPresent extends TypeSafeMatcher<WebElement> {

	@Override
	public boolean matchesSafely(WebElement element) {
		try {
			element.getSize();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void describeTo(Description description) {
		description.appendText("present");
	}
	
	public static IsPresent present() {
		return new IsPresent();
	}

}

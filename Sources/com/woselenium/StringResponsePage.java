package com.woselenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class StringResponsePage extends Page {
	
	public StringResponsePage(WebDriver driver) {
		super(driver);
	}

	public String stringResponse() {
		return driver.findElement(By.tagName("body")).getText();
	}

}

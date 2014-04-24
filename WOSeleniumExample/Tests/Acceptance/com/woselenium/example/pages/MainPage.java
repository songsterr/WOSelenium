package com.woselenium.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.woselenium.Page;

public class MainPage extends Page {

	@FindBy(id = "incrementLink")
	private WebElement incrementLink;
	
	@FindBy(id = "counter")
	private WebElement counter;

	public MainPage(WebDriver driver) {
		super(driver);
	}

	public MainPage incrementCounter() {
		incrementLink.click();
		return this;
	}

	public int counterValue() {
		return Integer.parseInt(counter.getText());
	}

}

package com.woselenium.example.components;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

public class Main extends ERXComponent {
	public int counter;

	public Main(WOContext context) {
		super(context);
	}

	public WOActionResults incrementCounter() {
		counter++;
		return context().page();
	}
}

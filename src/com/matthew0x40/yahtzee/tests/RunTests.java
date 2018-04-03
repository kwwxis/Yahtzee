package com.matthew0x40.yahtzee.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTests {

	public static void main(String[] args) {
		testClass(TestHand.class);
		testClass(TestScoreboard.class);
	}
	
	public static void testClass(Class<?> cls) {
		Result result = JUnitCore.runClasses(cls);
		
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.getDescription() + ": " + failure.getException());
			System.out.println(failure.getTrace());
		}

		System.out.println((result.wasSuccessful() ? "Success" : "Failure") + " for [" + cls.getName() + "]");
	}
}

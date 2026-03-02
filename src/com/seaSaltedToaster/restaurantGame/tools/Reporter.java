package com.seaSaltedToaster.restaurantGame.tools;

import java.util.*;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;

public class Reporter {
	
	public static Map<Double, String> entries = new HashMap<Double, String>();
	
	private static double timeLast, timeFrame;
	private static String curRep;

	public static void report(String name) {
		entries.put(GLFW.glfwGetTime() - timeLast, curRep);
		
		Reporter.curRep = name;
		timeLast = GLFW.glfwGetTime();
	}
	
	public static void section(String name) {
		entries.put(-1., name);
	}
	
	public static void report() {
		double frameDur = GLFW.glfwGetTime() - timeFrame;
		System.out.print("[ ");
		for(Entry<Double, String> entry : entries.entrySet()) {
			if(entry.getKey() < 0) {
				System.out.print(entry.getValue().toUpperCase() + " = "); continue; }
			System.out.print(entry.getValue());
			float percentage = (float) ((entry.getKey() / frameDur) * 100);
			if(percentage >= 1)
				System.out.print(" : " + percentage + "% , ");
			else
				System.out.print(" : " + "<1." + "% , ");
		}
		System.out.println("}");
		System.out.println();
	}
	
	public static void clear() {
		entries.clear();
		timeFrame = GLFW.glfwGetTime();
	}
	
}

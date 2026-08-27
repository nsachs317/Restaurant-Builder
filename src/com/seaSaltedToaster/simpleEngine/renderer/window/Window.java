package com.seaSaltedToaster.simpleEngine.renderer.window;

import java.nio.DoubleBuffer;
import java.util.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;

import com.seaSaltedToaster.restaurantGame.tools.Reporter;

public class Window {
	
	public static List<WindowListener> listeners;
	public static long windowID;
	
	public static double DeltaTime;
	private static double lastFrameTime;
	
	public static double width, height;
	public static boolean fullscreen = true;
	
	public void createWindow(int width, int height, String title) {
		boolean initState = GLFW.glfwInit();
		Window.width = width;
		Window.height = height;
		
		if(initState == false) {
			throw new IllegalStateException("Could not create GLFW");
		}

		//Anti Aliasing
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);
		
		long arg01 = fullscreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL;
		windowID = GLFW.glfwCreateWindow(width, height, title, arg01, MemoryUtil.NULL);
		
		if(windowID == MemoryUtil.NULL) {
			throw new IllegalStateException("Cant make window :(");
		}
		
		GLFW.glfwSetWindowSizeCallback(windowID, new WindowResizer());
		
		GLFW.glfwMakeContextCurrent(windowID);
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(windowID);
		GL.createCapabilities();
		
		//Enable anti aliasing
		GL11.glEnable(GL13.GL_MULTISAMPLE);  
		
		Window.listeners = new ArrayList<WindowListener>();
	}
	
	public void updateWindow() {
		GLFW.glfwSwapBuffers(windowID);
		Reporter.report("Poll Inputs");
		GLFW.glfwPollEvents();
		Reporter.report("Cap Window");
		GLFW.glfwSwapInterval(1);
		Window.DeltaTime = getDelta();
	}
	
	private static double getDelta() {
		double currentTime = GLFW.glfwGetTime();
		double delta = (currentTime - lastFrameTime);
		lastFrameTime = currentTime;
		DeltaTime = delta;
		return delta;
	}
	
	public static double getCursorPosX() {
	    DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
	    GLFW.glfwGetCursorPos(windowID, posX, null);
	    return posX.get(0);
	}
	    
	public static double getCursorPosY() {
	    DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
	    GLFW.glfwGetCursorPos(windowID, null, posY);
	    return posY.get(0);
	}
	
	public static double getWidth() {
		return width;
	}

	public static double getHeight() {
		return height;
	}

	public static double getAspectRatio() {
		return getWidth() / getHeight();
	}
		
	public void closeWindow() {
		GLFW.glfwDestroyWindow(windowID);
		GLFW.glfwTerminate();
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(windowID);
	}

}


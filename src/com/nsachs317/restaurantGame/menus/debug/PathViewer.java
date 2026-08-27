package com.nsachs317.restaurantGame.menus.debug;

import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.PathfindingWorld;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.scale.AspectRatio;

public class PathViewer extends UiComponent {

	public PathViewer() {
		super(9);
		this.setScale(0.4f, 0.4f);
		this.setPosition(0.5f, 0.0f);
		
		this.getConstraints().setHeight(new AspectRatio(1));
	}
	
	public void updateInfo() {
		PathfindingWorld world = MainApp.restaurant.layers.get(0).getManager().getPathWorld();
		
		int gridSize = world.getWalkableWorld().length;
		boolean[][] map = world.getWalkableWorld();
				
		int[] pixels = new int[gridSize * gridSize];
		int index = 0;
		for(int x = 0; x < gridSize; x++) {
			for(int y = 0; y < gridSize; y++) {
				boolean val = map[x][y];
				if(val)
					pixels[index] = Color.WHITE.getRGB();
				else
					pixels[index] = Color.BLUE.getRGB();
				index++;
			}
		}
		
		int[] data = new int[gridSize * gridSize];
		  for (int i = 0; i < gridSize * gridSize; i++) {
			   int a = (pixels[i] & 0xff000000) >> 24;
			   int r = (pixels[i] & 0xff0000) >> 16;
			   int g = (pixels[i] & 0xff00) >> 8;
			   int b = (pixels[i] & 0xff);
	
			   data[i] = a << 24 | b << 16 | g << 8 | r;
		  }
	
		  int result = glGenTextures();
		  glBindTexture(GL_TEXTURE_2D, result);
		  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	
		  IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
		    .order(ByteOrder.nativeOrder()).asIntBuffer();
		  buffer.put(data).flip();
	
		  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, gridSize, gridSize, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		  glBindTexture(GL_TEXTURE_2D, 0);
		  this.setTexture(result);
	}

}

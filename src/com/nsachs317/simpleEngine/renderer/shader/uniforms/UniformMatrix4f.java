package com.nsachs317.simpleEngine.renderer.shader.uniforms;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import com.nsachs317.simpleEngine.utilities.Matrix4f;

public class UniformMatrix4f extends Uniform {

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(32);

	public UniformMatrix4f(String name) {
		super(name);
	}
	
	@Override
	public void loadValue(Object value)	{
		Matrix4f matrix = (Matrix4f) value;
		matrix.store(matrixBuffer);
		GL20.glUniformMatrix4fv(super.getLocation(), false, matrixBuffer);
	}

	
}

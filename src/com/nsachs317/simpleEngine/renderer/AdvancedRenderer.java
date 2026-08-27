package com.nsachs317.simpleEngine.renderer;

import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.componentArchitecture.ModelComponent;
import com.nsachs317.simpleEngine.models.Vao;

public class AdvancedRenderer extends Renderer {

	public AdvancedRenderer(Engine engine) {
		super(new AdvancedShader(), engine);
	}

	@Override
	public void prepare() {
		//Nothing
		super.prepareFrame(false);
	}

	@Override
	public void render(Object obj) {
		//Nothing
		if(obj instanceof Vao) {
			super.renderVao((Vao) obj);
		}
		if(obj instanceof Entity) {
			Entity entity = (Entity) obj;
			super.loadComponents(entity);
			ModelComponent model = (ModelComponent) entity.getComponent("Model");
			if(model == null || model.getMesh() == null) return;
			super.renderVao(model.getMesh());
		}
	}

	@Override
	public void endRender() {
		//Nothing
		super.endRendering();
	}

}

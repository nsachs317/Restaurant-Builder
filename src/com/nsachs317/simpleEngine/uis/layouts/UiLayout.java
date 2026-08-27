package com.nsachs317.simpleEngine.uis.layouts;

import java.util.List;

import com.nsachs317.simpleEngine.uis.UiComponent;

public abstract class UiLayout {

	public abstract void update(UiComponent component, List<UiComponent> children);
	
}
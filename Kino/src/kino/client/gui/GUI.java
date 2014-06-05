package kino.client.gui;

import java.util.ArrayList;
import java.util.List;

import kino.client.bindings.MenuControlGUIOutputs;

public abstract class GUI {
	private ScreenGUIHolder holder;
	public GUI previous;
	public GUI next;
	private boolean isOpen = false;
	private boolean isExposed = false;

	public GUI(ScreenGUIHolder paramHolder) {
		holder = paramHolder;
	}

	// Essentials
	public void draw(double interpolation) {
		drawElements(interpolation);
	}

	public ScreenGUIHolder getHolder() {
		return holder;
	}

	// Events
	public void onOpen() {
	}

	public void onClose() {
	}

	public void onExpose() {
	}

	public void onCover() {
	}

	public void onResize() {
	}

	public boolean onPress(int x, int y, MenuControlGUIOutputs.Action action) {
		return false;
	}

	public boolean onMove(int x, int y, MenuControlGUIOutputs.Action action) {
		return false;
	}

	public boolean onRelease(int x, int y, MenuControlGUIOutputs.Action action) {
		return false;
	}

	public void onControlDown(MenuControlGUIOutputs.Action action) {
	}

	public void onControlUp(MenuControlGUIOutputs.Action action) {
	}

	// Final Events
	public final void doOpen() {
		isOpen = true;
		onOpen();
		for (Element el : elements)
			el.onOpen();
	}

	public final void doClose() {
		isOpen = false;
		isExposed = false;
		onClose();
		for (Element el : elements)
			el.onClose();
	}

	public final void doExpose() {
		isExposed = true;
		onExpose();
	}

	public final void doCover() {
		isExposed = false;
		onCover();
	}

	public final void doPress(int x, int y, MenuControlGUIOutputs.Action action) {
		if (!onPress(x, y, action)) {
			for (Element el : elements) {
				if (el.pointIsInside(x, y))
					el.onPress(x, y);
			}
		}
	}

	public final void doMove(int x, int y, MenuControlGUIOutputs.Action action) {
		if (!onMove(x, y, action)) {
			for (Element el : elements) {
				if (el.pointIsInside(x, y))
					el.onMove(x, y);
			}
		}
	}

	public final void doRelease(int x, int y, MenuControlGUIOutputs.Action action) {
		if (!onRelease(x, y, action)) {
			for (Element el : elements) {
				if (el.pointIsInside(x, y))
					el.onRelease(x, y);
			}
		}
	}

	// States
	public boolean isOpen() {
		return isOpen;
	}

	public boolean isExposed() {
		return isExposed;
	}

	// Elements
	private List<Element> elements = new ArrayList<Element>();

	public final void addElement(Element el) {
		elements.add(el);
		el.onAdded();
		if (isOpen)
			el.onOpen();
	}

	public final void removeElement(Element el) {
		elements.remove(el);
		if (getHolder().getFocusElement() == el)
			getHolder().blurElement();
		el.onClose();
		el.onRemoved();
	}

	public final void drawElements(double interpolation) {
		for (Element e : elements)
			e.draw(interpolation);
	}
}
package kino.client.bindings;

import java.util.LinkedList;
import java.util.List;

public abstract class Input extends Put {
	private List<ControlBinding> registeredEventBindings = new LinkedList<ControlBinding>();

	/**
	 * Called by the profiles to add a control binding to respond to events that
	 * occur on this input
	 * 
	 * @see Input#unregisterBinding(ControlBinding)
	 * @param binding
	 *            The binding to invoke
	 */
	protected void registerBinding(ControlBinding binding) {
		if (!registeredEventBindings.contains(binding))
			registeredEventBindings.add(binding);
	}

	/**
	 * Removes a control binding
	 * 
	 * @see Input#registerBinding(ControlBinding)
	 * @param binding
	 *            The binding to remove
	 */
	protected void unregisterBinding(ControlBinding binding) {
		registeredEventBindings.remove(binding);
	}

	/**
	 * Invokes the registered bindings that are registered as events
	 */
	public void invokeEvent() {
		for (ControlBinding sb : registeredEventBindings) {
			//TODO
		}
	};
}

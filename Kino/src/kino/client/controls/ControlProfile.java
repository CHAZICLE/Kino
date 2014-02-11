package kino.client.controls;

import java.util.Iterator;

import kino.client.controls.bindings.ControlBinding;


public class ControlProfile implements Iterable<ControlBinding> {
	// Removal Management
	private boolean removed; public final void setRemoved() { removed = true; } public final boolean isRemoved() { return removed; }
	private ControlBinding first;
	class ControlBindingIterator implements Iterator<ControlBinding>
	{
		public ControlBindingIterator(ControlBinding first) {
			this.first = first;
		}
		private ControlBinding first;
		private ControlBinding previous;
		private ControlBinding current;
		@Override
		public boolean hasNext() {
			if(current==null)
				return first!=null;
			return current.next!=null;
		}
		@Override
		public ControlBinding next() {
			if(current==null)
				return current = first;
			previous = current;
			return current=current.next;
		}
		@Override
		public void remove() {
			if(current!=null && previous!=null)
				previous.next = current.next;
		}
	}
	@Override
	public Iterator<ControlBinding> iterator() {
		return new ControlBindingIterator(first);
	}
}

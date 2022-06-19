package com.inventory.utils;

import java.lang.reflect.Array;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;

public class JListFromList<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	public boolean empty = true;
	public JLabel emptyLabel = new JLabel("La lista esta vacia");
	JList<T> listView;

	public JListFromList(Class<T[]> klass, List<T> list) {
		super();
		listView = new JList<T>();
		setListData(klass, list);
	}
	
	public void setListData(Class<T[]> klass, List<T> list) {
		empty = list.size() <= 0;
		T[] array = klass.cast(Array.newInstance(klass.getComponentType(), list.size()));
		list.toArray(array);
		listView.setListData(array);
		
		if (empty) {
			remove(listView);
			add(emptyLabel);
		} else {
			remove(emptyLabel);
			add(listView);
		}
		invalidate();
	}
	public void setSelectedIndex(int n) { 
		listView.setSelectedIndex(n);
	}

	public T getSelectedValue() {
		return listView.getSelectedValue();
	}

	public void addListSelectionListener(ListSelectionListener l) {
		listView.addListSelectionListener(l);
	}
}

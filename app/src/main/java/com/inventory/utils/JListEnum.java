package com.inventory.utils;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;

public class JListEnum<T> extends JPanel {
	JList<T> listView;

	private static final long serialVersionUID = 1L;
	public JListEnum(Class<T> enumClass) {
		super();
		T[] values = enumClass.getEnumConstants();
		setLayout(new BorderLayout());
		listView = new JList<T>();
		listView.setListData(values);
		add(listView, BorderLayout.CENTER);
	}
	public T getSelectedValue() {
		return listView.getSelectedValue();
	}
}
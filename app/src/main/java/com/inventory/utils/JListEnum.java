package com.inventory.utils;

import javax.swing.JList;

public class JListEnum<T> extends JList<T>{
	private static final long serialVersionUID = 1L;

	public JListEnum(Class<T> enumClass) {
		super();
		T[] values = enumClass.getEnumConstants();
		setListData(values);
	}
}
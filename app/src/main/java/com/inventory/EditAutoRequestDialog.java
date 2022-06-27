package com.inventory;

import javax.swing.JFrame;
import javax.swing.JPanel;

class AutomaticRequest {
	Product product;
	Provider provider;
	int minumumAmount;
}

public class EditAutoRequestDialog<T> extends EditDialog<AutomaticRequest, T> {

	public EditAutoRequestDialog(JFrame owner, EditDialogListener<T> l) {
		super(owner, l);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String getDialogTitle() {
		return "Agregar Pedido Automatico";
	}

	@Override
	public JPanel getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AutomaticRequest getData() {
		// TODO Auto-generated method stub
		return null;
	}

}

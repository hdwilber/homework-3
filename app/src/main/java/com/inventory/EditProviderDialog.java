package com.inventory;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditProviderDialog<T> extends EditDialog<Provider, T> {
	JLabel labelName = new JLabel("Nombre:");
	JTextField inputName = new JTextField(10);
	
	public <Provider>EditProviderDialog(JFrame o, EditDialogListener<T> l) {
		super(o, l);
		setupContent();
	}

	public String getDialogTitle() {
		return "Agregar Proveedor";
	}

	@Override
	public void setData(Provider p) {
		super.setData(p);
		inputName.setText("");
		pack();
	}

	@Override
	public JPanel getForm() {
		JPanel form = new JPanel();
		GroupLayout layout = new GroupLayout(form);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        form.setLayout(layout);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING).
        		addComponent(labelName));
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING).
        		addComponent(inputName));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(labelName).addComponent(inputName));
        layout.setVerticalGroup(vGroup);	
        
        form.invalidate();
        return form;
	}

	@Override
	public Provider getData() {
		String name = inputName.getText();
		return new Provider(name, 2, 2);
	}
}

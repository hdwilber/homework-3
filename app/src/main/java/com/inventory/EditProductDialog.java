package com.inventory;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.inventory.utils.JListEnum;
import com.inventory.utils.JListFromList;

public class EditProductDialog<T> extends EditDialog<Product, T> {
	JLabel labelName = new JLabel("Nombre:");
	JTextField inputName;
	JLabel labelType = new JLabel("Tipo:");
	JLabel labelProvider = new JLabel("Provider:");
	JListEnum<ProductType> inputType;
	JListFromList<Provider> inputProvider;

	public <Product> EditProductDialog(JFrame o, EditDialogListener<T> l) {
		super(o, l);
		inputName = new JTextField(10);
		inputType = new JListEnum<ProductType>(ProductType.class);
	}

	public void setData(Product p, List<Provider> lp) {
		super.setData(p);
		inputName.setText("");
		
		inputProvider = new JListFromList<Provider>(Provider[].class, lp);
		setupContent();
		pack();
	}

	public JList<ProductType> getInputType() {
		 JList<ProductType> listInput = new JList<ProductType>();
		 List<ProductType> types = new ArrayList<ProductType>();
		 ProductType[] values = ProductType.values();
		 for(int i = 0; i < values.length; i++) {
			 types.add(values[i]);
		 }
		 listInput.setListData(values);
		 
		 return listInput;
	}

	public JPanel getForm() {
		JPanel form = new JPanel();
		GroupLayout layout = new GroupLayout(form);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        form.setLayout(layout);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelProvider)
        		.addComponent(labelName)
        		.addComponent(labelType)
        		);

        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(inputProvider)
        		.addComponent(inputName)
        		.addComponent(inputType)
        		);
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelProvider)
        		.addComponent(inputProvider));
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelName)
        		.addComponent(inputName));
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelType)
        		.addComponent(inputType));
        layout.setVerticalGroup(vGroup);	
        
        form.invalidate();
        return form;
	}
	
	public Product getData() {
		String name = inputName.getText();
		Product result = new Product(name, (ProductType)inputType.getSelectedValue());
		return result;
	}
	public void addToProvider() {
		Provider p = inputProvider.getSelectedValue();
		if (p != null) {
			p.addProduct(getData());
		}
	}
}

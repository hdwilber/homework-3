package com.inventory;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;

import com.inventory.utils.JListEnum;
import com.inventory.utils.JListFromList;

public class EditRequestDialog<T> extends EditDialog<Request, T> implements ListSelectionListener  {
	List<Provider> providers;
	List<Product> products;
	
	JLabel labelProduct = new JLabel("Producto:");
	JLabel labelProvider = new JLabel("Proveedor:");
	JLabel labelAmount = new JLabel("Cantidad:");
	JLabel labelPriority = new JLabel("Prioridad:");
	
	JListFromList<Product> productList;
	JListFromList<Provider> providerList;
	JFormattedTextField inputAmount;
	JListEnum<TransferRequestPriority> inputPriority;
	
	public <Request>EditRequestDialog(JFrame o, EditDialogListener<T> l) {
		super(o, l);
		setTitle("Pedir");
		inputPriority = new JListEnum<TransferRequestPriority>(TransferRequestPriority.class);
	}

	public String getDialogTitle() {
		return "Hacer Pedido";
	}

	public void setData(Request d, List<Provider> lp) {
		providers = lp;
		
		providerList = new JListFromList<Provider>(Provider[].class, lp);
		int index = 0;
		providerList.setSelectedIndex(index);
		Provider p = providerList.getSelectedValue();
		productList = new JListFromList<Product>(Product[].class, p.products);
		providerList.addListSelectionListener(this);

		super.setData(d);
		setupContent();
		pack();

	}

	@Override
	public JPanel getForm() {
		JPanel form = new JPanel();
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		inputAmount = new JFormattedTextField();
		
		GroupLayout layout = new GroupLayout(form);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        form.setLayout(layout);
        
//        providerList.setLayout(new BoxLayout(providerList, BoxLayout.X_AXIS));
//        providerList.setLayout(new FlowLayout());

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelProvider)
        		.addComponent(labelProduct)
        		.addComponent(labelAmount)
        		.addComponent(labelPriority)
        		);
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(providerList)
        		.addComponent(productList)
        		.addComponent(inputAmount)
        		.addComponent(inputPriority)
        		);
        layout.setHorizontalGroup(hGroup);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelProvider)
        		.addComponent(providerList));
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelProduct)
        		.addComponent(productList));
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelAmount)
        		.addComponent(inputAmount));
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelPriority)
        		.addComponent(inputPriority));
        layout.setVerticalGroup(vGroup);	
        
        form.invalidate();
        return form;
	}

	@Override
	public Request getData() {
		Provider provider = providerList.getSelectedValue();
		Product product = productList.getSelectedValue();
		int amount = (int) inputAmount.getValue();
		TransferRequestPriority priority = inputPriority.getSelectedValue();
		return new Request(provider, product, amount, priority);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		Provider p = providerList.getSelectedValue();
		productList.setListData(Product[].class, p.products);
		pack();
	}
}
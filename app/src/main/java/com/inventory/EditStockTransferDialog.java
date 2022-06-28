package com.inventory;

import java.text.NumberFormat;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;

import com.inventory.product.Product;
import com.inventory.taskrequest.InventoryStockTransfer;
import com.inventory.utils.JListFromList;

public class EditStockTransferDialog<T> extends EditDialog<InventoryStockTransfer, T> implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	List<Provider> providers;
	List<Product> products;

	JLabel labelProduct = new JLabel("Producto:");
	JLabel labelProvider = new JLabel("Proveedor:");
	JLabel labelAmount = new JLabel("Cantidad:");

	JListFromList<Product> productList;
	JListFromList<Provider> providerList;
	JFormattedTextField inputAmount;

	public EditStockTransferDialog(JFrame owner, EditDialogListener<T> l) {
		super(owner, l);
		setTitle("Pedir Stock");
	}

	public String getDialogTitle() {
		return "Transferir Stock";
	}
	public void setData(InventoryStockTransfer d, List<Provider> lp) {
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
		inputAmount = new JFormattedTextField(formatter);
		
		GroupLayout layout = new GroupLayout(form);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        form.setLayout(layout);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(labelProvider)
        		.addComponent(labelProduct)
        		.addComponent(labelAmount)
        		);
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(providerList)
        		.addComponent(productList)
        		.addComponent(inputAmount)
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
        layout.setVerticalGroup(vGroup);	
        
        form.invalidate();
        return form;
	}

	@Override
	public InventoryStockTransfer getData() {
		Product product = productList.getSelectedValue();
		int amount = (int) inputAmount.getValue();
		return new InventoryStockTransfer(product, amount);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Provider p = providerList.getSelectedValue();
		productList.setListData(Product[].class, p.products);
		pack();
	}

}

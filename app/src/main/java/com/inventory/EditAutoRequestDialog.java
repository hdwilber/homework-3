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
import com.inventory.taskrequest.TaskRequestPriority;
import com.inventory.utils.JListEnum;
import com.inventory.utils.JListFromList;

class AutomaticRequest {
	Product product;
	Provider provider;
	int minumumAmount;
	int amount;
	TaskRequestPriority priority;
	
	public AutomaticRequest(Provider p, Product product, TaskRequestPriority pri, int min, int a) {
		this.product = product;
		provider = p;
		minumumAmount = min;
		amount = a;
		priority = pri;
	}
}

public class EditAutoRequestDialog<T> extends EditDialog<AutomaticRequest, T> implements ListSelectionListener {
	List<Provider> providers;
	List<Product> products;
	
	JLabel labelProduct = new JLabel("Producto:");
	JLabel labelProvider = new JLabel("Proveedor:");
	JLabel labelMinAmount = new JLabel("Cantidad Minima:");
	JLabel labelAmount = new JLabel("Cantidad A Pedir:");
	JLabel labelPriority = new JLabel("Prioridad:");

	JListFromList<Product> productList;
	JListFromList<Provider> providerList;
	JFormattedTextField inputMinAmount;
	JFormattedTextField inputAmount;
	JListEnum<TaskRequestPriority> inputPriority;

	public EditAutoRequestDialog(JFrame owner, EditDialogListener<T> l) {
		super(owner, l);
		setTitle("Agregar Pedido Automatico");
		inputPriority = new JListEnum<TaskRequestPriority>(TaskRequestPriority.class);
	}

	private static final long serialVersionUID = 1L;

	public void setData(List<Provider> lp, Product product) {
		providers = lp;
		
		providerList = new JListFromList<Provider>(Provider[].class, lp);
		int index = 0;
		providerList.setSelectedIndex(index);
		Provider p = providerList.getSelectedValue();
		productList = new JListFromList<Product>(Product[].class, p.products);
		if (product != null) {
			int productIndex = p.products.indexOf(product);
			productList.setSelectedIndex(productIndex);
		}

		providerList.addListSelectionListener(this);

		super.setData(null);
		setupContent();
		pack();

	}

	@Override
	public String getDialogTitle() {
		return "Agregar Pedido Automatico";
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
		inputMinAmount = new JFormattedTextField(formatter);
		
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
        		.addComponent(labelMinAmount)
        		.addComponent(labelAmount)
        		.addComponent(labelPriority)
        		);
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(providerList)
        		.addComponent(productList)
        		.addComponent(inputMinAmount)
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
        		.addComponent(labelMinAmount)
        		.addComponent(inputMinAmount));
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
	public AutomaticRequest getData() {
		Provider provider = providerList.getSelectedValue();
		Product product = productList.getSelectedValue();
		int amount = (int) inputAmount.getValue();
		int minAmount = (int) inputMinAmount.getValue();
		TaskRequestPriority priority = inputPriority.getSelectedValue();
		return new AutomaticRequest(provider, product, priority, minAmount, amount);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Provider p = providerList.getSelectedValue();
		productList.setListData(Product[].class, p.products);
		pack();
	}
}
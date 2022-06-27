package com.inventory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

class ActionsColumnRenderer extends JPanel implements TableCellRenderer  {
	private static final long serialVersionUID = 1L;
	JPanel panel;
	private JButton displayStockButton;
	private JButton deleteButton;
	private JButton requestButton;

	public ActionsColumnRenderer() {
		super();
		setLayout(new BorderLayout());
		panel = new JPanel();
		displayStockButton = new JButton("Ver Stock");
		deleteButton = new JButton("Borrar");
		requestButton = new JButton("Pedir");
		panel.add(displayStockButton);
		panel.add(requestButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.CENTER);
	}

	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4,
			int arg5) {
		return this;
	}
}

class ProductListModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	List<Product> list;
	public ProductListModel(List<Provider> p) {
		super();
		list = new ArrayList<Product>();
		Iterator<Provider> iter = p.iterator();
		while(iter.hasNext()) {
			list.addAll(iter.next().products);
		}
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}
	
	public Product getRowAt(int i) {
		return list.get(i);
	}


	@Override
	public String getColumnName(int column) {
		String name = null;
		switch(column) {
		case 0:
			name = "Nombre";
			break;
		case 1:
			name =  "Descripcion";
			break;
		case 2:
			name = "Tipo";
			break;
		}
		return name;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 3;
	}

	@Override
	public Object getValueAt(int row, int column) {
		Product p = list.get(row);
		if(p != null) {
			if (column == 0) { 
				return p.getName();
			} else if (column == 1) {
				return "-";
			} else if (column == 2) {
				return p.type;
			}
		}
		// TODO Auto-generated method stub
		return null;
	}
}

class StockProductDetails extends JPanel {
	private static final long serialVersionUID = 1L;
	List<InventoryItem> items;
	Inventory inventory;
	Product product;
	JLabel labelProduct;
	JLabel labelCount;
	
	public StockProductDetails(Inventory i) {
		super();
		inventory = i;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		labelCount = new JLabel();
		labelProduct = new JLabel();
		add(labelProduct);
		add(labelCount);
	}

	public void setProduct(Product p) {
		if (p != null && product != p) {
			product = p;
			List<InventoryItem> items = inventory.depot.getCurrentStockByProduct(p);
			Iterator<InventoryItem> iter = items.iterator();
			int count = 0;
			while(iter.hasNext()) {
				InventoryItem item = iter.next();
				count += item.amount;
			}
			labelProduct.setText(p.toString());
			labelCount.setText("Total en stock: " + count);
		}
	}
}

public class ProductListTable extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	JTable table;
	ProductListModel model;
	Inventory inventory;
	StockProductDetails details;
	private final String ACTION_DISPLAY_STOCK = "DISPLAY_STOCK";
	private final String ACTION_REQUEST = "ACTION_REQUEST";
	private final String ACTION_DELETE = "ACTION_DELETE";

	public ProductListTable(Inventory i, List<Provider> providers) {
		super();
		inventory = i;
		details = new StockProductDetails(i);
		model = new ProductListModel(providers);
		table = new JTable(model);
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		JScrollPane scroll = new JScrollPane(table);
		setBorder(new EmptyBorder(12, 12, 12, 12));
		
		JPanel controller= new JPanel();
		controller.setLayout(new BoxLayout(controller, BoxLayout.Y_AXIS));
		controller.add(getControllers());
		controller.add(Box.createVerticalStrut(16));

		add(controller, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(details,BorderLayout.SOUTH);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				Product p = model.getRowAt(table.getSelectedRow());
				details.setProduct(p);
//				System.out.println(model.getRowAt(table.getSelectedRow()));
//				List<InventoryItem> stock = inventory.depot.getCurrentStockByProduct(p);
//				System.out.println(stock);
			}
		});

	}

	public JPanel getControllers() {
		JPanel p = new JPanel();
		JButton displayStockButton = new JButton("Ver Stock");
		JButton deleteButton = new JButton("Borrar");
		JButton requestButton = new JButton("Pedir");
		
		displayStockButton.setActionCommand(ACTION_DISPLAY_STOCK);
		displayStockButton.addActionListener(this);
		requestButton.setActionCommand(ACTION_REQUEST);
		requestButton.addActionListener(this);

		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(displayStockButton);
		p.add(Box.createHorizontalStrut(8));
		p.add(requestButton);
		p.add(Box.createHorizontalStrut(8));
		p.add(deleteButton);
		p.add(Box.createHorizontalGlue());
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == ACTION_DISPLAY_STOCK) {
			System.out.println("DISPLAY STOCK: " + table.getSelectedRow());
		} else if(event.getActionCommand() == ACTION_REQUEST) {
			System.out.println("REQUEST: " + table.getSelectedRow());
		} else if(event.getActionCommand() == ACTION_DELETE) {
			System.out.println("DELETE: " + table.getSelectedRow());
		}
	}
}

package com.inventory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
				return p.name;
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

public class ProductListTable extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	JTable table;
	ProductListModel model;
	private final String ACTION_DISPLAY_STOCK = "DISPLAY_STOCK";
	private final String ACTION_REQUEST = "ACTION_REQUEST";
	private final String ACTION_DELETE = "ACTION_DELETE";

	public ProductListTable(List<Provider> providers) {
		super();
		model = new ProductListModel(providers);
		table = new JTable(model);
		setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(table);
		add(scroll, BorderLayout.CENTER);
		add(getControllers(), BorderLayout.SOUTH);
	}

	public JPanel getControllers() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		JButton displayStockButton = new JButton("Ver Stock");
		JButton deleteButton = new JButton("Borrar");
		JButton requestButton = new JButton("Pedir");
		
		displayStockButton.setActionCommand(ACTION_DISPLAY_STOCK);
		displayStockButton.addActionListener(this);
		requestButton.setActionCommand(ACTION_REQUEST);
		requestButton.addActionListener(this);

		p.add(displayStockButton);
		p.add(requestButton);
		p.add(deleteButton);
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

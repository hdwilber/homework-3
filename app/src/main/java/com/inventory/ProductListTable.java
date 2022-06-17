package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellEditor;

import com.inventory.utils.WrapLayout;

class ActionsColumnRenderer extends JPanel implements TableCellRenderer  {
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

class ActionsColumnEditor extends DefaultCellEditor implements ActionListener, TableCellRenderer {
	private JTable table;
	private final String ACTION_DISPLAY_STOCK = "DISPLAY_STOCK";
	private final String ACTION_REQUEST = "ACTION_REQUEST";
	private final String ACTION_DELETE = "ACTION_DELETE";
	private int selectedRow = -1;
	private CellEditorListener listener;
	private JPanel editor;
	private JPanel panel;
	private JPanel root;

	public ActionsColumnEditor() {
		super(new JComboBox());
		root = new JPanel();
		editor = createPanel(true);
		panel = createPanel(false);
	}
	
	@Override
	public Component getComponent() {
		return editor;
	}

	public JPanel createPanel(boolean toEdit) {
		JPanel p = new JPanel();
		p.setLayout(new WrapLayout());
		JButton displayStockButton = new JButton("Ver Stock");
		JButton deleteButton = new JButton("Borrar");
		JButton requestButton = new JButton("Pedir");
		
		if (toEdit) {
			displayStockButton.setActionCommand(ACTION_DISPLAY_STOCK);
			displayStockButton.addActionListener(this);
			requestButton.setActionCommand(ACTION_REQUEST);
			requestButton.addActionListener(this);
		}
		p.add(displayStockButton);
		p.add(requestButton);
		p.add(deleteButton);
		return p;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4,
			int arg5) {
		return panel;
	}

	@Override
	public Component getTableCellEditorComponent(JTable t, Object value, boolean isSelected, int row, int column)
	{
//		table = t;
//		selectedRow = row;
//		JPanel cp = new JPanel();
//		cp.add(editor);
//		return cp;
		return editor;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == ACTION_DISPLAY_STOCK) {
			System.out.println("DISPLAY STOCK: " + selectedRow);
		} else if(event.getActionCommand() == ACTION_REQUEST) {
			System.out.println("REQUEST: " + selectedRow);
		}
	}
}

class ProductListModel extends AbstractTableModel {
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
		}
	}
}

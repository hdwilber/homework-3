package com.inventory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class InventoryManager<T> extends JFrame implements EditDialogListener<T> {
	EditProductDialog<T> editProductDialog;
	EditRequestDialog<T> editRequestDialog;
	EditProviderDialog<T> editProviderDialog;
	EditStockTransferDialog<T> editStockTransferDialog;
	ProductListTable productsTable;
	Inventory inventory;
	InventoryStatus status;

	public static void main(String args[]) {
		InventoryManager manager = new InventoryManager();
	}

	public InventoryManager() {
		super("Inventory Manager");
		editProductDialog = new EditProductDialog<T>(this, this);
		editRequestDialog = new EditRequestDialog<T>(this, this);
		editProviderDialog = new EditProviderDialog<T>(this, this);
		editStockTransferDialog = new EditStockTransferDialog<T>(this, this);

		inventory = new Inventory();
		status = new InventoryStatus(inventory);
		productsTable = new ProductListTable(inventory.providers);

		setupContent();
	}
	public JPanel getTopMenu() {
		JButton newProductButton = new JButton("Agregar producto");
		JButton newRequestButton = new JButton("Nuevo pedido");
		JButton transferButton = new JButton("Transferir");
		JButton addProvider = new JButton("Agregar Proveedor");

		newProductButton.addActionListener(event -> {
			editProductDialog.setData(null, inventory.providers);
			editProductDialog.setVisible(true);
		});
		newRequestButton.addActionListener(l -> {
			editRequestDialog.setData(null, inventory.providers);
			editRequestDialog.setVisible(true);
		});
		addProvider.addActionListener(event -> {
			editProviderDialog.setData(null);
			editProviderDialog.setVisible(true);
		});
		transferButton.addActionListener(l -> {
			editStockTransferDialog.setData(null, inventory.providers);
			editStockTransferDialog.setVisible(true);
		});

		JPanel topButtons = new JPanel();
		topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.X_AXIS));
		topButtons.add(newProductButton);
		topButtons.add(newRequestButton);
		topButtons.add(transferButton);
		topButtons.add(addProvider);

		return topButtons;
	}

	public void setupContent() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}
		});    

		setLayout(new BorderLayout());
		add(getTopMenu(), BorderLayout.NORTH);
//		add(productsTable, BorderLayout.CENTER);
		add(status, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	
	@Override
	public void onEditDialogComplete(EditDialog dialog, T original, T data) {
		if (data instanceof Product) { 
			((EditProductDialog)dialog).addToProvider();
		} else if (data instanceof Request) {
			inventory.addRequest((Request)original, (Request)data);
		} else if (data instanceof Provider) {
			inventory.addProvider((Provider)original, (Provider)data);
		} else if (data instanceof StockTransfer) {
			inventory.addStockTransfer((StockTransfer)original, (StockTransfer)data);
		}
		dialog.setVisible(false);
		
	}
	@Override
	public void onEditDialogCancel(EditDialog dialog, T original) {
		dialog.setVisible(false);
	}
}
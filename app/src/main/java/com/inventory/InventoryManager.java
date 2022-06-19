package com.inventory;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public class InventoryManager<T> extends JFrame implements EditDialogListener<T> {
	private static final long serialVersionUID = 1L;
	EditProductDialog<T> editProductDialog;
	EditRequestDialog<T> editRequestDialog;
	EditProviderDialog<T> editProviderDialog;
	EditStockTransferDialog<T> editStockTransferDialog;
	ProductListTable productsTable;
	Inventory inventory;
	InventoryStatus status;
	DepotStatus depotStatus;

	public static void main(String args[]) {
		new InventoryManager<Object>();
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
		depotStatus = new DepotStatus(inventory.depot);

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
		
		topButtons.setBorder(new EmptyBorder(12, 12, 12, 12));

		return topButtons;
	}

	public void setupContent() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}
		});    
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel dashboard = new JPanel();
		JPanel products = new JPanel();
		productsTable.setBorder(new EmptyBorder(12, 12, 12, 12));
		status.setBorder(new EmptyBorder(12, 12, 12, 12));
		depotStatus.setBorder(new EmptyBorder(12, 12, 12, 12));
		tabbedPane.addTab("Dashboard", dashboard);
		tabbedPane.addTab("Productos", productsTable);
		tabbedPane.addTab("Estadisticas", new JPanel());
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));
		dashboard.add(getTopMenu());
		dashboard.add(status);
		dashboard.add(depotStatus);
		
		getContentPane().add(tabbedPane);
		
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
			inventory.store.addStockTransfer((StockTransfer)data);
		}
		dialog.setVisible(false);
		
	}
	@Override
	public void onEditDialogCancel(EditDialog dialog, T original) {
		dialog.setVisible(false);
	}
}
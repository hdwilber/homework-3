package com.inventory;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.inventory.product.Product;
import com.inventory.taskrequest.InventoryRequest;
import com.inventory.taskrequest.InventoryStockTransfer;

public class InventoryManager<T> extends JFrame implements EditDialogListener<T> {
	private static final long serialVersionUID = 1L;
	EditProductDialog<T> editProductDialog;
	EditRequestDialog<T> editRequestDialog;
	EditProviderDialog<T> editProviderDialog;
	EditStockTransferDialog<T> editStockTransferDialog;
	ProductListTable productsTable;
	Inventory inventory;
	InventoryStatus status;
	List<DepotStatus> depotStatusList;

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
		productsTable = new ProductListTable(inventory, this, inventory.providers);
		depotStatusList = new ArrayList<DepotStatus>();

		inventory.depots.forEach(depot -> {
			DepotStatus dst = new DepotStatus(depot);
			dst.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY, 2), new EmptyBorder(12, 12, 12, 12)));
			depotStatusList.add(dst);
		});

		setupContent();
	}
	public JPanel getTopMenu() {
		JButton newProductButton = new JButton("Agregar producto");
		JButton newRequestButton = new JButton("Pedir");
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

		JPanel topMenu = new JPanel();
		topMenu.setLayout(new BoxLayout(topMenu, BoxLayout.X_AXIS));
		topMenu.add(newRequestButton);
		topMenu.add(Box.createHorizontalStrut(8));
		topMenu.add(transferButton);
		topMenu.add(Box.createHorizontalGlue());
		topMenu.add(newProductButton);
		topMenu.add(Box.createHorizontalStrut(8));
		topMenu.add(addProvider);
		
		topMenu.setBorder(new EmptyBorder(12, 12, 12, 12));
		return topMenu;
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
		status.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY, 2), new EmptyBorder(12, 12, 12, 12)));
		tabbedPane.addTab("Dashboard", dashboard);
		tabbedPane.addTab("Productos", productsTable);
		tabbedPane.addTab("Estadisticas", new JPanel());
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.PAGE_AXIS));

		dashboard.add(Box.createVerticalStrut(8));
		dashboard.add(getTopMenu());
		dashboard.add(Box.createVerticalStrut(8));
		dashboard.add(status);
		dashboard.add(Box.createVerticalStrut(8));


		dashboard.add(Box.createVerticalStrut(8));
		
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.add(tabbedPane);

		JPanel depotsPanel = new JPanel();
		JScrollPane depots = new JScrollPane(depotsPanel);
		depotStatusList.forEach(dst -> {
			depotsPanel.add(dst);
		});
		root.add(depots);

		getContentPane().add(root);
		pack();
		setVisible(true);
	}

	public void showEditRequestDialog(Product p) {
		editRequestDialog.setData(null, inventory.providers, p);
		editRequestDialog.setVisible(true);
	}
	
	@Override
	public void onEditDialogComplete(EditDialog dialog, T original, T data) {
		if (data instanceof Product) {
//			((EditProductDialog)dialog).addToProvider();
		} else if (data instanceof InventoryRequest) {
			inventory.addRequest((InventoryRequest)original, (InventoryRequest)data);
		} else if (data instanceof Provider) {
			inventory.addProvider((Provider)original, (Provider)data);
		} else if (data instanceof InventoryStockTransfer) {
			inventory.addStockTransfer((InventoryStockTransfer)data);
		}
		dialog.setVisible(false);
		
	}
	@Override
	public void onEditDialogCancel(EditDialog dialog, T original) {
		dialog.setVisible(false);
	}
}
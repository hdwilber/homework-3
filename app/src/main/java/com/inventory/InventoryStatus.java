package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

class PathStatusItem extends JLabel implements ListCellRenderer<TransferRequest> {
	public PathStatusItem(Image icon) {
		super(new ImageIcon(icon));
		setBorder(new EmptyBorder(8, 8, 8, 8));
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends TransferRequest> list, TransferRequest value,
			int index, boolean isSelected, boolean cellHasFocus) {
		return this;
	}
}

class PathStatusModel extends AbstractListModel<TransferRequest> {
	List<TransferRequest> list;
	public PathStatusModel(List<TransferRequest> l) {
		list = l;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public TransferRequest getElementAt(int index) {
		return list.get(index);
	}
	
}
class PathStatus extends JList<TransferRequest> {
	Image icon;
	PathStatusModel requestsModel;

	public PathStatus(String ic, List<TransferRequest> l) {
		super();
		requestsModel = new PathStatusModel(l);
//		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		Image image = new ImageIcon(getClass().getResource(ic)).getImage();
		icon = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
		PathStatusItem renderer = new PathStatusItem(icon);
		setCellRenderer(renderer);
		setModel(requestsModel);
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(1);
	}
}

class TransferStatus extends JPanel {
	public TransferStatus(String ts, String bs, String ticon, String bicon, List<TransferRequest> ti, List<TransferRequest> bi) { 
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		this.setMaximumSize(new Dimension(1000, 150));
		this.setPreferredSize(new Dimension(1000, 150));

		JPanel sep = new JPanel();
		sep.setBackground(Color.GRAY);
		sep.setPreferredSize(new Dimension(1000, 5));
		sep.setMaximumSize(new Dimension(1000, 5));

		JLabel topLabel = new JLabel(ts);
		JLabel bottomLabel = new JLabel(bs);

		add(topLabel);
		add(new JLabel(new ImageIcon(getClass().getResource("/icons/arrow-left.png"))));
		add(new PathStatus(ticon, ti ));
		add(sep);
		add(new PathStatus(bicon, bi));
		add(new JLabel(new ImageIcon(getClass().getResource("/icons/arrow-right.png"))));
		add(bottomLabel);
	}

}
public class InventoryStatus extends JPanel {
	StoreStatus storeStatus;
	StorageStatus storageStatus;
	ProviderStatus providerStatus;
	TransferStatus leftStatus;
	TransferStatus rightStatus;

	public InventoryStatus(Inventory inventory) {
		providerStatus = new ProviderStatus(inventory.providers);
		storageStatus = new StorageStatus();
		storeStatus = new StoreStatus();
		leftStatus = new TransferStatus(
				"Pedido",
				"Orden de Entrada",
				"/icons/request.png",
				"/icons/entering.png",
				inventory.requests,
				inventory.inboundOrders
				);
		rightStatus = new TransferStatus(
				"Transferencia", "Orden de Salida", "/icons/request.png", "/icons/leaving.png",
				inventory.stockTransfers,
				inventory.outboundOrders
				);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(providerStatus);
		add(leftStatus);
		add(storageStatus);
		add(rightStatus);
		add(storeStatus);
	}
}

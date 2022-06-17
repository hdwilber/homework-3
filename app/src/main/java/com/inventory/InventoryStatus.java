package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.Box;
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
		setToolTipText(value.id);
		return this;
	}
}

class PathStatusModel extends AbstractListModel<TransferRequest> {
	List<TransferRequest> list;
	boolean right;
	public PathStatusModel(List<TransferRequest> l, boolean r) {
		list = l;
		right = r;
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public TransferRequest getElementAt(int index) {
		return list.get(right ? list.size() - index -1 : index);
	}
	
	public void setList(List<TransferRequest> l) {
		list = l;
		fireContentsChanged(list, getSize(), getSize());
	}
	
}
class PathStatus extends JPanel {
	Image icon;
	PathStatusModel requestsModel;
	JList<TransferRequest> listView;
	boolean right;

	public PathStatus(String ic, List<TransferRequest> l, boolean r) {
		super();
		requestsModel = new PathStatusModel(l, r);
		setMaximumSize(new Dimension(2000, 50));
		setLayout(new BorderLayout());
		Image image = new ImageIcon(getClass().getResource(ic)).getImage();
		icon = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
		PathStatusItem renderer = new PathStatusItem(icon);
		listView = new JList<TransferRequest>();
		listView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listView.setAlignmentX(Component.RIGHT_ALIGNMENT);
		listView.setCellRenderer(renderer);
		listView.setModel(requestsModel);
		listView.setVisibleRowCount(1);
		listView.setBackground(getBackground());
		
		System.out.println("THE LIST: " + l.toString());
		right = r;
		add(listView, r ? BorderLayout.EAST : BorderLayout.WEST);
	}
	public void setList(List<TransferRequest> l) {
		requestsModel.setList(l);
	}
}

class TransferStatus extends JPanel implements InventoryEventListener {
	PathStatus topPath;
	PathStatus bottomPath;
	public TransferStatus(String ts, String bs, String ticon, String bicon, List<TransferRequest> ti, List<TransferRequest> bi) { 
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		JPanel sep = new JPanel();
		sep.setMaximumSize(new Dimension(2000, 5));
		sep.setBackground(Color.RED);

		topPath = new PathStatus(ticon, ti, false);
		bottomPath = new PathStatus(bicon, bi, false);

		JLabel topLabel = new JLabel(ts);
		JLabel bottomLabel = new JLabel(bs);
		add(topLabel);
		add(new JLabel(new ImageIcon(getClass().getResource("/icons/arrow-left.png"))));
		add(topPath);
		add(sep);
		add(bottomPath);
		add(new JLabel(new ImageIcon(getClass().getResource("/icons/arrow-right.png"))));
		add(bottomLabel);
	}

	@Override
	public void onTransferRequestUpdate(String name, List<TransferRequest> l) {
	System.out.println("EVENT HAPPENDED: " + name);
		if (name.equals("TOP")) {
			topPath.setList(l);
		} else if (name.equals("BOTTOM")) {
			bottomPath.setList(l);
		}
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
		
		inventory.addInventoryEventListener(leftStatus);
		inventory.addInventoryEventListener(rightStatus);
	}
}

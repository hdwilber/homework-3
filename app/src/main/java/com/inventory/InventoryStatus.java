package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class PathStatusItem extends JLabel implements ListCellRenderer<TransferRequest> {
	private static final long serialVersionUID = 1L;
	public static Color[] palette = {
			new Color(Integer.valueOf("5f66fb", 16)),
			new Color(Integer.valueOf("00c7e0", 16)),
			new Color(Integer.valueOf("5ed599", 16)),
			new Color(Integer.valueOf("b5c85f", 16)),
			new Color(Integer.valueOf("d79650", 16)),
			new Color(Integer.valueOf("d16b6b", 16)),
	};

	public PathStatusItem(Image icon) {
		super(new ImageIcon(icon));
		setOpaque(true);
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends TransferRequest> list, TransferRequest value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Color c = palette[value.priority.getValue()];
		setToolTipText(value.id);
		setBorder(BorderFactory.createCompoundBorder(new LineBorder(c, 2), new EmptyBorder(8, 8, 8, 8)));
		invalidate();
		return this;
	}
}

class PathStatusModel extends AbstractListModel<TransferRequest> {
	private static final long serialVersionUID = 1L;
	PriorityBlockingQueue<TransferRequest> list;
	TransferRequest[] arrayList;
	boolean right;
	public PathStatusModel(PriorityBlockingQueue<TransferRequest> l, boolean r) {
		list = l;
		right = r;
		updateList();
		arrayList = list.toArray(TransferRequest[]::new);
	}

	@Override
	public int getSize() {
		return arrayList.length;
	}

	@Override
	public TransferRequest getElementAt(int index) {
		if (index < arrayList.length) {
			return arrayList[right ? arrayList.length - index -1 : index];
		}
		return null;
	}
	
	public void updateList() {
		arrayList = list.toArray(TransferRequest[]::new);
		Arrays.sort(arrayList);
		fireContentsChanged(this, getSize(), getSize());
	}
}

class PathStatus extends JPanel {
	private static final long serialVersionUID = 1L;
	Image icon;
	PathStatusModel requestsModel;
	JList<TransferRequest> listView;
	boolean right;
	String iconResource;

	public PathStatus(String ic, PriorityBlockingQueue<TransferRequest> l, boolean r) {
		super();
		requestsModel = new PathStatusModel(l, r);
		iconResource = ic;
		setMaximumSize(new Dimension(2000, 50));
		setLayout(new BorderLayout());
		Image image = new ImageIcon(getClass().getResource(ic)).getImage();
		icon = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
		PathStatusItem renderer = new PathStatusItem(icon);
		listView = new JList<TransferRequest>();
		listView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listView.setAlignmentX(r ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
		listView.setCellRenderer(renderer);
		listView.setModel(requestsModel);
		listView.setVisibleRowCount(1);
		listView.setBackground(getBackground());
		
		right = r;
		add(listView, r ? BorderLayout.EAST : BorderLayout.WEST);
	}

	public void updateList() {
		requestsModel.updateList();
	}
}

class TransferStatus extends JPanel implements InventoryEventListener {
	private static final long serialVersionUID = 1L;
	PathStatus topPath;
	PathStatus bottomPath;
	public TransferStatus(String ts, String bs, String ticon, String bicon, PriorityBlockingQueue<TransferRequest> ti, PriorityBlockingQueue<TransferRequest> bi) { 
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		JPanel sep = new JPanel();
		sep.setMaximumSize(new Dimension(2000, 5));
		sep.setBackground(Color.RED);

		topPath = new PathStatus(ticon, ti, false);
		bottomPath = new PathStatus(bicon, bi, true);

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
	public void onTransferRequestUpdate() {
		topPath.updateList();
		bottomPath.updateList();
	}
}

public class InventoryStatus extends JPanel {
	private static final long serialVersionUID = 1L;
	StoreStatus storeStatus;
	StorageStatus storageStatus;
	ProviderStatus providerStatus;
	TransferStatus leftStatus;
	TransferStatus rightStatus;

	public InventoryStatus(Inventory inventory) {
		Provider p = inventory.providers.get(0);
		providerStatus = new ProviderStatus(p);
		storageStatus = new StorageStatus(inventory);
		storeStatus = new StoreStatus(inventory.store);
		leftStatus = new TransferStatus(
				"Pedido",
				"Orden de Entrada",
				"/icons/request.png",
				"/icons/inboundorder.png",
				p.requestsQueue,
				inventory.inboundOrders
				);

		rightStatus = new TransferStatus(
				"Transferencia",
				"Orden de Salida",
				"/icons/request.png",
				"/icons/outboundorder.png",
				inventory.stockTransfers,
				inventory.store.outboundOrders
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

package com.inventory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.inventory.taskrequest.TaskExecutor;
import com.inventory.taskrequest.TaskRequest;
import com.inventory.taskrequest.TaskRequestEventListener;

class PathStatusItem extends JPanel implements ListCellRenderer<TaskRequest> {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	public static Color[] palette = {
			new Color(Integer.valueOf("5f66fb", 16)),
			new Color(Integer.valueOf("00c7e0", 16)),
			new Color(Integer.valueOf("5ed599", 16)),
			new Color(Integer.valueOf("b5c85f", 16)),
			new Color(Integer.valueOf("d79650", 16)),
			new Color(Integer.valueOf("d16b6b", 16)),
	};

	public PathStatusItem(Image i) {
		super();
		icon = new ImageIcon(i);
//		setOpaque(false);
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends TaskRequest> list, TaskRequest value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			removeAll();
			Color c = palette[value.getPriority().getValue()];
			Font font = getFont().deriveFont(1, 10.0f);
			setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.WHITE, 1),
					BorderFactory.createCompoundBorder(new LineBorder(c, 1), new EmptyBorder(4, 4, 4, 4))));
			value.setContentInfo(this, icon, font);
		} else {
		}
		invalidate();
		return this;
	}
}

class PathStatusModel extends AbstractListModel<TaskRequest> {
	private static final long serialVersionUID = 1L;
	PriorityBlockingQueue<TaskRequest> list;
	TaskRequest[] arrayList;
	boolean right;
	public PathStatusModel(PriorityBlockingQueue<TaskRequest> l, boolean r) {
		list = l;
		right = r;
		updateList();
	}

	public void getArrayList() {
		TaskRequest[] aux = list.toArray(TaskRequest[]::new);
		Arrays.sort(aux);
		if (aux.length > 0) {
			if (aux.length >= 15) {
				arrayList = Arrays.copyOfRange(aux, 0, 15);
			} else {
				arrayList = aux;
			}
		} else {
			arrayList = aux;
		}
	}

	@Override
	public int getSize() {
		return arrayList.length;
	}

	@Override
	public TaskRequest getElementAt(int index) {
		if (index < arrayList.length) {
			return arrayList[right ? arrayList.length - index -1 : index];
		}
		return null;
	}
	
	public void updateList() {
		getArrayList();
		fireContentsChanged(this, getSize(), getSize());
	}
}

class PathStatus extends JPanel {
	private static final long serialVersionUID = 1L;
	Image icon;
	PathStatusModel requestsModel;
	JList<TaskRequest> listView;
	boolean right;
	String iconResource;

	public PathStatus(String ic, PriorityBlockingQueue<TaskRequest> l, boolean r) {
		super();
		requestsModel = new PathStatusModel(l, r);
		iconResource = ic;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBackground(Color.WHITE);
		Image image = new ImageIcon(getClass().getResource(ic)).getImage();
		icon = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
		PathStatusItem renderer = new PathStatusItem(icon);

		setPreferredSize(new Dimension(getWidth(), 75));
		setMinimumSize(new Dimension(0, 75));
		setMaximumSize(new Dimension(2000, 75));

		listView = new JList<TaskRequest>();
		listView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listView.setAlignmentX(r ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
		listView.setCellRenderer(renderer);
		listView.setModel(requestsModel);
		listView.setVisibleRowCount(1);
		right = r;
		
		if (r) {
			add(Box.createHorizontalGlue());
			add(listView);
			add(Box.createHorizontalStrut(10));
		} else {
			add(Box.createHorizontalStrut(10));
			add(listView);
			add(Box.createHorizontalGlue());
		}
	}

	public void updateList() {
		requestsModel.updateList();
	}
}

class TransferStatus extends JPanel implements TaskRequestEventListener {
	private static final long serialVersionUID = 1L;
	PathStatus topPath;
	PathStatus bottomPath;
	public TransferStatus(String ts, String bs, String ticon, String bicon, PriorityBlockingQueue<TaskRequest> ti, PriorityBlockingQueue<TaskRequest> bi) { 
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		setBackground(Color.WHITE);
		JPanel sep = new JPanel();
		sep.setBackground(Color.BLACK);
		sep.setMinimumSize(new Dimension(getWidth(), 5));
		sep.setPreferredSize(new Dimension(getWidth(), 5));
		sep.setMaximumSize(new Dimension(2000, 5));

		topPath = new PathStatus(ticon, ti, false);
		bottomPath = new PathStatus(bicon, bi, true);

		JLabel topLabel = new JLabel(ts);
		topLabel.setOpaque(false);
		JLabel bottomLabel = new JLabel(bs);
		bottomLabel.setOpaque(false);
		
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
		
		JPanel topLegend = new JPanel();
		JPanel bottomLegend = new JPanel();
		topLegend.setBackground(Color.WHITE);
		bottomLegend.setBackground(Color.WHITE);
		topLegend.setLayout(new BoxLayout(topLegend,BoxLayout.PAGE_AXIS));
		topLegend.add(topLabel);
		topLegend.add(new JLabel(new ImageIcon(getClass().getResource("/icons/arrow-left.png"))));
		topLegend.setAlignmentX(CENTER_ALIGNMENT);

		bottomLegend.setLayout(new BoxLayout(bottomLegend,BoxLayout.PAGE_AXIS));
		bottomLegend.add(new JLabel(new ImageIcon(getClass().getResource("/icons/arrow-right.png"))));
		bottomLegend.add(bottomLabel);
		bottomLegend.setAlignmentX(CENTER_ALIGNMENT);
		
		topPanel.add(Box.createVerticalGlue());
		topPanel.add(topLegend);
		topPanel.add(topPath);
		topPanel.add(Box.createVerticalGlue());

		bottomPanel.add(Box.createVerticalGlue());
		bottomPanel.add(bottomPath);
		bottomPanel.add(bottomLegend);
		bottomPanel.add(Box.createVerticalGlue());


		add(topPanel);
		add(sep);
		add(bottomPanel);
	}

	@Override
	public void onTaskRequestEvent(TaskExecutor source, TaskRequestEventType type, TaskRequest t) {
		// TODO Auto-generated method stub
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
		setBackground(Color.WHITE);
		providerStatus = new ProviderStatus(p);
		storageStatus = new StorageStatus(inventory);
		storeStatus = new StoreStatus(inventory.store);

		leftStatus = new TransferStatus(
				"Pedido",
				"Orden de Entrada",
				"/icons/request-white.png",
				"/icons/inboundorder-white.png",
				p.getQueue(),
				inventory.requestsProcessor.getQueue()
				);

		rightStatus = new TransferStatus(
				"Transferencia",
				"Orden de Salida",
				"/icons/request-white.png",
				"/icons/outboundorder.png",
				inventory.store.getQueue(),
				inventory.outboundsProcessor.getQueue()
				);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(providerStatus);
		add(Box.createHorizontalStrut(16));
		add(leftStatus);
		add(Box.createHorizontalStrut(16));
		add(storageStatus);
		add(Box.createHorizontalStrut(16));
		add(rightStatus);
		add(Box.createHorizontalStrut(16));
		add(storeStatus);
		inventory.requestsProcessor.addTaskRequestEventListener(leftStatus);
		p.addTaskRequestEventListener(leftStatus);
//		inventory.addInventoryEventListener(rightStatus);
	}
}

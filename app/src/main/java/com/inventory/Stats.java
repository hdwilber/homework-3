package com.inventory;

import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.inventory.taskrequest.TaskExecutor;
import com.inventory.taskrequest.TaskRequest;
import com.inventory.taskrequest.TaskRequestEventListener;

class StatsEntry extends JPanel {
	private static final long serialVersionUID = 1L;
	JLabel label;
	JLabel count;
	
	public StatsEntry(String l, ImageIcon i) {
		super();
		label = new JLabel(l);
		label.setIcon(i);
		count = new JLabel("0");
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(label);
		add(Box.createHorizontalStrut(16));
		add(count);
		
		this.setAlignmentX(LEFT_ALIGNMENT);
	}
	public void setValue(int value) {
		count.setText(""+value);
	}
}

public class Stats extends JPanel implements TaskRequestEventListener {
	private static final long serialVersionUID = 1L;
	Inventory inventory;
	StatsEntry requests, inbounds, transfers, outbounds;

	public Stats(Inventory i) {
		inventory = i;
		setBorder(new EmptyBorder(16, 16, 16, 16));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Image i1 = new ImageIcon(getClass().getResource("/icons/request.png")).getImage();
		requests = new StatsEntry("Total Pedidos:", new ImageIcon(i1.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH)));

		Image i2 = new ImageIcon(getClass().getResource("/icons/inboundorder.png")).getImage();
		inbounds = new StatsEntry("Total Ingresos:", new ImageIcon(i2.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH)));

		transfers = new StatsEntry("Total Transferencias:", new ImageIcon(i1.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH)));

		Image i3 = new ImageIcon(getClass().getResource("/icons/outboundorder.png")).getImage();
		outbounds =new StatsEntry("Total Salidas:", new ImageIcon(i3.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH)));
		
		inventory.requestsProcessor.addTaskRequestEventListener(this);
		inventory.outboundsProcessor.addTaskRequestEventListener(this);

		add(requests);
		add(inbounds);
		add(transfers);
		add(outbounds);
	}

	@Override
	public void onTaskRequestEvent(TaskExecutor source, TaskRequestEventType type, TaskRequest t) {
		requests.setValue(inventory.totalRequests);
		inbounds.setValue(inventory.totalInbounds);
		transfers.setValue(inventory.totalTransfers);
		outbounds.setValue(inventory.totalOutbounds);
	}
}

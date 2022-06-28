package com.inventory;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.inventory.taskrequest.TaskExecutor;
import com.inventory.taskrequest.TaskRequest;

public class StorageStatus extends ItemStatus {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	JLabel inActiveTasksLabel;
	JLabel inPausedTasksLabel;
	JLabel queueTasksLabel;
	Inventory inventory;
	
	public StorageStatus(Inventory i) {
		super(i.requestsProcessor, "/icons/storage.png", "ALMACEN");
		inventory = i;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel outboundStats = new JPanel();
		outboundStats.setLayout(new BoxLayout(outboundStats, BoxLayout.Y_AXIS));
		inActiveTasksLabel = new JLabel("En Proceso: " + i.outboundsProcessor.countActiveTasks());
		inPausedTasksLabel = new JLabel("En Pausa: " + i.outboundsProcessor.countPausedTasks());
		queueTasksLabel = new JLabel("En Cola: " + i.outboundsProcessor.countQueue());
		outboundStats.add(inActiveTasksLabel);
		outboundStats.add(inPausedTasksLabel);
		outboundStats.add(queueTasksLabel);
		i.outboundsProcessor.addTaskRequestEventListener(this);
		
		Component self = this.getComponent(0);
		Component inboundStats = this.getComponent(1);
		removeAll();
		invalidate();

		outboundStats.setBackground(Color.WHITE);
		add(inboundStats);
		add(Box.createHorizontalStrut(16));
		add(self);
		add(Box.createHorizontalStrut(16));
		add(outboundStats);
	}

	@Override
	public void onTaskRequestEvent(TaskExecutor source, TaskRequestEventType type, TaskRequest t) {
		if (source == inventory.outboundsProcessor) { 
			inActiveTasksLabel.setText("En Proceso: " + source.countActiveTasks());
			inPausedTasksLabel.setText("En Pausa: " + source.countPausedTasks());
			queueTasksLabel.setText("En Cola: " + source.countQueue());
		} else {
			super.onTaskRequestEvent(source, type, t);
		}
	}
}

package com.inventory;

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
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

		inActiveTasksLabel = new JLabel("En Proceso: " + i.requestsProcessor.countActiveTasks());
		inPausedTasksLabel = new JLabel("En Pausa: " + i.requestsProcessor.countPausedTasks());
		queueTasksLabel = new JLabel("En Cola: " + i.requestsProcessor.countQueue());

		stats.add(inActiveTasksLabel);
		stats.add(inPausedTasksLabel);
		stats.add(queueTasksLabel);
		
		i.outboundsProcessor.addTaskRequestEventListener(this);
		
		add(stats, 0);
		invalidate();
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

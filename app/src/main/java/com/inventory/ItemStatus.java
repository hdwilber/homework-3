package com.inventory;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.inventory.taskrequest.TaskExecutor;
import com.inventory.taskrequest.TaskRequest;
import com.inventory.taskrequest.TaskRequestEventListener;
import com.inventory.taskrequest.TaskRequestProcessor;

public class ItemStatus extends JPanel implements TaskRequestEventListener {
	private static final long serialVersionUID = 1L;
	JLabel processingLabel;
	JLabel pausedLabel;
	JLabel queueLabel;
	ImageIcon icon;
	TaskRequestProcessor processor;

	public ItemStatus(TaskRequestProcessor p, String i, String l) {
		processor = p;
		icon = new ImageIcon(
				new ImageIcon(getClass().getResource(i))
				.getImage()
				.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH)
		);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel label = new JLabel(l);
		setBorder(new EmptyBorder(4, 4, 4, 4));
		label.setIcon(icon);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		label.setOpaque(false);
		
		setBackground(Color.WHITE);
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

		processingLabel = new JLabel("En Proceso: " + processor.countActiveTasks());
		pausedLabel = new JLabel("En Pausa: " + processor.countPausedTasks());
		queueLabel = new JLabel("En Cola: " + processor.countQueue());

		stats.add(processingLabel);
		stats.add(pausedLabel);
		stats.add(queueLabel);
		stats.setBackground(Color.WHITE);

		add(label);
		add(stats);

		p.addTaskRequestEventListener(this);
	}

	@Override
	public void onTaskRequestEvent(TaskExecutor source, TaskRequestEventType type, TaskRequest t) {
		processingLabel.setText("En Proceso: " + source.countActiveTasks());
		pausedLabel.setText("En Pausa: " + source.countPausedTasks());
		queueLabel.setText("En Cola: " + source.countQueue());
	}
}
package com.inventory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

interface EditDialogListener<U> {
	public void onEditDialogComplete(EditDialog dialog, U original, U data);
	public void onEditDialogCancel(EditDialog dialog, U original);
}

public abstract class EditDialog<T, U> extends JDialog implements ActionListener {
	final static String ACTION_OK = "OK";
	final static String ACTION_CANCEL = "CANCEL";
	final static EmptyBorder DEFAULT_PADDING = new EmptyBorder(8, 8, 8, 8);

	EditDialogListener<U> listener;
	T original;
	JPanel form;
	JLabel label = new JLabel("");

	public EditDialog(JFrame owner, EditDialogListener<U> l) {
		super(owner);
		listener = l;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		Action closeAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				listener.onEditDialogCancel(EditDialog.this, (U)original);
			}
		};
		KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "closex");
		getRootPane().getActionMap().put("closex", closeAction);
	}
	
	public abstract String getDialogTitle();

	public void setData(T p) {
		setTitle(getDialogTitle());
		label.setText(getDialogTitle());
		original = p;
	}
	
	public void setupContent() {
		Container pane = getContentPane();
		pane.removeAll();
		JPanel main = new JPanel();
		main.setBorder(new CompoundBorder(DEFAULT_PADDING, new TitledBorder(LineBorder.createGrayLineBorder(), getDialogTitle())));
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getForm());
        main.add(new JSeparator());
        main.add(getActionButtons());
        getContentPane().add(main);
        pack();
	}
	
	public abstract JPanel getForm();
	
	public JPanel getActionButtons() {
        JPanel actionButtons = new JPanel();
        JButton cancelButton = new JButton("Cancelar");
        JButton okButton = new JButton("Ok");
        okButton.setActionCommand(ACTION_OK);
        cancelButton.setActionCommand(ACTION_CANCEL);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        actionButtons.setLayout(new BoxLayout(actionButtons, BoxLayout.X_AXIS));
        actionButtons.add(Box.createHorizontalGlue());
        actionButtons.add(cancelButton);
        actionButtons.add(Box.createHorizontalStrut(16));
        actionButtons.add(okButton);
        actionButtons.setBorder(DEFAULT_PADDING);
        return actionButtons;
	}

	public abstract T getData();

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case ACTION_OK:
				listener.onEditDialogComplete(this, (U)original, (U)getData());
			case ACTION_CANCEL:
				listener.onEditDialogCancel(this, (U)original);
			default:
		}
	}
}
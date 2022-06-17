package com.inventory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

interface EditDialogListener<U> {
	public void onEditDialogComplete(EditDialog dialog, U original, U data);
	public void onEditDialogCancel(EditDialog dialog, U original);
}

public abstract class EditDialog<T, U> extends JDialog implements ActionListener {
	final static String ACTION_OK = "OK";
	final static String ACTION_CANCEL = "CANCEL";

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
	
	public void setData(T p) {
		setTitle(p != null ? "Edit" : "Add");
		label.setText(p != null ? "Edit:" : "Add:");
		original = p;
	}
	
	public void setupContent() {
		getContentPane().removeAll();
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(label);
        getContentPane().add(getForm());
        getContentPane().add(getActionButtons());
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
        actionButtons.add(cancelButton);
        actionButtons.add(okButton);
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
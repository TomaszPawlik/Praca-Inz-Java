package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class InterfaceDialogNew extends JDialog {

	public JTextField width = new JTextField("");
	public JTextField height = new JTextField("");
	private JButton okButton;
	private JButton cancelButton;
	public boolean okPressed = false;
		
	InterfaceDialogNew(Interface parent){
		super(parent, "New", true);
	    Container contentPane = getContentPane();
	    JPanel p1 = new JPanel(new GridLayout(2, 2,3,3));
	    p1.add(new JLabel("Width:"));
	    p1.add(width);
	    p1.add(new JLabel("Height:"));
	    p1.add(height );
	    contentPane.add("Center", p1);

	    Panel p2 = new Panel();
	    okButton = new JButton("OK");
	    okButton.addActionListener((new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		okButtonactionPerformed(e);
        	};
        }));
	    p2.add(okButton);
	    cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener((new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		canelButtonactionPerformed(e);
        	};
        }));
	    p2.add(cancelButton);
	    
	    contentPane.add(p2, BorderLayout.SOUTH);
	    setSize(240, 120);
		
	}
	
	public void okButtonactionPerformed(ActionEvent e){
		okPressed = true;
		setVisible(false);
	}
	
	public void canelButtonactionPerformed(ActionEvent e){
		setVisible(false);
	}
	

}

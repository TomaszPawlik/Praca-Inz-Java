package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ModelEwakuacji.pedestrianStrategy;

public class InterfaceDialogStrategy extends JDialog implements ItemListener {
	private JButton okButton, cancelButton;
	private JRadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6, radioButton7, radioButton8;

	
	public boolean okPressed = false;
	pedestrianStrategy newStrategy = pedestrianStrategy.ANT;
		
	InterfaceDialogStrategy(Interface parent){
		super(parent, "Select new strategy", true);
	    Container contentPane = getContentPane();
	    
	    JPanel p1 = new JPanel(new GridLayout(2, 4,3,3));
	    
	    radioButton1 = new JRadioButton("Sprinter");
	    radioButton2 = new JRadioButton("Balance");
	    radioButton3 = new JRadioButton("Businessman");
	    radioButton4 = new JRadioButton("Ant");
	    radioButton4.setSelected(true);
	    radioButton5 = new JRadioButton("Sampling");
	    radioButton6 = new JRadioButton("Look & go");
	    radioButton7 = new JRadioButton("One choice");
	    radioButton8 = new JRadioButton("Random");
	    
	    ButtonGroup group = new ButtonGroup();
	    group.add(radioButton1);
	    group.add(radioButton2);
	    group.add(radioButton3);
	    group.add(radioButton4);
	    group.add(radioButton5);
	    group.add(radioButton6);
	    group.add(radioButton7);
	    group.add(radioButton8);
	    
	    radioButton1.addItemListener(this);
	    radioButton2.addItemListener(this);
	    radioButton3.addItemListener(this);
	    radioButton4.addItemListener(this);
	    radioButton5.addItemListener(this);
	    radioButton6.addItemListener(this);
	    radioButton7.addItemListener(this);
	    radioButton8.addItemListener(this);
	    
	    p1.add(radioButton1);
	    p1.add(radioButton2);
	    p1.add(radioButton3);
	    p1.add(radioButton4);
	    p1.add(radioButton5);
	    p1.add(radioButton6);
	    p1.add(radioButton7);
	    p1.add(radioButton8);
	    
	    contentPane.add(p1, BorderLayout.NORTH);
	
	    Panel p3 = new Panel();
	    okButton = new JButton("OK");
	    okButton.addActionListener((new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		okButtonactionPerformed(e);
        	};
        }));
	    p3.add(okButton);
	    cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener((new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		canelButtonactionPerformed(e);
        	};
        }));
	    p3.add(cancelButton);
	    
	    contentPane.add(p3, BorderLayout.SOUTH);
	    setSize(390, 120);
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		JRadioButton source = (JRadioButton)(e.getSource());
		
		if (source==radioButton1){
			newStrategy=pedestrianStrategy.SPRINTER;
		} else
		if (source==radioButton2){
			newStrategy=pedestrianStrategy.BALANCE;
		} else
		if (source==radioButton3){
			newStrategy=pedestrianStrategy.BUSINESSMAN;
		} else
		if (source==radioButton4){
			newStrategy=pedestrianStrategy.ANT;
		} else
		if (source==radioButton5){
			newStrategy=pedestrianStrategy.SAMPLING;
		} else
		if (source==radioButton6){
			newStrategy=pedestrianStrategy.LOOKANDGO;
		} else
		if (source==radioButton7){
			newStrategy=pedestrianStrategy.ONECHOICE;
		} else
		if (source==radioButton8){
			newStrategy=pedestrianStrategy.RANDOM;
		}
	}
	
	public void okButtonactionPerformed(ActionEvent e){
		okPressed = true;
		setVisible(false);
	}
	
	public void canelButtonactionPerformed(ActionEvent e){
		setVisible(false);
	}
	
}

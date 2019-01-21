package GUI;

import java.awt.Insets;

import javax.swing.JButton;

public class InterfaceGridButton extends JButton {
	int width;
	int height;
	
	InterfaceGridButton (int a, int b){
		width = a;
		height = b;
		setMargin(new Insets(1,1,1,1));
		
	}
}

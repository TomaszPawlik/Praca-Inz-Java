
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

import ModelEwakuacji.CellGrid;
import ModelEwakuacji.cellState;
import ModelEwakuacji.pedestrianStrategy;

public class Interface extends JFrame {
	public JTextArea output;
	JTextField stageField;
    JMenuItem menuItemNew, menuItemSave, menuItemLoad, menuItemLoad2, menuItemExit, menuItemRandomP, 
    		  menuItemSolveXTimes, menuRadioButtonWas, menuRadioButtonWarras, menuItemClearMessageBoard,
    		  menuItemIncreaseButtonSize, menuItemDecreaseButtonSize, menuRadioButtonIndeks, menuRadioButtonOrder,
    		  menuItemIncreaseMoore, menuItemDecreaseMoore, menuItemAssignStrategy;
    JButton buttonClear, buttonPedestrian, buttonWall, buttonExit, buttonInfo, buttonStrategy, buttonPrevious ,buttonNext, buttonSolve;
    public JPanel panelTopRight, panelBottomLeft;
    public JProgressBar pBar;
    public JLabel n1, n2;
    
    CellGrid cellGrid;
    
    int selectedTool =0;
    int selectedMethod = 0; // 0 - Was 1 - Warras
    int buttonSize = 26;
    
    String programPath;
    String inDataFilePath = "in_data.txt";
    String inPeopleFilePath = "in_people.txt";
    String outMovesFilePath = "out_moves.txt";
    String outInfoFilePath = "out_info.txt";
    
    
    public Interface(){
    	super("Praca inżynierska Tomasz Pawlik v 0.25");
    }
 
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
 
        menuBar = new JMenuBar();
 
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);
 
        menuItemNew = new JMenuItem("New");
        menuItemNew.setMnemonic(KeyEvent.VK_N);
        menuItemNew.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		menuItemNewactionPerformed(e);
        	};
        });
        menu.add(menuItemNew);
        
        menu.addSeparator();
 
        menuItemSave = new JMenuItem("Save Input Files");
        menuItemSave.setMnemonic(KeyEvent.VK_S);
        menuItemSave.setEnabled(false);
        menuItemSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemSaveactionPerformed(e);
			};
		});
        menu.add(menuItemSave);
 
        menuItemLoad = new JMenuItem("Load Input Files");
        menuItemLoad.setMnemonic(KeyEvent.VK_L);
        menuItemLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					menuItemLoadactionPerformed(e);
				}catch (Exception error){}
			};
		});
        menu.add(menuItemLoad);
        
        menuItemLoad2 = new JMenuItem("Load Output Files");
        menuItemLoad2.setMnemonic(KeyEvent.VK_L);
        menuItemLoad2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					menuItemLoad2actionPerformed(e);
				}catch (Exception error){}
			};
		});
        menu.add(menuItemLoad2);
 
        menu.addSeparator();
        
        menuItemExit = new JMenuItem("Exit");
        menuItemExit.setMnemonic(KeyEvent.VK_E);
        menuItemExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemExitactionPerformed(e);
			};
		});
        menu.add(menuItemExit);
 
        
        
        menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        
        ButtonGroup group = new ButtonGroup();

        menuRadioButtonWas = new JRadioButtonMenuItem("Wąs");
        menuRadioButtonWas.setSelected(true);
        menuRadioButtonWas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemRadioButtonPerformed(e);
			};
		});

        menuRadioButtonWarras = new JRadioButtonMenuItem("Warras");
        menuRadioButtonWarras.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemRadioButtonPerformed(e);
			};
		});

        group.add(menuRadioButtonWas);
        group.add(menuRadioButtonWarras);
        menu.add(menuRadioButtonWas);
        menu.add(menuRadioButtonWarras);

        menu.addSeparator();
        
        menuItemRandomP = new JMenuItem("Random pedestrians");
        menuItemRandomP.setEnabled(false);
        menuItemRandomP.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemRandomPactionPerformed(e);
			};
		});
        menu.add(menuItemRandomP);
        
        menuItemAssignStrategy = new JMenuItem("Assign strategy for all pedestrians");
        menuItemAssignStrategy.setEnabled(false);
        menuItemAssignStrategy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemAssignStrategyactionPerformed(e);
			};
		});
        menu.add(menuItemAssignStrategy);
        
        menu.addSeparator();
        
        menuItemSolveXTimes = new JMenuItem("Simulate evacuation X times");
        menuItemSolveXTimes.setEnabled(false);
        menuItemSolveXTimes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					menuItemSolveXTimesPerformed(e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			};
		});
        menu.add(menuItemSolveXTimes);
        
        menu.addSeparator();
        
        menuItemIncreaseMoore = new JMenuItem("Increase Moore radius");
        menuItemIncreaseMoore.setEnabled(false);
        menuItemIncreaseMoore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemIncreaseMooreformed(e);
			};
		});
        menu.add(menuItemIncreaseMoore);
        
        menuItemDecreaseMoore = new JMenuItem("Decrease Moore radius");
        menuItemDecreaseMoore.setEnabled(false);
        menuItemDecreaseMoore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemDecreaseMoorePerformed(e);
			};
		});
        menu.add(menuItemDecreaseMoore);
        
        menuBar.add(menu);
        
        menu = new JMenu("Interface");
        menu.setMnemonic(KeyEvent.VK_I);
        
        group = new ButtonGroup();

        menuRadioButtonIndeks = new JRadioButtonMenuItem("Show index of pedestrians");
        menuRadioButtonIndeks.setSelected(true);
        menuRadioButtonIndeks.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemRadioButtonPerformed(e);
			};
		});

        menuRadioButtonOrder = new JRadioButtonMenuItem("Show movement order of pedestrians");
        menuRadioButtonOrder.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemRadioButtonPerformed(e);
			};
		});

        group.add(menuRadioButtonIndeks);
        group.add(menuRadioButtonOrder);
        menu.add(menuRadioButtonIndeks);
        menu.add(menuRadioButtonOrder);
        
        menu.addSeparator();
        
        menuItemClearMessageBoard = new JMenuItem("Clear message board");
        menuItemClearMessageBoard.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemClearMessageBoardPerformed(e);
			};
		});
        menu.add(menuItemClearMessageBoard);
        
        menu.addSeparator();
        
        menuItemIncreaseButtonSize = new JMenuItem("Increase button size.");
        menuItemIncreaseButtonSize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemIncreaseButtonSizePerformed(e);
			};
		});
        menu.add(menuItemIncreaseButtonSize);
        
        menuItemDecreaseButtonSize = new JMenuItem("Decrease button size.");
        menuItemDecreaseButtonSize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menuItemDecreaseButtonSizePerformed(e);
			};
		});
        menu.add(menuItemDecreaseButtonSize);
        
        menuBar.add(menu);
        
        //menu = new JMenu("About");
        //menu.setMnemonic(KeyEvent.VK_A);
        //menuBar.add(menu);
 
        return menuBar;
    }
    
    public Container createContentPane() throws UnsupportedEncodingException {
    	
        JPanel contentPane = new JPanel(new BorderLayout());

        JPanel panelTopLeft = new JPanel(new BorderLayout());
        
        JPanel panelTopLeftUp = new JPanel();
        panelTopLeftUp.setLayout(new BoxLayout(panelTopLeftUp, BoxLayout.Y_AXIS));
        
        panelBottomLeft = new JPanel(new BorderLayout());
   
        JPanel panelLeftButtons1 = new JPanel(new GridLayout(0,2,5,5));
        JPanel panelLeftButtons2 = new JPanel(new GridLayout(2,0,5,5));
        JPanel panelLeftButtons3 = new JPanel(new GridLayout(0,2,5,5));
        
        panelTopRight = new JPanel();       
        GroupLayout layout = new GroupLayout(panelTopRight);       
        panelTopRight.setLayout(layout); 
        
        JPanel panelBottom = new JPanel(new BorderLayout());
        
        panelTopLeft.setBackground(Color.YELLOW);
        panelTopRight.setBackground(new Color(153,242,242));
        
        buttonClear = new JButton("Clear");
        buttonClear.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonClearactionPerformed(e);
        	};
        });
        buttonClear.setEnabled(false);
        
		java.net.URL imageURL = this.getClass().getResource("/resources/Pedestrian.png");
		ImageIcon icon = new ImageIcon(imageURL);
        buttonPedestrian = new JButton(icon);
        buttonPedestrian.setToolTipText("Pedestrian");
        buttonPedestrian.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonPedestrianactionPerformed(e);
        	};
        });
        buttonPedestrian.setEnabled(false);
        
		java.net.URL imageURL2 = this.getClass().getResource("/resources/Wall.png");
		ImageIcon icon2 = new ImageIcon(imageURL2);
        buttonWall = new JButton(icon2);
        buttonWall.setToolTipText("Wall");
        buttonWall.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonWallactionPerformed(e);
        	};
        });
        buttonWall.setEnabled(false);
        
		java.net.URL imageURL3 = this.getClass().getResource("/resources/Exit.png");
		ImageIcon icon3 = new ImageIcon(imageURL3);
        buttonExit = new JButton(icon3);
        buttonExit.setToolTipText("Exit");
        buttonExit.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonExitactionPerformed(e);
        	};
        });
        buttonExit.setEnabled(false);
        
        buttonInfo = new JButton("Info");
        buttonInfo.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonInfoactionPerformed(e);
        	};
        });
        buttonInfo.setEnabled(false);
        
        buttonStrategy = new JButton("Strategy");
        buttonStrategy.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonStrategyactionPerformed(e);
        	};
        });
        buttonStrategy.setEnabled(false);
        
        buttonPrevious = new JButton("Previous");
        buttonPrevious.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonPreviousactionPerformed(e);
        	};
        });
        buttonPrevious.setEnabled(false);
        
        buttonNext = new JButton("Next");
        buttonNext.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonNextactionPerformed(e);
        	};
        });
        buttonNext.setEnabled(false);
        
        buttonSolve = new JButton("Solve");
        buttonSolve.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		buttonSolveactionPerformed(e);
        	};
        });
        buttonSolve.setEnabled(false); 
        
        stageField = new JTextField();
        stageField.setEditable(false);
        
        panelLeftButtons1.add(buttonClear);
        panelLeftButtons1.add(buttonPedestrian);
        panelLeftButtons1.add(buttonWall);
        panelLeftButtons1.add(buttonExit);
        panelLeftButtons1.add(buttonInfo);
        panelLeftButtons1.add(buttonStrategy);
        
        panelLeftButtons2.add(buttonSolve);
        panelLeftButtons2.add(stageField);
        
        panelLeftButtons3.add(buttonPrevious);
        panelLeftButtons3.add(buttonNext);
        
        panelTopLeftUp.add(panelLeftButtons1);
        panelTopLeftUp.add(Box.createRigidArea(new Dimension(5,5)));
        panelTopLeftUp.add(panelLeftButtons2);
        panelTopLeftUp.add(Box.createRigidArea(new Dimension(5,5)));
        panelTopLeftUp.add(panelLeftButtons3);
        
        panelTopLeft.add(panelTopLeftUp, BorderLayout.NORTH);
        
        pBar = new JProgressBar(0,100);
        n1 = new JLabel("Simulation: ");
        n2 = new JLabel("Prgoress: ");
        
        panelBottomLeft.add(n1, BorderLayout.NORTH);
        panelBottomLeft.add(n2, BorderLayout.CENTER);
        panelBottomLeft.add(pBar, BorderLayout.SOUTH);
        panelTopLeft.add(panelBottomLeft, BorderLayout.SOUTH);
        
        
        JScrollPane scroll = new JScrollPane(panelTopRight);
        setContentPane(scroll);      

        output = new JTextArea(6,20);
        DefaultCaret caret = (DefaultCaret)output.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);
        panelBottom.add(scrollPane);       
            
        contentPane.add(panelTopLeft, BorderLayout.WEST);
        contentPane.add(scroll, BorderLayout.CENTER);
        contentPane.add(panelBottom, BorderLayout.PAGE_END);
        
        
    	programPath = Interface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    	programPath = URLDecoder.decode(programPath, "UTF-8");
 
        return contentPane;
    }
    
    public void menuItemNewactionPerformed(ActionEvent e){
    	
    	InterfaceDialogNew dialogNew = new InterfaceDialogNew(this);
    	dialogNew.setVisible(true);
    	if (dialogNew.okPressed){
    	   	int width = Integer.parseInt(dialogNew.width.getText());
    	   	int height = Integer.parseInt(dialogNew.height.getText());
    		cellGrid = new CellGrid(width, height, this);
    		
    		repaintGrid(cellGrid);
    		output.append("New Grid\n");
    		
    		menuItemSave.setEnabled(true);
    		menuItemRandomP.setEnabled(true);
    		menuItemSolveXTimes.setEnabled(true);
    		buttonClear.setEnabled(true);
    		buttonPedestrian.setEnabled(true);
    		buttonWall.setEnabled(true);
    		buttonExit.setEnabled(true);
    		buttonInfo.setEnabled(true);
    		buttonStrategy.setEnabled(true);
    		buttonSolve.setEnabled(true);
    		menuItemAssignStrategy.setEnabled(true);
    		menuItemIncreaseMoore.setEnabled(true);
    		menuItemDecreaseMoore.setEnabled(true);
    		
    		stageField.setText("");
    		stageField.setEditable(false);
    		buttonPrevious.setEnabled(false);
    		buttonNext.setEnabled(false);
    		
    	    inDataFilePath = "in_data.txt";
    	    inPeopleFilePath = "in_people.txt";
    	    outMovesFilePath = "out_moves.txt";
    	    outInfoFilePath = "out_info.txt";
    		
            pBar.setValue(0);
            n1.setText("Simulation: ");
            n2.setText("Prgoress: ");
    	}
    }

    public void repaintGrid(CellGrid cell){
    	Font myFont = new Font("Arial", Font.BOLD, 12);
   		
    	panelTopRight.removeAll();
   		panelTopRight.repaint(); 
        GroupLayout layout = new GroupLayout(panelTopRight);       
        panelTopRight.setLayout(layout);      	   		
   		
        
        layout.setAutoCreateContainerGaps(true);
      
        GroupLayout.SequentialGroup leftToRight = layout.createSequentialGroup();
        GroupLayout.SequentialGroup topToBottom = layout.createSequentialGroup();
   		
        
        
        GroupLayout.ParallelGroup[] columnTable = new GroupLayout.ParallelGroup[cell.width];       
        for (int i=0;i<cell.width;i++){
        	columnTable[i] = layout.createParallelGroup();
        }
        
        
        for (int n=0; n<cell.height; n++){
        	GroupLayout.ParallelGroup row = layout.createParallelGroup();
        	for (int m=0; m<cell.width; m++){
            	InterfaceGridButton baton = new InterfaceGridButton(m ,n);
            	 	
            	if (menuRadioButtonIndeks.isSelected()==true){
	            	if (cell.cellGridTable[m][n].getState()==cellState.CLEAR){
	            		baton.setIcon(null);
	            	}	
	            	if (cell.cellGridTable[m][n].getState()==cellState.WALL){
	            		java.net.URL imageURL = this.getClass().getResource("/resources/Wall.png");
	            		ImageIcon icon = new ImageIcon(imageURL);
	            		baton.setIcon(icon);
	            	}	
	            	if (cell.cellGridTable[m][n].getState()==cellState.PEDESTRIAN && cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(m, n)).isBlocked()==false){
	            		baton.setForeground(new Color(24, 158, 24));
	            		baton.setFont(myFont);
	            		baton.setText(String.valueOf(cell.getPedestrianIndexFromList(m, n)));
	            	}
	            	if (cell.cellGridTable[m][n].getState()==cellState.PEDESTRIAN && cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(m, n)).isBlocked()==true){
	            		baton.setForeground(new Color(24, 158, 24));
	            		baton.setBackground(Color.LIGHT_GRAY);
	            		baton.setFont(myFont);
	            		baton.setText(String.valueOf(cell.getPedestrianIndexFromList(m, n)));
	            	}
	            	if (cell.cellGridTable[m][n].getState()==cellState.EXIT){
	            		java.net.URL imageURL = this.getClass().getResource("/resources/Exit.png");
	            		ImageIcon icon = new ImageIcon(imageURL);
	            		baton.setIcon(icon);
	            	}
	            	if (cell.cellGridTable[m][n].getState()==cellState.PEDESTRIANONEXIT){
	            		baton.setForeground(new Color(24, 158, 24));
	            		baton.setBackground(Color.RED);
	            		baton.setFont(myFont);
	            		baton.setText(String.valueOf(cell.getPedestrianIndexFromList(m, n)));
	            	}
            	} else
                if (menuRadioButtonOrder.isSelected()==true){
	            	if (cell.cellGridTable[m][n].getState()==cellState.CLEAR){
	            		baton.setIcon(null);
	            	}	
	            	if (cell.cellGridTable[m][n].getState()==cellState.WALL){
	            		java.net.URL imageURL = this.getClass().getResource("/resources/Wall.png");
	            		ImageIcon icon = new ImageIcon(imageURL);
	            		baton.setIcon(icon);
	            	}	
	            	if (cell.cellGridTable[m][n].getState()==cellState.PEDESTRIAN && cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(m, n)).isBlocked()==false){
	            		baton.setForeground(new Color(24, 158, 24));
	            		baton.setFont(myFont);
	            		baton.setText(String.valueOf(cell.pedestriansList.get(cell.getPedestrianIndexFromList(m, n)).getmovementOrder()));
	            	}
	            	if (cell.cellGridTable[m][n].getState()==cellState.PEDESTRIAN && cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(m, n)).isBlocked()==true){
	            		baton.setForeground(new Color(24, 158, 24));
	            		baton.setBackground(Color.LIGHT_GRAY);
	            		baton.setFont(myFont);
	            		baton.setText(String.valueOf(cell.pedestriansList.get(cell.getPedestrianIndexFromList(m, n)).getmovementOrder()));	            	}
	            	if (cell.cellGridTable[m][n].getState()==cellState.EXIT){
	            		java.net.URL imageURL = this.getClass().getResource("/resources/Exit.png");
	            		ImageIcon icon = new ImageIcon(imageURL);
	            		baton.setIcon(icon);
	            	}
	            	if (cell.cellGridTable[m][n].getState()==cellState.PEDESTRIANONEXIT){
	            		baton.setForeground(new Color(24, 158, 24));
	            		baton.setBackground(Color.RED);
	            		baton.setFont(myFont);
	            		baton.setText(String.valueOf(cell.pedestriansList.get(cell.getPedestrianIndexFromList(m, n)).getmovementOrder()));             	}
                }
            	baton.addActionListener(new ActionListener(){
                	public void actionPerformed(ActionEvent e) {
                		batonChange(e);
                	};
                });
        		
        		columnTable[m].addComponent(baton, buttonSize, buttonSize, buttonSize);
        		row.addComponent(baton, buttonSize, buttonSize, buttonSize);       	
        	}
        	topToBottom.addGroup(row);
        }
           	        
        for (int j=0;j<cell.width;j++){
        	leftToRight.addGroup(columnTable[j]);
        }

        layout.setHorizontalGroup(leftToRight);
        layout.setVerticalGroup(topToBottom);
    	
    }
      
    public void batonChange(ActionEvent e){
    	InterfaceGridButton source = (InterfaceGridButton)(e.getSource());
    	
    	switch (this.selectedTool){
    	case 1: cellGrid.changeCell(source.width, source.height, cellState.CLEAR);
    			repaintGrid(cellGrid);
    			break;
    	case 2: cellGrid.changeCell(source.width, source.height, cellState.PEDESTRIAN);
				repaintGrid(cellGrid);
				break;
    	case 3: cellGrid.changeCell(source.width, source.height, cellState.WALL);
				repaintGrid(cellGrid);
				break;
    	case 4: cellGrid.changeCell(source.width, source.height, cellState.EXIT);
				repaintGrid(cellGrid);
				break;
    	case 5: String s = cellGrid.cellGridTable[source.width][source.height].toString(cellGrid, selectedMethod);
    			output.append(s + "\n");
    			break;
	    case 6: if (cellGrid.cellGridTable[source.width][source.height].getState()==cellState.PEDESTRIAN)
	    			{
				    	InterfaceDialogStrategy dialogStrategy = new InterfaceDialogStrategy(this);
				    	dialogStrategy.setVisible(true);
				    	if (dialogStrategy.okPressed){
				    		cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(source.width, source.height)).setStrategy(dialogStrategy.newStrategy);
				    		output.append("Pedestrian "+ cellGrid.getPedestrianIndexFromList(source.width, source.height) +" changes strategy to: "+" '"+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(source.width, source.height)).strategy+"'\n"
				    					 +"Distance weight: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(source.width, source.height)).distanceWeight
				    					 +" Crowd density weight: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(source.width, source.height)).crowdDensityWeight
				    					 +" Changing crowd density weight: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(source.width, source.height)).changingCrowdDensityWeight
				    					 +"\n");
				    	}
				    }
				break;
    	}
    	
    }
    
    public void menuItemSaveactionPerformed(ActionEvent e){ 	
    	cellGrid.createInitialFiles();
        
    	String a = "Initial files saved.";
    	output.append(a + "\n");
    }
    
    public void menuItemLoadactionPerformed(ActionEvent e) throws FileNotFoundException{
        JFileChooser fileChooser = new JFileChooser(programPath);
        
        int returnValue1, returnValue2, int1, int2, numberOfExits, numberOfWalls, numberOfPedestrians;
        String line;
        String[] stringTab;
        

        
        returnValue1 = fileChooser.showDialog(null, "Open in_data file");
        if (returnValue1 == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = fileChooser.getSelectedFile();         
        	inDataFilePath = selectedFile.getAbsolutePath();
            
        	try{
        		BufferedReader br1 = new BufferedReader(new FileReader(inDataFilePath));
        		StringBuilder sb1 = new StringBuilder();

        		while ((line=br1.readLine()) != null)
        		{
        			sb1.append(line+"\n");
        		}
        		br1.close();
        		BufferedReader text=new BufferedReader( new StringReader(sb1.toString()));
        	  
        		line = text.readLine();
        		stringTab = line.split(" ");
        	  
        		int1 = Integer.parseInt(stringTab[0]);
        		int2 = Integer.parseInt(stringTab[1]);
      	   		cellGrid = new CellGrid(int1, int2, this);
      	   		
        		text.readLine();
        		line = text.readLine();
      	   		
        		numberOfExits = Integer.parseInt(line);

        		for (int a=0;a<numberOfExits;a++){
        			line = text.readLine();
        			stringTab = line.split(" ");
            		int1 = Integer.parseInt(stringTab[1]);
            		int2 = Integer.parseInt(stringTab[2]);
        			cellGrid.addExitToList(int1, int2);
        		}
      	   		
        		text.readLine();
        		line = text.readLine();
        		
        		numberOfWalls = Integer.parseInt(line);
        		
        		for (int a=0;a<numberOfWalls;a++){
        			line = text.readLine();
        			stringTab = line.split(" ");
            		int1 = Integer.parseInt(stringTab[1]);
            		int2 = Integer.parseInt(stringTab[2]);
        			cellGrid.cellGridTable[int1][int2].setState(cellState.WALL);
        		}
        	  
        	  
          }
          catch (Exception error) {}
          returnValue2 = fileChooser.showDialog(null, "Open in_people file");
          if (returnValue2 == JFileChooser.APPROVE_OPTION) {
          	File selectedFile2 = fileChooser.getSelectedFile();         
          	inPeopleFilePath = selectedFile2.getAbsolutePath();
            
        	try{
        		BufferedReader br1 = new BufferedReader(new FileReader(inPeopleFilePath));
        		StringBuilder sb1 = new StringBuilder();

        		while ((line=br1.readLine()) != null)
        		{
        			sb1.append(line+"\n");
        		}
        		br1.close();
        		BufferedReader text=new BufferedReader( new StringReader(sb1.toString()));
        	  
        		line = text.readLine();
      	   		
        		numberOfPedestrians = Integer.parseInt(line);

        		for (int a=0;a<numberOfPedestrians;a++){
        			line = text.readLine();
        			stringTab = line.split(" ");
            		int1 = Integer.parseInt(stringTab[0]);
            		int2 = Integer.parseInt(stringTab[1]);
        			cellGrid.addPedestrianToList(int1, int2);
        		}  	  
          }
          catch (Exception error) {}
          }
          repaintGrid(cellGrid);
          
          output.append("New Grid\n");
		
  		menuItemSave.setEnabled(true);
		menuItemRandomP.setEnabled(true);
		menuItemSolveXTimes.setEnabled(true);
		buttonClear.setEnabled(true);
		buttonPedestrian.setEnabled(true);
		buttonWall.setEnabled(true);
		buttonExit.setEnabled(true);
		buttonInfo.setEnabled(true);
		buttonStrategy.setEnabled(true);
		buttonSolve.setEnabled(true);
		menuItemAssignStrategy.setEnabled(true);
		menuItemIncreaseMoore.setEnabled(true);
		menuItemDecreaseMoore.setEnabled(true);
		
		stageField.setText("");
		stageField.setEditable(false);
		buttonPrevious.setEnabled(false);
		buttonNext.setEnabled(false);   
        }         
        
    	String s = "Initial files loaded";
    	output.append(s + "\n");   	
    }
    
    public void menuItemLoad2actionPerformed(ActionEvent e) throws FileNotFoundException{
        JFileChooser fileChooser = new JFileChooser(programPath);
        int returnValue1, returnValue2, returnValue3, int1, int2, numberOfExits, numberOfWalls, numberOfPedestrians;
        String line;
        String[] stringTab;
        
        returnValue1 = fileChooser.showDialog(null, "Open in_data file");
        if (returnValue1 == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = fileChooser.getSelectedFile();         
        	inDataFilePath = selectedFile.getAbsolutePath();
            
        	try{
        		BufferedReader br1 = new BufferedReader(new FileReader(inDataFilePath));
        		StringBuilder sb1 = new StringBuilder();

        		while ((line=br1.readLine()) != null)
        		{
        			sb1.append(line+"\n");
        		}
        		br1.close();
        		BufferedReader text=new BufferedReader( new StringReader(sb1.toString()));
        	  
        		line = text.readLine();
        		stringTab = line.split(" ");
        	  
        		int1 = Integer.parseInt(stringTab[0]);
        		int2 = Integer.parseInt(stringTab[1]);
      	   		cellGrid = new CellGrid(int1, int2, this);
      	   		
        		text.readLine();
        		line = text.readLine();
      	   		
        		numberOfExits = Integer.parseInt(line);

        		for (int a=0;a<numberOfExits;a++){
        			line = text.readLine();
        			stringTab = line.split(" ");
            		int1 = Integer.parseInt(stringTab[1]);
            		int2 = Integer.parseInt(stringTab[2]);
        			cellGrid.addExitToList(int1, int2);
        		}
      	   		
        		text.readLine();
        		line = text.readLine();
        		
        		numberOfWalls = Integer.parseInt(line);
        		
        		for (int a=0;a<numberOfWalls;a++){
        			line = text.readLine();
        			stringTab = line.split(" ");
            		int1 = Integer.parseInt(stringTab[1]);
            		int2 = Integer.parseInt(stringTab[2]);
        			cellGrid.cellGridTable[int1][int2].setState(cellState.WALL);
        		}
        	  
        	  
          }
          catch (Exception error) {}
          
          returnValue2 = fileChooser.showDialog(null, "Open out_moves file");
          if (returnValue2 == JFileChooser.APPROVE_OPTION) {
          	File selectedFile2 = fileChooser.getSelectedFile();         
          	outMovesFilePath = selectedFile2.getAbsolutePath();
          	
        	try{
        		BufferedReader br1 = new BufferedReader(new FileReader(outMovesFilePath));
        		StringBuilder sb1 = new StringBuilder();

        		while ((line=br1.readLine()) != null)
        		{
        			sb1.append(line+"\n");
        		}
        		br1.close();
        		BufferedReader text=new BufferedReader( new StringReader(sb1.toString()));
        	  
        		line = text.readLine();
        		line = text.readLine();
        		numberOfPedestrians = Integer.parseInt(line);
        		line = text.readLine();
        		cellGrid.stage = Integer.parseInt(line);
        		line = text.readLine();
        	  
        		for (int a=0;a<numberOfPedestrians;a++){
        			line = text.readLine();
        			stringTab = line.split(" ");
            		int1 = Integer.parseInt(stringTab[2]);
            		int2 = Integer.parseInt(stringTab[3]);
        			cellGrid.addPedestrianToList(int1, int2);
        		}    	   		
        	}
        	catch (Exception error) {}
          	
          	returnValue3 = fileChooser.showDialog(null, "Open out_info file");
            if (returnValue3 == JFileChooser.APPROVE_OPTION) {
              	File selectedFile3 = fileChooser.getSelectedFile();         
              	outInfoFilePath = selectedFile3.getAbsolutePath();
          	
    	    	menuItemSave.setEnabled(false);
        		menuItemRandomP.setEnabled(false);
    	    	buttonSolve.setEnabled(false);
    			buttonClear.setEnabled(false);
    			buttonPedestrian.setEnabled(false);
    			buttonWall.setEnabled(false);
    			buttonExit.setEnabled(false);
    			buttonStrategy.setEnabled(false);
    			buttonNext.setEnabled(true);
    			stageField.setEditable(false);
    			buttonInfo.setEnabled(true);
    			
    			stageField.setText("0");
    			stageField.setHorizontalAlignment(JTextField.CENTER);
    			
    			cellGrid.solved=true;
    			cellGrid.goToStage(Integer.parseInt(stageField.getText()), outMovesFilePath, inDataFilePath, outInfoFilePath);
    			
    			repaintGrid(cellGrid); 
            }
          }
        }
        
    	String s = "Output files loaded - preview of simulation available.";
    	output.append(s + "\n");   	
    }
    
    public void menuItemExitactionPerformed(ActionEvent e){
    	System.exit(0);
    }
    
    public void menuItemRadioButtonPerformed(ActionEvent e){
    	if (e.getSource()==menuRadioButtonWas){
    		selectedMethod=0;
    		output.append("Evacuation method: Wąs\n");
    	}
    	else if (e.getSource()==menuRadioButtonWarras){
    		selectedMethod=1;
    		output.append("Evacuation method: Warras\n");
    	}
    	else if (e.getSource()==menuRadioButtonIndeks){
    		repaintGrid(cellGrid);
    	}
    	else if (e.getSource()==menuRadioButtonOrder){
    		repaintGrid(cellGrid);
    	}
    }  
    
    public void menuItemRandomPactionPerformed(ActionEvent e){
    	int numberOfClearCells=0;
    	
    	InterfaceDialogRandomP dialogRandomP = new InterfaceDialogRandomP(this);
    	dialogRandomP.setVisible(true);
    	if (dialogRandomP.okPressed){
    		
    		for (int a=0;a<cellGrid.width;a++)
    			for (int b=0;b<cellGrid.height;b++){
    				if (cellGrid.cellGridTable[a][b].getState()==cellState.CLEAR)
    					numberOfClearCells++;
    			}
    		if (dialogRandomP.numberOfNewPedestrians>numberOfClearCells){
        		JOptionPane.showMessageDialog(this, "Too few clear cells.", "Error", JOptionPane.ERROR_MESSAGE);
    		}else
    		{
        		cellGrid.generateRandomPedestirans(dialogRandomP.numberOfNewPedestrians, dialogRandomP.randomStrategy, dialogRandomP.newStrategy);
        		repaintGrid(cellGrid);
        		output.append("Random pedestrians generated.\n"); 
    		}		
    	}
    }
    
    public void buttonClearactionPerformed(ActionEvent e){
    	this.selectedTool = 1;
    }
        
    public void buttonPedestrianactionPerformed(ActionEvent e){
    	this.selectedTool = 2;    	
    }
    
    public void buttonWallactionPerformed(ActionEvent e){
    	this.selectedTool = 3;   	
    }
    
    public void buttonExitactionPerformed(ActionEvent e){
    	this.selectedTool = 4;
    }
    
    public void buttonInfoactionPerformed(ActionEvent e){
    	this.selectedTool = 5;
    }
    
    public void buttonStrategyactionPerformed(ActionEvent e){
    	this.selectedTool = 6;
    }
    
    public void buttonPreviousactionPerformed(ActionEvent e){
    	
    	int x= Integer.parseInt(stageField.getText());
    	x--;
    	
    	if (x==0){
    		buttonNext.setEnabled(true);
    		buttonPrevious.setEnabled(false);
    		}
    	else if (x==cellGrid.stage-1){
    		buttonNext.setEnabled(false);
    		buttonPrevious.setEnabled(true);
    		}
    		
    	else{
    		buttonNext.setEnabled(true);
    		buttonPrevious.setEnabled(true);
    		}
    		
    	stageField.setText(Integer.toString(x));
    	
    	cellGrid.goToStage(x, outMovesFilePath, inDataFilePath, outInfoFilePath);
    	repaintGrid(cellGrid); 
    }
    
    public void buttonNextactionPerformed(ActionEvent e){
    	
    	int x= Integer.parseInt(stageField.getText());
    	x++;
    	
    	if (x==0){
    		buttonNext.setEnabled(true);
    		buttonPrevious.setEnabled(false);
    		}
    	else if (x==cellGrid.stage-1){
    		buttonNext.setEnabled(false);
    		buttonPrevious.setEnabled(true);
    		}
    		
    	else{
    		buttonNext.setEnabled(true);
    		buttonPrevious.setEnabled(true);
    		}
    		
    	stageField.setText(Integer.toString(x));
    	
    	cellGrid.goToStage(x, outMovesFilePath, inDataFilePath, outInfoFilePath);
    	repaintGrid(cellGrid);  	
    }
    
    
    
    
    
    
    
    public class sWorker extends SwingWorker<Integer, String> {
    	
    	public sWorker(){
    	}
    	
    	  @Override
    	  protected Integer doInBackground() throws Exception {
  			
			cellGrid.simulateEvacuationOnce();
			
			stageField.setText("0");
			stageField.setHorizontalAlignment(JTextField.CENTER);
			
			cellGrid.goToStage(Integer.parseInt(stageField.getText()), outMovesFilePath, inDataFilePath, outInfoFilePath);
			buttonNext.setEnabled(true);
			stageField.setEditable(false);
  			
  			return 1;
    	  }
    	  
    	  protected void done() {
              try {
            	  
      			repaintGrid(cellGrid);			
    	    	output.append("Evacuation solved in "+cellGrid.stage+" moves.\n");
            	  
              } catch (Exception ex) {
                  ex.printStackTrace();
              }
          }
   }
    
    public class mWorker extends SwingWorker<Integer, String> {
    	int a,b;
    	Boolean c;
    	pedestrianStrategy d;
    	
    	public mWorker(int numberOfSimulations, int numberOfNewPedestrians, boolean randomStrategy, pedestrianStrategy newStrategy){
    		a=numberOfSimulations;
    		b=numberOfNewPedestrians;
    		c=randomStrategy;
    		d=newStrategy;	
    	}
    	
    	  @Override
    	  protected Integer doInBackground() throws Exception {
  			
    		cellGrid.simulateEvacuationXTimes(a, b, c, d );
    		
			stageField.setText("0");
			stageField.setHorizontalAlignment(JTextField.CENTER);
			
			cellGrid.goToStage(Integer.parseInt(stageField.getText()), outMovesFilePath, inDataFilePath, outInfoFilePath);
  			buttonNext.setEnabled(true);
			stageField.setEditable(false);
  			
  			return 1;
    	  }
    	  
    	  protected void done() {
              try {
            	  
      			repaintGrid(cellGrid);
          		output.append("Simulation of "+a+" evacuations has ended.\n");
          		output.append("Statistic file has been generated.\n");
          		output.append("Preview of last simulation.\n");
            	  
              } catch (Exception ex) {
                  ex.printStackTrace();
              }
          }
   }
    
    
    

    public void buttonSolveactionPerformed(ActionEvent e){
    	this.selectedTool = 0;
    	if (cellGrid.getNumberOfExits()==0 || cellGrid.getNumberOfPedestrians()==0)
    		JOptionPane.showMessageDialog(this, "No pedestrians or no exits. Please check.", "Error", JOptionPane.ERROR_MESSAGE);
    	else{
    		
	    	menuItemSave.setEnabled(false);
    		menuItemRandomP.setEnabled(false);
	    	buttonSolve.setEnabled(false);
			buttonClear.setEnabled(false);
			buttonPedestrian.setEnabled(false);
			buttonWall.setEnabled(false);
			buttonExit.setEnabled(false);
			buttonStrategy.setEnabled(false);
			
	        sWorker worker = new sWorker();
           	worker.execute();
           	
    	}	
    }
    
    public void menuItemSolveXTimesPerformed(ActionEvent e) throws InterruptedException{
    	int numberOfClearCells=0;
    	
    	InterfaceDialogSolveXTimes dialog = new InterfaceDialogSolveXTimes(this);
    	dialog.setVisible(true);
    	if (dialog.okPressed){
    		
    		for (int a=0;a<cellGrid.width;a++)
    			for (int b=0;b<cellGrid.height;b++){
    				if (cellGrid.cellGridTable[a][b].getState()==cellState.CLEAR)
    					numberOfClearCells++;
    			}
    		if (dialog.numberOfNewPedestrians>numberOfClearCells){
        		JOptionPane.showMessageDialog(this, "Too few clear cells.", "Error", JOptionPane.ERROR_MESSAGE);        		
    		}else if (cellGrid.getNumberOfExits()==0)
    	    	JOptionPane.showMessageDialog(this, "No exits. Please check.", "Error", JOptionPane.ERROR_MESSAGE);
    	    else
    		{	
    	    	menuItemSave.setEnabled(false);
        		menuItemRandomP.setEnabled(false);
    	    	buttonSolve.setEnabled(false);
    			buttonClear.setEnabled(false);
    			buttonPedestrian.setEnabled(false);
    			buttonWall.setEnabled(false);
    			buttonExit.setEnabled(false);
    			buttonStrategy.setEnabled(false);
    			stageField.setEditable(false);	
    			
		        mWorker worker = new mWorker(dialog.numberOfSimulations, dialog.numberOfNewPedestrians, dialog.randomStrategy, dialog.newStrategy);
               	worker.execute();
    			
    			}
    		}		
    	}
    
    
    
    
    
    

    
    
    
    
    public void menuItemIncreaseMooreformed(ActionEvent e){
    	cellGrid.mooreRadius++;
		output.append("New Moore radius: "+cellGrid.mooreRadius+"\n");
    }
    
    public void menuItemDecreaseMoorePerformed(ActionEvent e){
    	if (cellGrid.mooreRadius==1){
    		output.append("Can not lower Moore radius lower than 1.\n");
    	} else{
    		cellGrid.mooreRadius--;
    		output.append("New Moore radius: "+cellGrid.mooreRadius+"\n");
    	}
    }
    
    public void menuItemAssignStrategyactionPerformed(ActionEvent e){
	    InterfaceDialogStrategy dialogStrategy = new InterfaceDialogStrategy(this);
	    dialogStrategy.setVisible(true);
	    if (dialogStrategy.okPressed){
	    	if (cellGrid.pedestriansList.size()==0){
	    		JOptionPane.showMessageDialog(this, "No pedestrians on grid.", "Error", JOptionPane.ERROR_MESSAGE);
	    	} else {
		    	for (int a=0;a<cellGrid.pedestriansList.size();a++)
		    		cellGrid.pedestriansList.get(a).setStrategy(dialogStrategy.newStrategy);
		    	
		    	output.append("Pedestrians strategy  changed to: "+" '"+cellGrid.pedestriansList.get(0).strategy+"'\n"
		    				 +"Distance weight: "+cellGrid.pedestriansList.get(0).distanceWeight
		   					 +" Crowd density weight: "+cellGrid.pedestriansList.get(0).crowdDensityWeight
		    				 +" Changing crowd density weight: "+cellGrid.pedestriansList.get(0).changingCrowdDensityWeight
		   					 +"\n");
	    	}
	   	}
	    
    }
    
    
    public void menuItemClearMessageBoardPerformed(ActionEvent e){
    	output.setText("");
    }
    
    public void menuItemIncreaseButtonSizePerformed(ActionEvent e){
    	buttonSize+=2;
		repaintGrid(cellGrid);
    }
    
    public void menuItemDecreaseButtonSizePerformed(ActionEvent e){
    	buttonSize-=2;
		repaintGrid(cellGrid);
		}
    
    
    
    private static void createAndShowGUI() {

    	Interface demo = new Interface();

        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.setJMenuBar(demo.createMenuBar());
        try{
        	demo.setContentPane(demo.createContentPane());
        } catch (Exception e){}
 
        demo.setSize(800, 600);
        demo.setLocation(300, 200);
        demo.setVisible(true);
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
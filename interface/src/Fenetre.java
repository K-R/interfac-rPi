import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Component;
import java.io.File;
import java.util.*;
import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JComboBox;

public class Fenetre extends JFrame{
	private snmp objSNMP;	
	private Bouton bouton1; // 8 buttons to check the status of each port
	private Bouton bouton2;
	private Bouton bouton3;
	private Bouton bouton4;
	private Bouton bouton5;
	private Bouton bouton6;
	private Bouton bouton7;
	private Bouton bouton8;
	private Bouton[] tabBouton = new Bouton[8];
	private JTable tableau;//visual table that store informations and buttons to interact with
	private String[][] tabIp;//stores IP addresses
	private Integer cmpt=0;//counter that indicates if a job is being executed
	private Panneau pan;//defines the background
	private String[] comboData;
	private JComboBox combo;
	private Os OSboot;
	private LinkedList<Job> JobList = new LinkedList<Job>();// list that stores the jobs to launch

	public Fenetre(String cheminTFTP, snmp Snmp){
		File f = new File(cheminTFTP+"/kernels");
		ArrayList<String> kernelList = new ArrayList<String>(Arrays.asList(f.list()));
		comboData = kernelList.toArray(new String[kernelList.size()]); //defines the list of kernel available
		OSboot = new Os(cheminTFTP, Snmp, false);
		
		//caracteristics of the frame
		this.setLocation(200,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Command center");
		this.setSize(880, 445);
		this.setResizable(false);

		objSNMP = Snmp;
		bouton1 = new Bouton("", objSNMP.getEtat(1));
		bouton2 = new Bouton("", objSNMP.getEtat(2));
		bouton3 = new Bouton("", objSNMP.getEtat(3));
		bouton4 = new Bouton("", objSNMP.getEtat(4));
		bouton5 = new Bouton("", objSNMP.getEtat(5));
		bouton6 = new Bouton("", objSNMP.getEtat(6));
		bouton7 = new Bouton("", objSNMP.getEtat(7));
		bouton8 = new Bouton("", objSNMP.getEtat(8));
		pan = new Panneau();

		tabBouton[0]=bouton1;//store buttons in a table
		tabBouton[1]=bouton2;
		tabBouton[2]=bouton3;
		tabBouton[3]=bouton4;
		tabBouton[4]=bouton5;
		tabBouton[5]=bouton6;
		tabBouton[6]=bouton7;
		tabBouton[7]=bouton8;
		
		//locate buttons on the corresponding ports
		bouton1.setBounds(88,344,73,56);
		bouton2.setBounds(177,344,73,56);
		bouton3.setBounds(266,344,73,56);
		bouton4.setBounds(355,344,73,56);
		bouton5.setBounds(450,344,73,56);
		bouton6.setBounds(539,344,73,56);
		bouton7.setBounds(628,344,73,56);
		bouton8.setBounds(716,344,73,56);
		pan.setBounds(0,270,880,175);

        //title of the columns of the table
		String  title[] = {"Port Number", "Kernel to load", "IP address", "(Re)Start the board", "Shutdown the board"};
		JComboBox combo = new JComboBox(comboData);
        tabIp=objSNMP.snmpGetAllAddress();
        //Data of the table
		Object[][] data = {  
				{"1", comboData[0], tabIp[0][0], "start", "stop"},
				{"2", comboData[0], tabIp[1][0], "start", "stop"},
				{"3", comboData[0], tabIp[2][0], "start", "stop"},
				{"4", comboData[0], tabIp[3][0], "start", "stop"},
				{"5", comboData[0], tabIp[4][0], "start", "stop"},
				{"6", comboData[0], tabIp[5][0], "start", "stop"},
				{"7", comboData[0], tabIp[6][0], "start", "stop"},
				{"8", comboData[0], tabIp[7][0], "start", "stop"},
		};

		//caracteristics of the table
		ZModel zModel = new ZModel(data, title);
		this.tableau = new JTable(zModel);
		this.tableau.setRowHeight(30);
		this.tableau.getTableHeader().setPreferredSize(new Dimension(0, 30));
		this.tableau.getColumn("(Re)Start the board").setCellEditor(new ButtonEditor(new JCheckBox()));
		this.tableau.getColumn("(Re)Start the board").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Shutdown the board").setCellEditor(new ButtonEditor(new JCheckBox()));
		this.tableau.getColumn("Shutdown the board").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Kernel to load").setCellEditor(new DefaultCellEditor(combo));
		this.tableau.getColumn("Kernel to load").setCellRenderer(new ComboRenderer());
		tableau.setRowSelectionAllowed(false);
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		custom.setHorizontalAlignment(JLabel.CENTER);
		tableau.getColumnModel().getColumn(0).setCellRenderer(custom);
		tableau.getColumnModel().getColumn(2).setCellRenderer(custom);		
		
		//make appear buttons, background and table
		for(int i=0; i<=7; i++){
			this.getContentPane().add(tabBouton[i]);
		}
		this.getContentPane().add(pan);		
		this.getContentPane().add(new JScrollPane(tableau), BorderLayout.CENTER);
		this.setVisible(true);
		
		UnThread thread = new UnThread();//thread that refreshes IP addresses every 5 seconds
		thread.start();
		UnThread2 thread2 = new UnThread2();//thread that checks if there is a job to launch every second
		thread2.start();
	}
	
	//Thread to refresh IP addresses
	private class UnThread extends Thread{
		public void run() {
			while(true){
				tabIp=objSNMP.snmpGetAllAddress();
				for(int i=0; i<=7; i++){
					tableau.setValueAt(tabIp[i][0], i, tableau.getColumnModel().getColumnIndex("IP address"));
				}
				tableau.repaint();
				try{
					UnThread.sleep(5000);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	//Thread to launch jobs
	private class UnThread2 extends Thread{
		public void run() {
			while(true){
				if(cmpt>0){ //check if a job is being executed
					cmpt--;
					if(cmpt==0){
						int i=JobList.removeFirst().getPort()-1;//removes the job from the list if it has just ended his execution
						tabBouton[i].start();
						tabBouton[i].repaint();
					}
				}
				else if (JobList.size()!=0){//if no job is being executed and a job is waiting
					cmpt=30;
					Job FirstJob=JobList.getFirst();
					int i= FirstJob.getPort();
					OSboot.lancementOs(FirstJob.getKernelName(),i);//execution of the first job of the list
					tabBouton[i-1].enCours();
					tabBouton[i-1].repaint();
				}
				try{
					UnThread2.sleep(1000);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	//Use of start and stop buttons
	public class ButtonEditor extends DefaultCellEditor {
		protected Fenetre fenetre;
		protected JButton button;
		private boolean isPushed;
		private ButtonListener bListener = new ButtonListener();

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.addActionListener(bListener);
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			bListener.setRow(row);
			bListener.setColumn(column);
			bListener.setTable(table);
			button.setBorderPainted(true);
			if (value=="start" || value=="stop"){
				button.setText((String)value);
			}
			return button;
		}

		class ButtonListener implements ActionListener{
			private Iterator<Job> it; 
			private int row;
			private JTable table;
			private JButton button;

			public void setColumn(int col){}			
			public void setRow(int row){this.row = row;}		
			public void setTable(JTable table){this.table = table;}
			public JButton getButton(){return this.button;}
			
			public void actionPerformed(ActionEvent event) {
				it = JobList.iterator();
				int numPort = this.row + 1;//get the port number
				String OS = (String) (table.getModel()).getValueAt(this.row, 1);//get the OS chosen
				if(((JButton)event.getSource()).getText()=="start"){
					if(JobList.size()!=0 && JobList.getFirst().getPort()!=numPort){
						tabBouton[numPort-1].enAttente();
					}
					JobList.add(new Job(numPort, OS));// when the start button is pushed it adds the appropriate job to the list
				}
				else{
						tabBouton[numPort-1].stop();
						OSboot.stopOs(numPort);//when the stop button is pushed it turns off the corresponding port
						if(JobList.size()!=0 && JobList.getFirst().getPort()==numPort){
							cmpt=0;	//if we turns off the port corresponding to the job that was executed, it resets it's allocated time
						}
						while(it.hasNext()){	//remove all the jobs with the same port number waiting in the list
							if(it.next().getPort()==numPort){
								it.remove();
							}
						}
				}
				repaint();
			}
		}
	}
}


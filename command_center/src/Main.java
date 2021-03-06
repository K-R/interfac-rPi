import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
public class Main{

	public static void showHelp(){
		System.out.println("");
		System.out.println("");
		System.out.println("HELP MENU : ");
		System.out.println("");
		System.out.println("Choix [ [IpAddress]   [Path]    [-g] | [-all] | [-p NumPort DesiredState kernelName]  |  [-h]");
		System.out.println("IpAddress                   		: Ip address of the switch");
		System.out.println("Path                    			: path to the TFTP directory");
		System.out.println("-g   (graphic)              		: show graphic interface");
		System.out.println("-all                                        : show Ip addresses and Mac addresses of all ports");
		System.out.println("-p	NumPort DesiredState kernelName         : load the specified kernel on the port NumPort (NumPort=0 -> common action), DesiredState=0 -> turn off or DesiredState=1 -> start/restart");
		System.out.println("-h   (help)                 		: show this help");
		System.out.println("");
		System.out.println("");
	}	

	public static void main(String[] args){

		int taille = args.length;
		String strIp = "";
		String cheminTFTP = "";
		int NumPort;
		int desiredState;

		snmp objSNMP = null;

		String tabInfo[][]; 
		Os OSboot = null;
		
		ArrayList<String> kernelList = null;

		if(taille<=2|| args[0].equalsIgnoreCase("-h") ){
			Main.showHelp();
			System.exit(0);              
		}
		
		if(args[0].length()<7 || args[0].length()>15){
			System.out.println("Enter a valid IP address"); 
			System.exit(0);    
		}
		else{ 
			strIp = args[0];
			
			objSNMP = new snmp(strIp);

			if(args[1].equalsIgnoreCase("-g")==false || args[1].equalsIgnoreCase("-p")==false){
				try{
					cheminTFTP = args[1];
					
					File f = new File(cheminTFTP+"/kernels");
					kernelList = new ArrayList<String>(Arrays.asList(f.list()));
					
					if (kernelList.isEmpty()){
						System.out.println("No kernel found in the PATH/kernels folder");
						System.exit(0);
					}
					
					System.out.println("Found kernels:");
					System.out.println(kernelList.toString());
					

				}
				catch(Exception e){
					System.out.println("Error path directory");
					System.exit(0);
				}
			}

			if(args[2].equalsIgnoreCase("-g")){
				Fenetre fen = new Fenetre(cheminTFTP, objSNMP);
			}
			else{
				
				if(args[2].equalsIgnoreCase("-all")){
					tabInfo = objSNMP.snmpGetAllAddress();
						System.out.println(" ");
					for (int i=0;i<tabInfo.length;i++){
						System.out.println("Port "+ (i+1) + " : " + tabInfo[i][0] + "  " + tabInfo[i][1]);
					} 
						System.out.println(" ");
				}
				else{

					if(args[2].equalsIgnoreCase("-p")){
						OSboot = new Os(cheminTFTP, objSNMP,true);
						
						for(int i=2;i<taille && i<=32;i=i+4){	//script a adapter

							if(args[i].equalsIgnoreCase("-p") && i!=taille-1 && i+1!=taille-1){


								try{
									NumPort=Integer.valueOf(args[i+1]);
									if(NumPort>8 || NumPort<0 ){
										System.out.println("Uncorrect port value");
										System.exit(0);
									}
									desiredState=Integer.valueOf(args[i+2]);
									switch (desiredState){

										case 0 :
											OSboot.stopOs(NumPort);
											break;

										case 1 :
											if(kernelList.contains(args[i+3])){
												OSboot.lancementOs(args[i+3],NumPort);
											}
											else{
												System.out.println("Error no kernel found");
												System.exit(0);
											}
											break;

										default :
											System.out.println("Uncorrect state value");
											System.exit(0);
											break;
										}
								}
								catch(Exception e){
									System.out.println("Enter number for port, desiredState and choose the OS -a or -d");
									System.exit(0);
								}
							}
							else{
								System.out.println("Enter port, desiredState and desired OS -a or -d"); 
								System.exit(0);
							}

						}
					}
					else{
						System.out.println("Syntax error");
						Main.showHelp();            
					}
				}
			}

		}
	}

}	

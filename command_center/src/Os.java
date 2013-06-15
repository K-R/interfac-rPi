import java.io.*;
import java.net.*;

public class Os{
	private  File from;
	private  File to;
	private  String cheminTFTP;
	private  snmp objSNMP;
	private  Boolean commandLine=false;

	public Os(String cheminTFTP, snmp obj,Boolean command){
		to = new File(cheminTFTP+"/kernel");
		this.cheminTFTP=cheminTFTP;
		objSNMP = obj;
		commandLine=command;
	}

	public void lancementOs(String kernelName, Integer numPort){

		InetAddress inet=null;
		Boolean ping=false;

		from = new File(cheminTFTP + "/kernels/"+kernelName);
		try{							
			Copie.copy(from,to);
		}catch(Exception e){
			System.out.println("Failed to load "+kernelName);
			System.exit(0);
		}

		if (numPort==0){
			for(int i=1;i<9;i++){
				objSNMP.snmpSet(i,2);
				objSNMP.snmpSet(i,2);
				objSNMP.snmpSet(i,1);
				objSNMP.snmpSet(i,1);
			}
		}
		else{	
			objSNMP.snmpSet(numPort,2);
			objSNMP.snmpSet(numPort,2);
			objSNMP.snmpSet(numPort,1);
			objSNMP.snmpSet(numPort,1);
		}
		if(commandLine==true){	
			try{	System.out.println("loading " + kernelName + " port " + numPort + " ...");
				Thread.sleep(15000);
				Ping.broadcastPing("192.168.0.", 255);
				Thread.sleep(3000);
				System.out.println("loading done ");
			}
			catch(InterruptedException e){
				System.out.println("Error sleep");
			}
		}
	}
	public void stopOs(Integer NumPort){

		if(NumPort==0){
			for(int i=1;i<9;i++){
				objSNMP.snmpSet(i, 2);
			}
		}
		else{
			objSNMP.snmpSet(NumPort, 2);
		}
		
		if(commandLine==true){
			System.out.println("stop port " + NumPort);
		}

	}

}

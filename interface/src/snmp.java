
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.lang.Integer;
import snmp.*; //snmp.jar file

public class snmp {

	public String READ_COMMUNITY;
	public String WRITE_COMMUNITY;
	public int iSNMPVersion;
	public String strOID_Table;
	public String strOID_Mac;
	public String strIPAddress;

	public snmp(String AddIp) {
		READ_COMMUNITY = "public";
		WRITE_COMMUNITY = "private";
		iSNMPVersion = 1; // 1 represents SNMP version=2
		strOID_Table = "1.3.6.1.4.1.4526.11.16.1.1.1.3.1.";
		strOID_Mac = "1.3.6.1.2.1.17.7.1.2.2.1.2.1";
		strIPAddress = AddIp;

	}

	public void snmpSet(int port, int Value) {
		try {
			if (port == 0) {

				InetAddress hostAddress = InetAddress.getByName(strIPAddress);
				SNMPv1CommunicationInterface comInterface = new SNMPv1CommunicationInterface(iSNMPVersion, hostAddress, WRITE_COMMUNITY);
				SNMPVarBindList newVars = new SNMPVarBindList();

				for (int i = 1; i < 9; i++) {
					//System.out.println("Port : "+ i);
					//System.out.println("Setting value : " + String.valueOf(Value));
					//System.out.println("to OID " + strOID + String.valueOf(i) +"\n");
					newVars = comInterface.setMIBEntry(strOID_Table + String.valueOf(i), new SNMPInteger(Value));
				}
			}

			if (port < 9 && port > 0) {
				InetAddress hostAddress = InetAddress.getByName(strIPAddress);
				SNMPv1CommunicationInterface comInterface = new SNMPv1CommunicationInterface(iSNMPVersion, hostAddress, WRITE_COMMUNITY);
				SNMPVarBindList newVars = new SNMPVarBindList();
				//System.out.println("Port : "+ port);
				//System.out.println("to OID " + strOID + String.valueOf(port));
				//System.out.println("Setting value : " + String.valueOf(Value) + "\n"); 
				newVars = comInterface.setMIBEntry(strOID_Table + String.valueOf(port), new SNMPInteger(Value));
				String valueString = new String();
			}



		} catch (Exception e) {
			System.out.println("Exception during SNMP operation: " + e);
			System.out.println("Can't get SNMP info from "+strIPAddress+"... exiting now");
			System.exit(0);
		}
	}
	/*
	 * This method process the request and Get the Value on the device
	 * @returns value of the state of the port
	 */

	public int snmpGetPort(int port) {
		String str = "";
		try {
			InetAddress hostAddress = InetAddress.getByName(strIPAddress);
			//System.out.println("hostAddress = " + hostAddress);
			//System.out.println("community = " + community);
			SNMPv1CommunicationInterface comInterface = new SNMPv1CommunicationInterface(iSNMPVersion, hostAddress, READ_COMMUNITY);
			//System.out.println("strOID = " + strOID_Table + String.valueOf(port));
			// returned Vector has just one pair inside it.

			SNMPVarBindList newVars = comInterface.getMIBEntry(strOID_Table + String.valueOf(port));
			// extract the (OID,value) pair from the SNMPVarBindList;
			//the pair is just a two-element
			// SNMPSequence

			SNMPSequence pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
			// extract OID from the pair; it's the first element in the sequence

			SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
			//extract the value from the pair;
			// it's the second element in the sequence

			SNMPObject snmpValue = pair.getSNMPObjectAt(1);
			str = snmpValue.toString();
		} catch (Exception e) {
			System.out.println("Exception during SNMP operation: " + e);
			System.out.println("Can't get SNMP info from "+strIPAddress+"... exiting now");
			System.exit(0);
		}
		//System.out.println("Retrieved value: " + str);
		return Integer.valueOf(str);
	}

	public String[] snmpGetMacAddress(){

		String str="";
		int taille=0;
		String macTabEntier[]=null;
		String macTab[]=null;
		try {
			InetAddress hostAddress = InetAddress.getByName(strIPAddress);
			//System.out.println("hostAddress = " + hostAddress);
			SNMPv1CommunicationInterface comInterface = new SNMPv1CommunicationInterface(iSNMPVersion, hostAddress, READ_COMMUNITY);
			//System.out.println("strOID = " + strOID_Table + String.valueOf(port));
			// returned Vector has just one pair inside it.

			SNMPVarBindList newVars = comInterface.retrieveMIBTable(strOID_Mac);
			// extract the (OID,value) pair from the SNMPVarBindList;
			//the pair is just a two-element
			// SNMPSequence

			taille=newVars.size();
			//System.out.println("taille : " + taille);

			SNMPSequence pair[]=new SNMPSequence[taille];
			macTabEntier = new String[8];
			macTab = new String[6];
                        
			for (int i=0;i<taille;i++){
				pair[i] = (SNMPSequence) (newVars.getSNMPObjectAt(i));
                                int j=Integer.valueOf(pair[i].getSNMPObjectAt(1).toString());
                                if (j<=8){
                                    macTabEntier[j-1]=pair[i].getSNMPObjectAt(0).toString().replace(strOID_Mac + ".","").replace(".",":");
                                    //System.out.println(macTabEntier[i][0]+ " : " + macTabEntier[i][1]);

                                    macTab = macTabEntier[j-1].split(":");

                                    macTabEntier[j-1]="";

                                    for (int k=0;k<macTab.length;k++){

                                            StringBuilder sb = new StringBuilder();
                                            sb.append(Integer.toHexString(Integer.valueOf(macTab[k])));
                                            if (sb.length() < 2) {
                                                    sb.insert(0, '0'); // pad with leading zero if needed
                                            }
                                            String hex = sb.toString();


                                            if(k!=macTab.length-1){
                                                    macTabEntier[j-1] = macTabEntier[j-1] + hex+":";
                                            }
                                            else{
                                                    macTabEntier[j-1] = macTabEntier[j-1] + hex;
                                            }


                                    }
                                    //System.out.println(macTabEntier[i][0] +" : " + macTabEntier[i][1]);   
			}
                        }

		} catch (Exception e) {
			System.out.println("Exception during SNMP operation: " + e);
			System.out.println("Can't get SNMP info from "+strIPAddress+"... exiting now");
			System.exit(0);
		}

		return macTabEntier;

	}

	public String[][] snmpGetAllAddress(){
		String ipTab[][];
		String macTab[]=snmpGetMacAddress();
		Boolean ping=false;
		int taille = macTab.length;

		ipTab=new String[taille][2];
		int compteur=0;
		int i=0;
		BufferedReader in ;
              


		Ping.broadcastPing("192.168.0.", taille);

		// arp -n | grep adrMac | awk '{print $1}' 
		for (int j=0;j<macTab.length;j++){
			ipTab[j][1]=macTab[j];
                        if(macTab[j]!=null){
                            try{

                                    ProcessBuilder pb = new ProcessBuilder("./getip.sh",macTab[j]);
                                    pb.redirectErrorStream(true);
                                    Process process = pb.start();
                                    InputStream processOutput = process.getInputStream();
                                    try {
                                            process.getOutputStream().close(); // fermeture du flux stdin inutilisé
                                            in = new BufferedReader( new InputStreamReader(process.getInputStream()));
                                            ipTab[j][0]=in.readLine();

                                    } finally { processOutput.close(); }
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }
                            //System.out.println (ipTab[j][0] + " : " + ipTab[j][1]+ "\n");          
                        }
                }

		//for (int j=0;j<ipTab.length;j++){
			//System.out.println (String.valueOf(i)+ " : " + ipTab[j][0] + " : " + ipTab[j][1] );  
		//}
        
		return ipTab;
	}

	public void snmpGetTable() {

		for (int i = 1; i < 9; i++) {
			//System.out.println("Port : " + i);
			snmpGetPort(i);
			//System.out.println();
		}
	}

	public boolean getEtat(int NumPort) {
		int ret = 0;

		ret = snmpGetPort(NumPort);

		if (ret == 1) {
			return true;
		} else {
			return false;
		}
	}

}

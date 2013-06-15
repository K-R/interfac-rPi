import java.net.InetAddress;


public class Ping {
	
	public static void broadcastPing(String net, int nb_max_reachable) {
		InetAddress inet = null;
		int compteur = 0;
		int i = 1;
		try{
			while(compteur!=nb_max_reachable && i<255) {

				inet = InetAddress.getByName(net+i);
				//System.out.println("Sending Ping Request to " + "192.168.0."+i);
				if (inet.isReachable(20)==true){
					//System.out.println("Host is reachable");
					compteur++;
				}
				else{
					//System.out.println("Host is NOT reachable");
				}
				//System.out.println(compteur);
				i++;
			}
		}
		catch(Exception e){
			System.out.println("Internal error, can't fill ARP Table with ping");
			System.exit(0);
		}
	}

}

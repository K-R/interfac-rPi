import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Main {
	public static void help() {
		System.out.println("Connexion [host] [options] [mb851 number] [file] ");
		System.out.println("	host :");
		System.out
				.println(" 		Specify the ip adress of the raspberry running Debian");
		System.out.println("	options :");
		System.out.println("		-f : flash the card");
		System.out.println("		-r : restart the card");
		System.out.println("		-t : recover traces");
		System.out
				.println("		for more options : use directly the flasher on the raspberry ");
		System.out.println("	mb851 number :");
		System.out
				.println("		the card is assiociated to a specific device like ttyACM0 on the raspberry");
		System.out.println("		Just specify 0,1 ...");
		System.out.println("	file :");
		System.out
				.println("		only used for flashing a .bin file on the mb851.  ");
	}

	public static void main(String[] arg) {
		if (arg.length == 0) {
			help();
		} else {
			try {

				JSch jsch = new JSch();

				// jsch.setKnownHosts("/home/foo/.ssh/known_hosts");

				String host = null;
				host = arg[0];
				host = host.substring(host.indexOf('@') + 1);
				Session session = jsch.getSession("pi", host, 22);
				session.setPassword("raspberry");

				UserInfo ui = new MyUserInfo() {
					public void showMessage(String message) {
						JOptionPane.showMessageDialog(null, message);
					}

					public boolean promptYesNo(String message) {
						Object[] options = { "yes", "no" };
						int foo = JOptionPane.showOptionDialog(null, message,
								"Warning", JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, options,
								options[0]);
						return foo == 0;
					}
				};

				session.setUserInfo(ui);
				session.connect(30000); // making a connection with timeout.

				// Channel channel = session.openChannel("shell");
				ChannelExec exec = (ChannelExec) session.openChannel("exec");

				exec.setInputStream(System.in);
				exec.setOutputStream(System.out);

				if (arg[1].equals("-f")) {
					if (arg.length == 4) {
						exec.setCommand("./stm32w_flasher/py_files/stm32w_flasher.py -f -r /contiki-stm32w/"
								+ arg[3] + " -p /dev/ttyACM" + arg[2]);
					} else {
						help();
						session.disconnect();
						return;
					}
				}
				else if (arg[1].equals("-r")) {
					if (arg.length == 3) {
						exec.setCommand("./stm32w_flasher/py_files/stm32w_flasher.py -s -p /dev/ttyACM"
								+ arg[2]);
					} else {
						help();
						session.disconnect();
						return;
					}
				}
				else if (arg[1].equals("-t")) {
					if (arg.length == 3) {
						exec.setCommand("./stm32w/serial -b115200 /dev/ttyACM"
								+ arg[2]);
					}else{
						help();
						session.disconnect();
						return;
					}
				}
				else{
					help();
					session.disconnect();
					return;
				}
				exec.connect(3 * 1000);	
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static abstract class MyUserInfo implements UserInfo,
			UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
		}

		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			return null;
		}
	}
}

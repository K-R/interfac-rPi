//a job is the association of a kernel name and a port number in order to start the right kernel on the right board
public class Job {
	
	private String kernelName;
	private int port;
	
	public Job(int port, String kernelName){
		this.kernelName = kernelName;
		this.port = port;
	}
	
	public int getPort(){
		return port;
	}
	
	public String getKernelName(){
		return kernelName;
	}
	
}

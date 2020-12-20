import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	ArrayList<String> clientList = new ArrayList<String>();
	ClientInfo info;
	
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
			
			Socket connection;
			String name="";
			ObjectInputStream in;
			ObjectOutputStream out;
			String message="";
			ArrayList<String> clientChoice;
			int count;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count=count;
			}
			
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
					
				 while(true) {
					    try {
					    	info = (ClientInfo)in.readObject();
					    	message = info.retMessage();
					    	if(message.isEmpty()) {
					    		name = info.retName();
					    		clientList.add(name);
					    		info.setList(clientList);
					    		info.setAction(true);
					    		for(int i=0;i<clients.size();i++){
					    			clients.get(i).out.writeObject(info);
						    		clients.get(i).out.reset();
					    		}
					    	} else {
					    		info.setList(clientList);
						    	clientChoice = info.retChoice();
					    		info.setAction(false);
					    		info.editMessage(name);
					    		for(int i=0; i<clients.size(); i++) {
						    		for(int j=0; j<clientChoice.size(); j++) {
						    			if(clientChoice.get(j).equals(clients.get(i).name)) {
						    				clients.get(i).out.writeObject(info);
								    		clients.get(i).out.reset();
						    			}
						    		}
						    	}
					    	}
					    	callback.accept("client: " + count + " sent: " );
					    	
					    	}
					    catch(Exception e) {
					    	System.out.println("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	clientList.remove(name);
					    	ClientInfo info = new ClientInfo("", "", new ArrayList<String>());
					    	info.setList(clientList);
					    	info.setAction(true);
					    	clients.remove(this);
					    	for(int i=0;i<clients.size();i++){
					    		try {
					    			clients.get(i).out.writeObject(info);
						    		clients.get(i).out.reset();
					    		}
					    		catch(Exception e1) {
					    			
					    		}
				    			
				    		}
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}


	
	

	

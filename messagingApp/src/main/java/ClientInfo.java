import java.io.Serializable;
import java.util.ArrayList;

public class ClientInfo implements Serializable{
	private static final long serialVersionUID = -4556106911720622350L;
	String name;
	String message;
	ArrayList<String> clientChoice;
	ArrayList<String> clientList;
	Boolean listAction;
	ClientInfo(String name1, String message1, ArrayList<String> clientChoice){
		this.clientChoice=clientChoice;
		this.message=message1;
		this.name=name1;
	}
	String retName() {
		return name;
	}
	String retMessage() {
		return message;
	}
	ArrayList<String> retChoice(){
		return clientChoice;
	}
	void setList(ArrayList<String> clientList) {
		this.clientList=clientList;
	}
	ArrayList<String> retList(){
		return clientList;
	}
	void setAction(Boolean listAction) {
		this.listAction=listAction;
	}
	Boolean retAction() {
		return listAction;
	}
	void editMessage(String name) {
		message+="\nby "+name+"\n";
	}
}

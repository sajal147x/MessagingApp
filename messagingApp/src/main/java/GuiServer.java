
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;
	ClientInfo info;
	ListView<String> listItems, listItems2;
	Boolean check=false;
	TextField enter;
	Button connect;
	String name;
	VBox list;
	TextArea received;
	TextArea sent;
	Button send;
	ArrayList<String> clientChoiceList;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("THE ABSOLUTE CHONK!");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				});
											
		});
		
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("welcome"));
											primaryStage.setTitle("This is an absolute chonk client");
											clientConnection = new Client(data->{
							Platform.runLater(()->{
											synchronized(((ClientInfo)data).retList()){
												if(((ClientInfo)data).retAction()) {
													ArrayList<String> clientList = ((ClientInfo)data).retList();
													list.getChildren().clear();
													for(int i=0; i<clientList.size(); i++) {
														System.out.println(clientList.get(i));
														Button curr = new Button(clientList.get(i));
														curr.setId(clientList.get(i));
														curr.setStyle(
																"-fx-background-color: white;"+
																"-fx-border-width: 0;"+
																"-fx-border-radius: 0;");
														curr.setPrefWidth(200);
														curr.setOnAction(e1->{
															if(clientChoiceList.contains(curr.getId())) {
																curr.setStyle("-fx-background-color: white;");
																clientChoiceList.remove(curr.getId());
																System.out.println(curr.getId());
															} else {
																curr.setStyle("-fx-background-color: green;");
																clientChoiceList.add(curr.getId());
																System.out.println(curr.getId());
															}
														});
														list.getChildren().add(curr);
													}
												} else {
													String acceptedMessage = ((ClientInfo)data).retMessage();
													received.setText(received.getText()+acceptedMessage);
												}
											}
												
										});
							});
							
											clientConnection.start();
		});
		
		connect = new Button("CONNECT");
		connect.setOnAction(e->{
			primaryStage.setScene(sceneMap.get("client"));
			name = enter.getText();
			clientChoiceList = new ArrayList<String>();
			info = new ClientInfo(name, "", clientChoiceList);
			clientConnection.send(info);
			check=true;
		});
		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		
		c1 = new TextField();
		b1 = new Button("Send");
		sent = new TextArea();
		received = new TextArea();
		send = new Button("send");
		
		b1.setOnAction(e->{
			if(check==false) {
				ArrayList<String> clientChoice = new ArrayList<String>();
				info = new ClientInfo(name, "", clientChoice);
				clientConnection.send(info);
				check=true;
			} else {
				ArrayList<String> clientChoice = new ArrayList<String>();
				clientChoice.add(c1.getText());
				info = new ClientInfo("", c1.getText(), clientChoice);
				clientConnection.send(info);
			}
			});
		
		send.setOnAction(e->{
			System.out.println("The choice list is:");
			for(int i=0; i<clientChoiceList.size(); i++) {
				System.out.println(clientChoiceList.get(i));
			}
			info = new ClientInfo("", sent.getText(), clientChoiceList);
			clientConnection.send(info);
		});
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		sceneMap.put("welcome", createWelcomeGui());
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		 
		
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: #128C7E");
		
		pane.setCenter(listItems);
		return new Scene(pane, 500, 400);
		
		
	}
	
	public Scene createWelcomeGui() {
		BorderPane pane = new BorderPane();
		Text disp = new Text("Enter Name:");
		enter = new TextField();
		HBox middle = new HBox(50, disp, enter);
		VBox middleV = new VBox(50, middle, connect);
		middleV.setPadding(new Insets(70));
		pane.setCenter(middleV);
		pane.setPadding(new Insets(70));
		BackgroundImage backGround = new BackgroundImage(new Image("welcomeWallpaper.jpg",1080,720,false,false),BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		pane.setBackground(new Background(backGround));
		Scene s1 = new Scene(pane,500,400);
		s1.getStylesheets().add("style1.css");
		return s1;
	}
	
	public Scene createClientGui() {
		BorderPane pane = new BorderPane();
		list = new VBox();
		Text Contacts = new Text("CONTACTS");
		VBox contactV = new VBox(25, Contacts, list);
		
		Text Chat = new Text("CHAT");
		
		VBox chatV = new VBox(50, Chat, sent);
		
		Text Message = new Text("MESSAGES");
		VBox messageV = new VBox(50, Message, received);
		
		HBox middle = new HBox(50, chatV, messageV);
		
		VBox right = new VBox(send);
		
		Contacts.setId("text");
		Chat.setId("text");
		Message.setId("text");
		
		Contacts.setStyle("-fx-text-fill: white;");
		
		pane.setLeft(contactV);
		contactV.setPadding(new Insets(100,0,0,10));
		pane.setCenter(middle);
		pane.setRight(right);
		right.setPadding(new Insets(150,200,0,0));
		middle.setPadding(new Insets(200,0,300,100));
		BackgroundImage backGround = new BackgroundImage(new Image("wallpaper.jpg",1080,720,false,false),BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		pane.setBackground(new Background(backGround));
		Scene s1 = new Scene(pane, 1980, 1020);
		s1.getStylesheets().add("style2.css");
		return s1;
		
	}

}

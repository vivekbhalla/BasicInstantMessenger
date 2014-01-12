import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class Client extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	// Constructor
	public Client(String host){
		super("Client Messenger");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
			add(userText, BorderLayout.NORTH);
			chatWindow = new JTextArea();
			add(new JScrollPane(chatWindow), BorderLayout.CENTER);
			setSize(400,250);
			setVisible(true);
	}
	
	// Connect and Chat
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileConnected();
		}catch(EOFException eof){
			showMessage("\n Client terminated Connection!");
		}catch(IOException io){
			io.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	
	// Connect to the Server
	private void connectToServer() throws IOException{
		showMessage(" Attempting Connection... \n");
		
		// Here 6789 is the port which is used by the server for incoming requests
		connection = new Socket(InetAddress.getByName(serverIP), 6789); 
		showMessage(" Connecting to " + connection.getInetAddress().getHostName());
	}
	
	// Get Stream to Send & Receive Messages
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Setup Successful! \n");
	}
	
	// While you are connected to the server
	private void whileConnected() throws IOException{
		typeNow(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException cnf){
				showMessage("\n Error getting message!");
			}
		}while(!message.equals("SERVER - END"));
	}
	
	// Close Streams & Sockets
	private void closeConnection(){
		showMessage("\n Closing Connection...\n");
		typeNow(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException io){
			io.printStackTrace();
		}
	}
	
	// Send Message to Server
	private void sendMessage(String message){
		try{
			output.writeObject("CLIENT - "+ message);
			output.flush();
			showMessage("\nCLIENT - " + message);
		}catch(IOException io){
			chatWindow.append("\n Error, can't send message");
		}
	}
	
	// Updates ChatWindow
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}
			}
		);
	}
	
	// Enable user to type
	private void typeNow(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(tof);
				}
			}
		);
	}
}

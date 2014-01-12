import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Server extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	// Constructor
	public Server(){
		super("Server Messenger");
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
		add(new JScrollPane(chatWindow));
		setSize(400,250);
		setVisible(true);
	}
	
	// Setup & Run Server
	public void startRunning(){
		try{
			// Here 6789 is the port being used for incoming requests.
			server = new ServerSocket(6789,100);
			while(true){
				try{
					// Connect & Converse
					waitForConnection();
					setupStreams();
					whileConnected();
				}catch(EOFException eof){
					showMessage("\n Server ended the connection! ");
				}finally{
					closeConnection();
				}
			}
		}catch(IOException io){
			io.printStackTrace();
		}
	}
	
	// Wait for Connection & Display Connection Information
	private void waitForConnection() throws IOException{
		showMessage(" Waiting for a connection...\n");
		connection = server.accept();
		showMessage(" Connecting to "+ connection.getInetAddress().getHostName());
		
	}
	
	// Get Stream to Send & Receive Messages
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Setup Successful! \n");
	}
	
	// While you are Connected
	private void whileConnected() throws IOException{
		String msg = " You are now connected! ";
		sendMessage(msg);
		typeNow(true);
		do{
			try{
				msg = (String) input.readObject();
				showMessage("\n" + msg);
			}catch(ClassNotFoundException cnf){
				showMessage("\n Error getting message!");	
			}
		}while(!msg.equals("CLIENT - END"));	
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
	
	// Send Message to Client
	private void sendMessage(String msg){
		try{
			output.writeObject("SERVER - "+ msg);
			output.flush();
			showMessage("\nSERVER - " + msg);
		}catch(IOException io){
			chatWindow.append("\n Error, can't send message");
		}
	}
	
	// Updates Chat Window
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

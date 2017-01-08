package ie.gmit.sw;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileInputStream;

public class Server {
  public static void main(String[] args) throws Exception {
    ServerSocket m_ServerSocket = new ServerSocket(7777,10);
    int id = 0;
    while (true) {
      Socket clientSocket = m_ServerSocket.accept();
      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
      cliThread.start();
    }
  }
}

class ClientServiceThread extends Thread {
  Socket clientSocket;
  String message;
  int clientID = -1;
  boolean running = true;
  ObjectOutputStream out;
  ObjectInputStream in;

  ClientServiceThread(Socket s, int i) {
    clientSocket = s;
    clientID = i;
  }

  void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
  
  void listFile(){
		try{
			while (true){
				File directory=new File("/Users/weichenwang/Documents");
				File[] files=directory.listFiles();

				try{
					out.writeObject(files);
					out.flush();
					System.out.println("client>" + "files");
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
  void downFile(String filePath){
	
	       try {
	           while (true) {
	          
	               File fi = new File(filePath);
	               System.out.println(filePath);
	               System.out.println("file length:" + (int) fi.length());
	             
	               
	               DataInputStream fis= new DataInputStream(new BufferedInputStream(
	            		   new FileInputStream(filePath)));
	               
	               DataOutputStream ps = new DataOutputStream(clientSocket.getOutputStream());
	               ps.writeUTF(fi.getName());
	               System.out.println(fi.getName());
	               ps.flush();
	               ps.writeLong((long)fi.length());
	               ps.flush();
	               int bufferSize = 8192;
	               byte[] buf = new byte[bufferSize];
	               while (true) {
	                   int read = 0;
	                   if (fis != null) {
	                       read = fis.read(buf);
	                   }
	                   if (read == -1) {
	                       break;
	                   }
	                   ps.write(buf, 0, read);
	               }
	               ps.flush();
	                          
	               fis.close();         
	               System.out.println("complete!");
	           }
	       } catch (Exception e) {
	           e.printStackTrace();
	       } 
	}
  public void run() {
    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
        + clientSocket.getInetAddress().getHostName());
    try 
    {
    	out = new ObjectOutputStream(clientSocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
		message=(String)in.readObject();
		
		if(message.equals("1")){
			System.out.println("client>"+clientID+"  "+ message);
			//if (message.equals("bye"))
			sendMessage("server got the following: "+message);
    	}
      
		if(message.equals("2")){
		  try 
		  {
				System.out.println("client>"+clientID+"  "+ "request download list");
				listFile();
		} catch (Exception e) {
			// TODO: handle exception
		}
	  }
		if(!message.equals("1")&&!message.equals("2")&&!message.equals("0")&&!message.equals("3")){					//
			
		  try 
		  {
			  System.out.println("client>"+clientID+"  "+ "request download");
			  downFile(message);//
		} catch (Exception e) {
			// TODO: handle exception
		}
		  message="0";//
	  }
		System.out.println("Ending Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

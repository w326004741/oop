package ie.gmit.sw;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class client{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message="";
 	File[] files;
 	String path;
 	String ipaddress;
	client(){}
	void run(String s)
	{
		
		try{
			//1. creating a socket to connect to the server
			System.out.println("Please Enter your IP Address");
			requestSocket = new Socket("127.0.0.1",7777);	//;
			System.out.println("Connected to "+ipaddress+" in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Hello");
			//3: Communicating with the server
			if(s.equals("1"))
			{
			message=s;
			sendMessage(message);
			}
			
			if(s.equals("2"))
		  {
			  try 
			  {		
				  message="2";
				  sendMessage(message);
				  files=(File[])in.readObject();
				  int index=0;
				  for(File file : files)
				  {
				   System.out.println(++index+". " + file.getName());					//corrected 1
				  }
				   
				  System.out.println();
			  }catch(Exception e){
				  
			  }
			  
		  }
		  
		  if(s.equals("3")){
			  System.out.println("Please chose the file you want download(number):");
			  Scanner scanner=new Scanner(System.in);									//corrected 2
			  
			  
			  try {			
				  System.out.println(path=files[scanner.nextInt()-1].getPath());
				  
				  path=path.replace('\\', '/');
				  message=path;	//
				  System.out.println(path);
				  sendMessage(message);
				  
				  downFile();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		  }
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void downFile(){
		//从服务器端下载文件
		try { 
		           while(true){  
		               DataInputStream is = new  DataInputStream(requestSocket.getInputStream());   
		               OutputStream os = requestSocket.getOutputStream();       
		               //1、得到文件名       
		               String filename="./downloads/ ";
		
		               filename += is.readUTF(); 
		               System.out.println(filename);
		               System.out.println("新生成的文件名为:"+filename);  
		               FileOutputStream fos = new FileOutputStream(filename);  
		               byte[] b = new byte[1024]; 
		               int length = 0;  
		               if((length=is.read(b))!=-1){  
		                   //2、把socket输入流写到文件输出流中去  
		                   fos.write(b, 0, length);  
		               }  
		               fos.flush();  
		               fos.close();               
		               is.close();  
		           }  
		             
		       } catch (IOException e) {  
		           // TODO Auto-generated catch block  
		           e.printStackTrace();  
		       } 
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	

	public static void main(String args[])
	{

		
		
		
	
			client client = new client();
			while(true)
			{
			System.out.println("1.Connect to Server");
			System.out.println("2.Print File Listing");
			System.out.println("3.Download File");
			System.out.println("4.Quit");
			String com;
			Scanner command=new Scanner(System.in);
			com=command.next();
			
			if(com.equals("1"))
				client.run(com);
			if(com.equals("2"))
				client.run(com);
			if(com.equals("3"))
				client.run(com);
			
			if(com.equals("4")) break;
				
		}
		System.out.println("Software terminated!");//
		
	}
}
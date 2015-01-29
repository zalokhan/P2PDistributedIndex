import java.io.*;
import java.net.*;
import java.util.*;

public class RegServer implements Runnable {
	
	static int cookie;
	public Socket client;
	
	public static LinkedList<Object> table = new LinkedList<Object>();
	public static LinkedList<Object> l1 = new LinkedList<Object>();
	public static LinkedList<Object> l2 = new LinkedList<Object>();
	public static LinkedList<Object> l3 = new LinkedList<Object>();
	public static LinkedList<Long> l4 = new LinkedList<Long>();
	public static LinkedList<Object> l5 = new LinkedList<Object>();
	public static LinkedList<Integer> l6 = new LinkedList<Integer>();
	public static LinkedList<Object> l7 = new LinkedList<Object>();
	public static LinkedList<Long> l8 = new LinkedList<Long>();	//Start time
	public static LinkedList<String> ip = new LinkedList<String>();
	
	/*
	 List 1 : Hostname/IP Address
	 List 2 : Cookie number
	 List 3 : Flag whether the peer is active
	 List 4 : TTL Field initialized to 7200
	 List 5 : Port Number
	 List 6 : Number of times this peer has been active in the last 30 days
	 List 7 : The most recent time this peer has registered
	 */

	
	/*				DECODE MESSAGE FROM CLIENT
						FORMAT OF MESSAGE
			REQ : <sp> Request <sp> version <cr><lf>
			Host : <sp> hostname/ipaddress <cr><lf>
			Port : <sp> port number <cr><lf>
			Cookie : <sp> cookie number <cr><lf>
			Os : <sp> OS with OS version <cr><lf><cr><lf>	
	 */	
	public String decode_request(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_1 = split[0].split("<sp>");	// FIRST LINE SPLITTING
		return(split_1[1]);
	}
	public String decode_version(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_1 = split[0].split("<sp>");	// FIRST LINE SPLITTING		
		return(split_1[2]);
	}
	public String decode_hostname(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_2 = split[1].split("<sp>");	//SECOND LINE SPLITTING
		String[] split_6 = split_2[1].split("/");	//IPADDRESS AND HOSTNAME SPLITTING
		return(split_6[0]);
	}
	public String decode_ipaddress(String request)
	{		
		String[] split = request.split("<cr><lf>");
		String[] split_2 = split[1].split("<sp>");	//SECOND LINE SPLITTING
		String[] split_6 = split_2[1].split("/");	//IPADDRESS AND HOSTNAME SPLITTING
		return(split_6[1]);
	}
	public String decode_port(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_3 = split[2].split("<sp>");	//THIRD LINE SPLITTING
		return(split_3[1]);
	}
	public String decode_cookie(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_4 = split[3].split("<sp>");	//FOURTH LINE SPLITTING
		return(split_4[1]);
	}
	public String decode_os(String request)
	{
		
		String[] split = request.split("<cr><lf>");
		String[] split_5 = split[4].split("<sp>");	//FIFTH LINE SPLITTING
		return(split_5[1]);	
	}

					//REPLY TO CLIENT IN THIS FORMAT
	public String reply(String status, String phrase, String hostname, String os, String cookie, String data)
	{
		
		/*
		 				FORMAT OF MESSAGE
		 		version <sp> status code <sp> phrase <cr><lf>
		 		Host : <sp> hostname <cr><lf>
		 		Os : <sp> OS with OS version <cr><lf>
		 		Cookie : <sp> cookie <cr><lf>
				Data :<sp> Data....<cr><lf><cr><lf>
		 */
		
		String message;
		
		message ="P2P-DI/1.0<sp>"+status+"<sp>"+phrase+"<cr><lf>"
				+"Host :<sp>"+hostname+"<cr><lf>"
				+"OS :<sp>"+os+"<cr><lf>"
				+"Cookie :<sp>"+cookie+"<cr><lf>"
				+"Data :<sp>"+data+"<cr><lf><cr><lf>";
		
		return message;
	}
	
	public RegServer (Socket peer)
	{
		client = peer;
	}
	
	public RegServer() {
		// TODO Auto-generated constructor stub
	}
	public void run()
	{
		try{
			System.out.println("\n\nClient connected to Registration Server");
			//BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
			BufferedReader br2= new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintStream pw = new PrintStream(client.getOutputStream());
			
			boolean loop;
			boolean reg_unreg;
			boolean active;
			int clientport;
			String cook;
			String ipadd;
			loop=true;	
			String header, x, user;
			
			user=br2.readLine();	//Input client hostname
			System.out.println(user);
			clientport=Integer.parseInt(br2.readLine());	//Input RFC Server port number of client
			
			String os_name=System.getProperty("os.name");
			String os_version = System.getProperty("os.version");
			String os=os_name +" "+ os_version;
			
			
			
			while(loop==true)
			{	RegServer r=new RegServer(); 
				x=br2.readLine();
				user=r.decode_hostname(x);
				clientport = Integer.parseInt(r.decode_port(x));
				header=r.decode_request(x);
				ipadd = r.decode_ipaddress(x);
				cook=r.decode_cookie(x);
				System.out.println(cook);
				
				
				System.out.println("The Client has requested for : " + header);
				
				//REGISTER METHOD
				
				if(header.equalsIgnoreCase("REGISTER"))
				{
					//Starting the time for the current peer
					long start_time = System.currentTimeMillis( );
		        	Date dstart =new Date();
										
					reg_unreg=false;
					//Checking if client registered before or not
					for(int i=0; i<l5.size(); i++)
					{
						if(ipadd.equals(ip.get(i).toString()))
						{
							System.out.println("Client already registered with host name = "+user);
							reg_unreg=true;
							break;
						}
					}
					
					if(reg_unreg==false)	//registering a new client port number
					{
						l1.add(user);
						l5.add(clientport);
						l2.add(cookie);
						cookie=cookie+1;
						l7.add(dstart);
						l8.add(start_time);
						l4.add((long) 7200);
						l6.add(1);
						ip.add(ipadd);
					}
					
					System.out.println("Client's port is :" + clientport);
					System.out.println("Client's hostname is :" + user);
					System.out.println("Client's ip address is :" + ipadd);
										
					String response;
					response=r.reply("200", "OK", "Server/Ip", os, (l2.get(ip.indexOf(ipadd))).toString(), "");
					pw.println(response);
					
					//check if IP Address is already present 
					if(reg_unreg==false)
					{
						active=true;	//toggling new peer as active
						l3.add(active);
					}
					else	//toggling old peer as active
					{
						int count=0;
						active=true;
						int position=ip.indexOf(ipadd);
						l3.remove(position);
						l3.add(position, active);
						l4.remove(position);
						l4.add(position, (long) 7200);
						l5.remove(position);
						l5.add(position, clientport);
						//Refreshing last registered time
						l7.remove(position);
						l7.add(position, dstart);
						l8.remove(position);
						l8.add(position, start_time);
						count=l6.get(position);
						count++;
						l6.remove(position);
						l6.add(position, count);
					}
					System.out.println("Client has been registered and exited");
					client.close();
				}
				
				//LEAVE METHOD
				
				if(header.equalsIgnoreCase("LEAVE"))
				{
					loop=false;
					active=false;
					int position=l2.indexOf(Integer.parseInt(cook));
					if(position == -1)	//To see if leaving user had been registered
					{
						System.out.println("The user has not been registered !");
						String response;
						response =r.reply("304", " User Not Registered", "Server/Ip", os, "-1", "");
						pw.println(response);
						client.close();
						break;
					}
					l3.remove(position);
					l3.add(position, active);
					l4.remove(position);
					l4.add(position,(long) 0);
					String response;
					response =r.reply("300", "LEFT", "Server/Ip", os, cook, "");
					pw.println(response);
					System.out.println("Client :"+user+" has left successfully !");
					client.close();
				}
				
				//PQUERY METHOD
				
				if(header.equalsIgnoreCase("PQuery"))
				{
					String data = "s*";
					int position=l2.indexOf(Integer.parseInt(cook));
					if(position == -1)	//To see if requesting user had been registered
					{
						System.out.println("The user has not been registered !");
						String response;
						response =r.reply("304", " User Not Registered", "Server/Ip", os, "-1", "");
						pw.println(response);
						client.close();
						break;
					}
					String check = (l3.get(l2.indexOf(Integer.parseInt(cook)))).toString();
					if(check.equals("false"))//To see if requesting user has been active
					{
						System.out.println("The user has not been registered !");
						String response;
						response =r.reply("304", " User Not Registered", "Server/Ip", os, "-1", "");
						pw.println(response);
						client.close();
						break;
					}
					
					for(int i=0; i<l3.size(); i++)
					{
						boolean b = new Boolean((boolean) l3.get(i)).booleanValue();
						if(b==true)
						{	
							data = data.concat(String.valueOf(l1.get(i)));
							data = data.concat("*");
							data = data.concat(String.valueOf(l5.get(i)));
							data = data.concat("*");
						}
					
					}
					String response;
					response =r.reply("201", "PQUERY ACK", "Server/Ip", os, cook, data);
					
					pw.println(response);
					client.close();
				}
				
				//KEEPALIVE METHOD
				
				if(header.equalsIgnoreCase("KeepAlive"))
				{
					int position=l2.indexOf(Integer.parseInt(cook));
					if(position == -1)	//To see if requesting user had been registered
					{
						System.out.println("The user has not been registered !");
						String response;
						response =r.reply("304", " User Not Registered", "Server/Ip", os, "-1", "");
						pw.println(response);
						client.close();
						break;
					}
					if(l3.get(position).equals(false))
					{
						String response;
						response =r.reply("304", " User Not Registered", "Server/Ip", os, "-1", "");
						pw.println(response);
						break;
					}
					
					long start_time = System.currentTimeMillis( );
					l4.remove(position);
					l4.add(position, (long) 7200);
					
					l8.remove(position);
					l8.add(position, start_time);
					String response;
					response =r.reply("200", "OK", "Server/Ip", os, cook, "");
					pw.println(response);
					System.out.println("The client has refreshed the connection");
					
				}
				
				//DISPLAY OF ENTIRE TABLE (ONLY FOR DEBUGGING)
				
				if(header.equalsIgnoreCase("DISPLAY"))
				{
					System.out.println("The table stored in the Registration Server is:");
					System.out.println(table);
				}
								
				else
				{
					client.close();
				}
				
				
			}
	
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}

	public static void main(String[] args)throws IOException {
			
		
		@SuppressWarnings("resource")
		ServerSocket regser = new ServerSocket(65423);
		Socket cc;
		cookie=1;
		
		table.add(l1);
		table.add(l2);
		table.add(l3);
		table.add(l4);
		table.add(l5);
		table.add(l6);
		table.add(l7);
		
		System.out.println("Server Started !!");
		
		timetolive time = new timetolive();
		Thread t1 = new Thread (time);
		t1.start();
		
		while(true)
		{
			cc=regser.accept(); //listening to a new client
			RegServer r =new RegServer (cc);
			Thread t = new Thread(r);
			t.start();
		}
	}

}

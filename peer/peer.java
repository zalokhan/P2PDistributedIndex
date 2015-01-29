import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;

public class peer implements Runnable{
	
	public static LinkedList<String> rfcnum = new LinkedList<String>();
	public static LinkedList<String> rfctitle = new LinkedList<String>();
	public static LinkedList<String> host = new LinkedList<String>();
	public static LinkedList<Integer> port = new LinkedList<Integer>();
	public static LinkedList<Long> ttl = new LinkedList<Long>();	
	public static LinkedList<Long> start_time=new LinkedList<Long>();
	public static LinkedList<Long> timer=new LinkedList<Long>();
	
	public static LinkedList<String> active_hostname = new LinkedList<String>();
	public static LinkedList<String> active_port = new LinkedList<String>();

	static int cookie;
	static int localport;
	static String hostname;
	public static String path;
	public static String rsip;
		
	public peer ()
	{
	}	
	public String getpath()
	{
		return path;
	}
	public LinkedList<String> getrfcnum()
	{
		return rfcnum;
	}
	public LinkedList<String> getrfctitle()
	{
		return rfctitle;
	}
	public LinkedList<String> gethost()
	{
		return host;
	}
	public LinkedList<Integer> getport()
	{
		return port;
	}
	public LinkedList<Long> getttl()
	{
		return ttl;
	}
			
				//TRANSMISSION MESSAGE FORMATS
	public String reg(String request, String hostname, String port, String os, String cookie)
	{
		
		/*
 						FORMAT OF MESSAGE
 				REQ : <sp> Request <sp> version <cr><lf>
 				Host : <sp> hostname <cr><lf>
 				Port : <sp> port number <cr><lf>
 				Cookie : <sp> cookie number <cr><lf>
 				Os : <sp> OS with OS version <cr><lf><cr><lf>	
		 */
		
		String message;
		
		message="REQ<sp>"+request+"<sp>P2P-DI/1.0<cr><lf>"
				+"Host :<sp>"+hostname+"<cr><lf>"
				+"Port :<sp>"+port+"<cr><lf>"
				+"Cookie :<sp>"+cookie+"<cr><lf>"
				+"OS :<sp>"+os+"<cr><lf><cr><lf>";	
		
		return message;
	}
	public String rfc(String document, String hostname, String os)
	{
		
		/*
		 				FORMAT OF MESSAGE
		 		GET <sp> document <sp> version <cr><lf>
		 		Host : <sp> hostname <cr><lf>
		 		Os : <sp> OS with OS version <cr><lf><cr><lf>	
		 */
		
		String message;
		
		message ="GET<sp>"+document+"<sp>P2P-DI/1.0<cr><lf>"
				+"Host :<sp>"+hostname+"<cr><lf>"
				+"OS :<sp>"+os+"<cr><lf><cr><lf>";
		
		return message;
	}
			
				//RECEPTION DECODING
		/*
					FORMAT OF MESSAGE
			version <sp> status code <sp> phrase <cr><lf>
			Host : <sp> hostname <cr><lf>
			Os : <sp> OS with OS version <cr><lf>
			Cookie : <sp> cookie <cr><lf>
			Data : <sp> data....	<cr><lf><cr><lf>
		*/
	public String decode_version(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_1 = split[0].split("<sp>");	// FIRST LINE SPLITTING
		return(split_1[0]);		
	}
	public String decode_status(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_1 = split[0].split("<sp>");	// FIRST LINE SPLITTING
		return(split_1[1]);
	}
	public String decode_phrase(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_1 = split[0].split("<sp>");	// FIRST LINE SPLITTING
		return(split_1[2]);
	}
	public String decode_hostname(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_2 = split[1].split("<sp>");	//SECOND LINE SPLITTING
		String[] split_4 = split_2[1].split("/");	//IPADDRESS AND HOSTNAME SPLITTING
		return(split_4[0]);
	}
	public String decode_ipaddress(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_2 = split[1].split("<sp>");	//SECOND LINE SPLITTING
		String[] split_4 = split_2[1].split("/");	//IPADDRESS AND HOSTNAME SPLITTING
		return(split_4[1]);
	}
	public String decode_os(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_3 = split[2].split("<sp>");	//THIRD LINE SPLITTING
		return(split_3[1]);
	}
	public String decode_data(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_5 = split[4].split("<sp>");	//THIRD LINE SPLITTING
		return(split_5[1]);
	}
	public String decode_cookie(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_5 = split[3].split("<sp>");	//THIRD LINE SPLITTING
		return(split_5[1]);
	}
	public String decode_data2(String request)
	{
		String[] split = request.split("<cr><lf>");
		String[] split_5 = split[3].split("<sp>");	//THIRD LINE SPLITTING
		return(split_5[1]);
	}
	
	
	public void run()
	{
		try
		{
			System.out.println("This message is printed by the RFC Server");
			ServerSocket peer = new ServerSocket(localport);
			Socket cc;
			while (true)
			{
				cc=peer.accept();
				rfcserver r = new rfcserver (cc);
				Thread t = new Thread(r);
				t.start();
			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException {
		Random rand = new Random();
		int random = rand.nextInt((65500-65400)-1)+65400;		
		cookie=0;
		
		//Initialize RFCServer
		peer p1 = new peer();
		Thread t = new Thread (p1);
		t.start();
		
		localport = random;
		System.out.println("This peer server is using port number : " + localport);
		
		String os_name=System.getProperty("os.name");
		String os_version = System.getProperty("os.version");
		String os=os_name +" "+ os_version;

		
		BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));	//Input from user
		
		System.out.println("Client Started !!");
		String user,response = null;
		InetAddress inet =InetAddress.getLocalHost();	//Returns IP Address
		String[] s1 = (inet.toString()).split("/");
		hostname = s1[0];
		
		System.out.println("Enter IP Address of the Registration Server.");
		rsip=br1.readLine();
		
		while(true)
			{
				System.out.println("Enter RFC directory path");
				path = br1.readLine();
				String file;
				boolean b = new File(path).exists();
				if(b==true)
				{
					break;
				}
				else
				{
					System.out.println("Invalid directory name...\nPlease try again !");
				}
			}
		
		while(true)
		{
			
			try
			{	
			peer p2 = new peer(); //creating object to use decode funtions
			//DISPLAYING MENU FOR CLIENT INTERFACE
			System.out.println("\nChoose one of the following commands:\n"
					+ "REGISTER\n"
					+ "PQuery\n"
					+ "KeepAlive\n"
					+ "LEAVE\n"
					+ "RFCQuery\n"
					+ "GetRFC\n"
					+ "Get allrfc\n"
					+ "RFCdisplay");
			System.out.print("Enter a Command: ");
			user=br1.readLine();
			
			
			{
			//REMOVING ENTRIES OF OWN FILES FROM LIST FOR UPDATION
			for(int i=0; i<rfcnum.size(); i++)
			{
				if(host.get(i).equals(hostname))
				{
					rfcnum.remove(i);
					host.remove(i);
					rfctitle.remove(i);
					port.remove(i);
					ttl.remove(i);
					start_time.remove(i);
					i--;
				}
			}
			
			}
			
			//ADDING RFC FROM PERSONAL DIRECTORY
			   
			  String file;
			  File folder = new File(path);
			 
			  File[] listOfFiles = folder.listFiles(); 
			  for (int i = 0; i < listOfFiles.length; i++) 
			  {
				  
				  if (listOfFiles[i].isFile()) 
				  {
					  file = listOfFiles[i].getName();
					  if (file.substring(0, 3).equals("rfc"))
					  {
						  String[] split = file.split("=");
						  rfcnum.add(split[0]);
						  rfctitle.add(split[1]);
						  host.add(hostname);
						  port.add(localport);
						  ttl.add((long) 7200);
						  start_time.add((long) 0);
					  }
				  }
			  }
			
			
			
			
			
			try{
				
				Long diff = null;
				Long ttlive = null;
				long current_time = System.currentTimeMillis( );
					for(int i=0; i<ttl.size(); i++ )
					{
						if((host.get(i)).equals(hostname))
						{
						}
						else
						{
							diff = current_time-start_time.get(i);
							start_time.remove(i);
							start_time.add(i, current_time);
							ttlive = (Long)(ttl.get(i));
							ttlive=ttlive*1000;
							ttlive=(ttlive-diff)/1000;
							if (ttlive<=0)
							{
								//Remove entry if ttl has expired
								rfcnum.remove(i);
								host.remove(i);
								rfctitle.remove(i);
								port.remove(i);
								ttl.remove(i);
								start_time.remove(i);
							}
							else
							{
								ttl.remove(i);
								ttl.add(i, ttlive);
							}
						}
					}		
				
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
			
			
			//REMOVING DUPLICATES FROM RFC INDEX
			for (int q=0;q<50;q++ )  //Ensure duplicacy removal
			{
			for (int i=0; i<rfcnum.size(); i++)
			{
				for (int j=0; j<rfcnum.size(); j++)
				{	
					if(i==j)
					{
						continue;
					}
					if (rfcnum.get(i).equals(rfcnum.get(j)))
					{
						if (host.get(i).equals(host.get(j)))
								{
								rfcnum.remove(j);
								host.remove(j);
								rfctitle.remove(j);
								port.remove(j);
								ttl.remove(j);
								start_time.remove(j);
								}
					}
				}
			}
			}
			//CHECKING FOR SERVER CONNECTION OR P2P CONNECTION
			
			if(user.equalsIgnoreCase("REGISTER")||user.equalsIgnoreCase("LEAVE")||user.equalsIgnoreCase("PQuery")||user.equalsIgnoreCase("KeepAlive")||user.equalsIgnoreCase("DISPLAY"))
			{	
				//REGISTER SERVER FUNCTIONS
			
				Socket sv = new Socket(rsip,65423);
				BufferedReader br2= new BufferedReader(new InputStreamReader(sv.getInputStream()));	//Input from Socket
				PrintStream pw = new PrintStream(sv.getOutputStream());
				pw.println(inet);
				pw.println(localport);

			
							//REGISTER
				
				if(user.equalsIgnoreCase("REGISTER"))
				{
					user=p2.reg("REGISTER", inet.toString(), Integer.toString(localport), os, Integer.toString(cookie));
					pw.println(user);
					
					response =br2.readLine();
					String status = p2.decode_status(response);
					String phrase = p2.decode_phrase(response);
					int x=Integer.parseInt(p2.decode_cookie(response));
					cookie = x;					
					System.out.println("Response from Server : "+status+phrase);
					System.out.println("Your cookie is : "+cookie);					

				}
			
			
							//LEAVE
			
				if(user.equalsIgnoreCase("leave"))
				{
					user=p2.reg("LEAVE", inet.toString(), Integer.toString(localport), os, Integer.toString(cookie));
					pw.println(user);
					response=br2.readLine(); // For client console
					String status = p2.decode_status(response);
					String phrase = p2.decode_phrase(response);
					System.out.println("Response from Server : "+status+phrase);
					sv.close();
				}
			
			
							//PQuery
				
				if(user.equalsIgnoreCase("pquery"))
				{		
					user=p2.reg("PQuery", inet.toString(), Integer.toString(localport), os, Integer.toString(cookie));
					pw.println(user);
					
					active_hostname.clear();
					active_port.clear();
				
					response=br2.readLine();
					
					if(p2.decode_status(response).equals("304"))
					{
						System.out.println("Response from Server : "+p2.decode_status(response)+p2.decode_phrase(response));
						continue;
					}
					
					System.out.println("Receiving list of active peers");
					String data = p2.decode_data(response);
					String[] s = data.split("\\*");
					
					for(int i=1; i<(s.length)-1; i++)
					{
						String[] s3=s[i].split("/");
						active_hostname.add(s3[0]);
						i++;
						active_port.add(s[i]);
					}
					
					for(int k = 0; k<active_port.size(); k++)
					{
						if(localport == Integer.parseInt(active_port.get(k)))
						{
							active_port.remove(k);
							active_hostname.remove(k);
						}
					}
					
					if(active_port.size()==0)
					{
						System.out.println("No other clients are active right now");
						System.out.println("Please try again later");
					}
						
					for(int i=0; i<active_hostname.size();i++)
					{
						System.out.println("Host"+i+" : "+active_hostname.get(i));
						System.out.println("Port"+i+"     : "+active_port.get(i));
					}
				

				}
			
			
							//KeepAlive
				
				if(user.equalsIgnoreCase("KeepAlive"))
				{					
					user=p2.reg("KeepAlive", inet.toString(), Integer.toString(localport), os, Integer.toString(cookie));
					pw.println(user);
					response=br2.readLine(); // For client console
					String status = p2.decode_status(response);
					String phrase = p2.decode_phrase(response);
					System.out.println("Response from Server : "+status+phrase);
				}
			
			
							//Display
				if(user.equalsIgnoreCase("DISPLAY"))
				{	
					user=p2.reg("DISPLAY", inet.toString(), Integer.toString(localport), os, Integer.toString(cookie));
					pw.println(user);
					System.out.println("The Entire table is displayed on the server side");
				}
							
			}
			
			
			else
			{
						//RFC SERVER CONNECTION
				
				
							//RFC Query
				
				if(user.equalsIgnoreCase("RFCQuery"))
				{ 
					
						long st=System.currentTimeMillis( );
					
					for(int i=0; i<active_port.size(); i++)
					{
					try
					{		
						Socket p2p = new Socket(active_hostname.get(i), Integer.parseInt(active_port.get(i)));
						BufferedReader br3= new BufferedReader(new InputStreamReader(p2p.getInputStream()));	//Input from Socket
						PrintStream pw2 = new PrintStream(p2p.getOutputStream());
						peer rfc = new peer();
						
						user=rfc.rfc("RFC-index", inet.toString(), os);
						
						pw2.println(user);
						

							response=br3.readLine();
							System.out.println("Receiving list of RFCs");
							String data = p2.decode_data2(response);
							String[] s = data.split("\\*");
							
							for(int i1=1; i1<(s.length)-1; i1++)
							{
								rfcnum.add(s[i1]);
								i1++;
								rfctitle.add(s[i1]);
								i1++;
								String[] s3=s[i1].split("/");
								
								host.add(s3[0]);
								i1++;
								port.add(Integer.parseInt(s[i1]));
								ttl.add((long) 7200);
								start_time.add(st);
								
							}	
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
						continue;
					}
				
					
					//Input of RFC List
					}
					//REMOVING DUPLICATES FROM RFC INDEX
				
					for (int i=0; i<rfcnum.size(); i++)
					{
						for (int j=0; j<rfcnum.size(); j++)
						{	
							if(i==j)
							{
								continue;
							}
							if (rfcnum.get(i).equals(rfcnum.get(j)))
							{
								if (host.get(i).equals(host.get(j)))
										{
										rfcnum.remove(j);
										host.remove(j);
										rfctitle.remove(j);
										port.remove(j);
										ttl.remove(j);
										start_time.remove(j);
										j--;
										}
							}
						}
					
					
					}
					for(int i=0; i<rfcnum.size(); i++)
					{
						 System.out.println(host.get(i)+" : "+rfcnum.get(i)+"-"+rfctitle.get(i));
					}
					System.out.println(rfcnum.size());
					
				}
				
					//GET RFC
				
				if(user.equalsIgnoreCase("GetRFC"))
				{
					int num = 0;
					String reqdrfc, s;
					boolean f1=false;
					while(true)
					{
						try		//INPUT VALID 4 DIGIT NUMBER FROM USER
						{
							System.out.println("Enter the 4 digit rfc number : ");
							s = br1.readLine();
							num = Integer.parseInt(s);
							if(s.length()<4)
							{
								System.out.println("Invalid entry");
								continue;
							}
							break;
						}
						catch(Exception e)
						{
							System.out.println("Invalid entry");
							System.out.println(e.getMessage());
							continue;
						}
												
					}
					
					reqdrfc = "rfc"+s;
					//Checking if file present in this client's directory
					for(int i =0; i<rfcnum.size(); i++)
					{
						if(rfcnum.get(i).equals(reqdrfc))
						{
							if(host.get(i).equals(hostname))
							{
								System.out.println("RFC is already present in your system");
								f1=true;
								break;
							}
						}
					}
					if(f1==true)
					{
						continue;
					}
					boolean b = false;
					
					//TRYING TO CONTACT ALL PEERS TO OBTAIN REQUIRED FILE
					for(int i=0; i<rfcnum.size(); i++)
					{
						try
						{
							if(rfcnum.get(i).equals(reqdrfc))
							{
								Socket p2p = new Socket(host.get(i), port.get(i));
								BufferedReader br3= new BufferedReader(new InputStreamReader(p2p.getInputStream()));	//Input from Socket
								PrintStream pw2 = new PrintStream(p2p.getOutputStream());
								peer rfc = new peer();
								user=rfc.rfc("RFC "+s, inet.toString(), os);
								pw2.println(user);
								String respFile = null;
								//Receive and write file
								String a=null;
								while((a=br3.readLine())!=null){
									response=a;
									respFile = respFile+response+"\n";
								}
								
								String data = p2.decode_data2(respFile);
								
								String[] s2 =data.split("@@@");
								String filename = s2[0];
								String content = s2[1];
								PrintWriter out = new PrintWriter(path+"\\"+filename);
								out.println(content);
								out.close();
								
								System.out.println("The file has been received and stored successfully");
								b=true;
								break;
							}
							else
							{
								b=false;
							}
						}
						catch(Exception e)
						{	System.out.println(e.getMessage());
							System.out.println("The peer "+host.get(i)+" is inactive right now.");
							continue;
						}
					}
					if(b==false)
					{
						System.out.println("Sorry file not present in the RFC Index");
					}
					
				}//GetRFC
				
									//GET ALL RFCS
				
				
				if(user.equalsIgnoreCase("GetallRFC"))
				{
					int num = 0;		
					boolean b = false;
					
					
					//TRYING TO CONTACT ALL PEERS TO OBTAIN REQUIRED FILE
					for(int i=0; i<rfcnum.size(); i++)
					{
						
						b=false;
						for(int j=0; j<rfcnum.size(); j++)
						{
							
						
							if(rfcnum.get(i).equals(rfcnum.get(j)))
							{
								if (host.get(j).equals(hostname))
								{
									
									b= true;
									break;
								}
							}
						}
						
						try
						{
								if(b==false)
								{	
								Socket p2p = new Socket(host.get(i), port.get(i));
								BufferedReader br3= new BufferedReader(new InputStreamReader(p2p.getInputStream()));	//Input from Socket
								PrintStream pw2 = new PrintStream(p2p.getOutputStream());
								peer rfc = new peer();
								
								String s =rfcnum.get(i).substring(3, 7);
								//System.out.println(s);
								user=rfc.rfc("RFC "+s, inet.toString(), os);
								
								long t1=System.currentTimeMillis( );
								
								pw2.println(user);
								String respFile = null;
								//Receive and write file
								String a=null;
								while((a=br3.readLine())!=null)
								{
									response=a;
									respFile = respFile+response+"\n";
								}
								
								long t2=System.currentTimeMillis( );
								long t3 = t2-t1;
								timer.add(t3);
								
								String data = p2.decode_data2(respFile);
								
								String[] s2 =data.split("@@@");
								String filename = s2[0];
								String content = s2[1];
								PrintWriter out = new PrintWriter(path+"\\"+filename);
								out.println(content);
								out.close();
								}
							
						}
						catch(Exception e)
						{	System.out.println(e.getMessage());
							System.out.println("The peer "+host.get(i)+" is inactive right now.");
							continue;
						}
					}
					
					System.out.println("The files have been received and stored successfully");
					PrintWriter time = new PrintWriter(path+"\\"+"time.txt");
					long sum=0;
					for(int k=0; k<timer.size(); k++)
					{
						System.out.println(timer.get(k));
						time.println(timer.get(k));
						sum = sum+timer.get(k);
					}
					
					System.out.println(sum);
					time.println(sum);
					time.close();
		
					
				}//GetRFC
				
				
				
									//RFC INDEX DISPLAY
				if(user.equalsIgnoreCase("RFCdisplay"))
				{
					System.out.println("\nDisplaying RFC Index:"
									+ "\nrfc number :"+rfcnum
									+ "\nhost :"+host
									+ "\nport :"+port
									+ "\nttl : "+ttl);
				}
				
				
			}//else
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			continue;
		}

		}//while
		

	}// Main

}//Class

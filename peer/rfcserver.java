import java.net.*;
import java.util.LinkedList;
import java.io.*;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;

public class rfcserver implements Runnable{
	
	public static LinkedList<String> rfcnum = new LinkedList<String>();
	public static LinkedList<String> rfctitle = new LinkedList<String>();
	public static LinkedList<String> host = new LinkedList<String>();
	public static LinkedList<Integer> port = new LinkedList<Integer>();
	public static LinkedList<Long> ttl = new LinkedList<Long>();
	
	Socket client;
	String request;
	
	public rfcserver(Socket peer)
	{
		client = peer;
	}
	
	public rfcserver() 
	{
	}

	/*
							FORMAT OF MESSAGE
				GET <sp> document <sp> version <cr><lf>
				Host : <sp> hostname <cr><lf>
				Os : <sp> OS with OS version <cr><lf><cr><lf>	
	 */
	
	
	public String decode_document(String request)
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
	
	public String reply(String status, String phrase, String hostname, String os, String data)
	{
		
		/*
		 				FORMAT OF MESSAGE
		 		version <sp> status code <sp> phrase <cr><lf>
		 		Host : <sp> hostname <cr><lf>
		 		Os : <sp> OS with OS version <cr><lf>
		 		Data : data....	<cr><lf><cr><lf>
		 */
		
		String message;
		
		message ="P2P-DI/1.0<sp>"+status+"<sp>"+phrase+"<cr><lf>"
				+"Host :<sp>"+hostname+"<cr><lf>"
				+"OS :<sp>"+os+"<cr><lf>"
				+"Data :<sp>"+data+"<cr><lf><cr><lf>";
		
		return message;
	}
	
	
	public void run() 
	{
		try
		{
			peer p = new peer();
			rfcnum = p.getrfcnum();
			rfctitle = p.getrfctitle();
			host = p.gethost();
			port = p.getport();
						
			BufferedReader br2= new BufferedReader(new InputStreamReader(client.getInputStream()));	//Input from Socket
			PrintStream pw = new PrintStream(client.getOutputStream());
			rfcserver r = new rfcserver();			//Using default constructor
			InetAddress inet =InetAddress.getLocalHost();	//Returns IP Address
			
			
			request=br2.readLine();
			
			String c_document = r.decode_document(request);
			String c_version = r.decode_version(request);
			String c_hostname = r.decode_hostname(request);
			String c_ipaddress = r.decode_ipaddress(request);
			String c_os = r.decode_os(request);
			String paths = p.getpath();
						
						//RFC QUERY BLOCK
			
			if(c_document.equalsIgnoreCase("RFC-index"))
			{
				
					String data = "s*";
					
					for(int i=0; i<rfcnum.size(); i++)
					{
						
							data = data.concat(String.valueOf(rfcnum.get(i)));
							data = data.concat("*");
							data = data.concat(String.valueOf(rfctitle.get(i)));
							data = data.concat("*");
							data = data.concat(String.valueOf(host.get(i)));
							data = data.concat("*");
							data = data.concat(String.valueOf(port.get(i)));
							data = data.concat("*");						
					}
					
					String response;
					response =r.reply("501", "RFCQUERY ACK", inet.toString(), System.getProperty("os.name"), data);
					
					pw.println(response);
					client.close();
			}
				
			
						//GET RFC BLOCK
			
			//GET RFC BLOCK
			if(c_document.substring(0,4).equalsIgnoreCase("RFC "))
			{
				String num=c_document.substring(4,8);
				String file;
				String data="";
				File folder = new File(paths);
			 
				File[] listOfFiles = folder.listFiles();
			 
				for (int i=0; i<listOfFiles.length;i++)
				{
						file = listOfFiles[i].getName();
			 
						String w=file.substring(3,7);
						if(w.equals(num))
						{
							String rfcpath=(paths+"\\"+file);
							String data1 = new String(readAllBytes(get(rfcpath)));
							data = file+"@@@"+data1;
							break;
						}
			 
				}
			 
				String response;
				response =r.reply("222", "GetQUERY ACK", inet.toString(), System.getProperty("os.name"), data);
				pw.println(response);
				
				client.close();
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}


	}
}
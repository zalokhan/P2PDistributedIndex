Instructions for compiling and running the Registration Server and Peer Programs. (Windows Command Prompt)

Install jdk 1.7.0. 

Registration Server-
The registration server comprises of 2 files
RegServer.java - contains the main function.
timetolive.java - It is a thread which continuously and parallely checks and updates the TTL field of the Registration Index.

1. First set path of the bin folder of "jdk 1.7.0"

2. Now open cmd and Go to the directory in cmd where your .java files are stored. [If cmd was already running restart cmd]

3. Note down the IP address of the Server machine.

4. Compile the 2 files using the command  
    "javac RegServer.java timetolive.java"

5. Run only the main class file using the following command 
    "java RegServer"

6. Now the Server program should be running and the following message should appear on the console "Server Started!"

7. At any point in time, if the user wishes to view the Registration Index, it can be done using Display command and this Index would be displayed on the server side.



Peer-
The Peer also comprises of 2 files
peer.java - comprises of the peer client as main as well as spawns a parallel running RFC server thread.
rfcserver.java - A thread of this class is initiated every time a client contacts.
Assume 2 folders RFC-A and RFC-B

Store the RFC-A folder in the peer A machine. This is an empty folder.

Store the RFC-B folder in the peer B machine. This contains the two RFCs required for testing.

1. Go to the directory in cmd where your .java files are stored.

2. Compile the 2 files using the command  
    "javac peer.java rfcserver.java"

3. Run only the main class file using the following command 
    "java peer"

4. Now the Peer program should be running and the following message should appear on the console "Client Started!"

5. It will also ask the user to enter the IP address of the registration server. 

6. It will now ask the user to enter a directory path where the RFC files are stored for upload or where the user would like to download the desired RFC files.
    Enter the correct RFC Directory path.

7. It will now ask the user to enter a command. The list of commands will be mentioned. 
    The first command to be entered should be "register". If the peer is not registered initially, it will give an error message saying "user not registered".
    On successful registration, the server will assign and send a cookie number which will be displayed on the client console along with hostname/IP address and other such details.

8. Now the user may choose to enter the command "pquery" which will fetch and display a list of all active peers from the Registration Index.

9. Next the user may choose to enter the command "rfcquery" which will fetch and display a list of all RFC files present in own system as well as the RFC's of the other active peers.
    When this command is entered, this spawns a new rfcserver thread on the receiver end which serves a request from one particular peer client.
    After this, the RFC index at the source end will be updated.

10. With this updated RFC Index, the user may now choose to download a single RFC or all RFCs from the index.
      For a single RFC, the user may enter the command "GetRFC" which will then ask the user to enter a particular RFC Number from the index.
      On entering a valid number, the RFC file will be fetched and stored into the RFC directory.
      For all RFCs, the user may enter the command "GetAllRFC". This will fetch all RFC files from each active peer from the RFC index list.

11. At any point in time, if the user wishes to view the RFC Index, it can be done using "rfcdisplay" command.

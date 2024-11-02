Project Name: IMPLEMENTATION OF FTP CLIENT AND SERVER WITH MULTIPLE THREADS

This zip file holds a repository that implements the FTP Server and Client with multiple threads.

Problem Statement:

Implement a simple version of client/server software. It consists of two programs: FTPClient and FTPServer. 
First, FTPServer is started on a computer. It  listens  on  a  certain  TCP  port  (such  as  4007) and is capable of supporting multiple 
clients with different threads. Then,  two FTPClient  are  executed  on  the  same computer where the server runs;  the  server’s  port 
number  are  supplied in the command line,  for  example, “ftpclient  4007”.    The  user  can  issue  a  command  at  each of the 
two clients:  “get  <filename>”, which is to retrieve a file from the server, or “upload < filename>”, which is to upload a file to the server.
Because the file could be arbitrarily large, you are required to split the file into chunks of 1K bytes and use
a loop to send the chunks, each time one chunk.

Execution:

Compilation steps in cmd:

1. Server -->  javac FTPServer.java
2. Client1 --> javac FTPClient.java
3. Client2 --> javac FTPClient.java

Running program in cmd:

1. Server --> java FTPServer.java
2. Client1 --> java FTPClient.java
3. Client2 --> java FTPClient.java

Test Case1:

Start server on one cmd window
Start client1 on another cmd window
Start clinet2 on another cmd window
Enter command: ftpclient 5100 (5100 is the port number. You can try entering invalid commands here)
From the Menu options select 1. Send File 2. Receive File 3. Exit
i. Send file will prompt the user to enter the file name to be uploaded to the server. Enter valid file name to be uploaded here.
ii. Receive file will prompt the user to enter the file name to be downloaded from the server. Enter valid file name to be downloaded here.
iii. Terminate the program.

Note: The files to be uploaded and downloaded should be in the same folder as in the FTPServer and FTPClient java programs. 
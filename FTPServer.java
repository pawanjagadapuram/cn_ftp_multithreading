import java.net.*;
import java.io.*;
import java.util.*;

public class FTPServer {
    public static void main(String args[]) throws Exception {
        ServerSocket soc = new ServerSocket(5100);
        System.out.println("FTP Server Started on Port Number 5100");
        transferfile t = new transferfile(soc);
    }
}

class transferfile extends Thread {
    ServerSocket serverSocket;
    Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;

    transferfile(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
		System.out.println("Waiting for connection ...");
            this.run();
        } catch (Exception ex) {
        }
    }

    void initializeStreams() {
        try {
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
        } catch (Exception ex) {

        }

    }

    void closeStreams() {
        try {
            din.close();
            dout.close();
        } catch (Exception ex) {

        }

    }

	void SendFile() throws Exception {
        String filename = din.readUTF();
        File f = new File(filename);
        if (!f.exists()) {
            dout.writeUTF("File Not Found");
            return;
        } else {
            dout.writeUTF("READY");
            FileInputStream fin = new FileInputStream(f);
		long fileLength = f.length();
		int downloadedBytes = 0;
		while(downloadedBytes < fileLength) {
		int currentDownloadBytes = 1000;
		if (fileLength - downloadedBytes < 1000) {
			currentDownloadBytes = (int) (fileLength - downloadedBytes);
		}
		byte[] currentDownload = new byte[currentDownloadBytes];
		fin.read(currentDownload,0,currentDownloadBytes);
		dout.write(currentDownload,0,currentDownloadBytes);
		downloadedBytes += currentDownloadBytes;
		}
		fin.close();
		System.out.println("Client download successful!");
}
	}

	void ReceiveFile() throws Exception {
        String filename = din.readUTF();
        if (filename.compareTo("File not found") == 0) {
            return;
        }
        File f = new File("new" + filename);
        String option;
        if (f.exists()) {
            dout.writeUTF("File Already Exists");
            option = din.readUTF();
        } else {
            dout.writeUTF("SendFile");
            option = "Y";
        }

        if (option.compareTo("Y") == 0) {
            FileOutputStream fout = new FileOutputStream(f);
            while (true) {
                byte[] currentReceive = new byte[1000];
                int bytesReceived = din.read(currentReceive, 0, 1000);
                fout.write(currentReceive, 0, 1000);
                if (bytesReceived == -1)
                    break;
            }
            fout.close();
            System.out.println("Client upload successful!");
        } else {
            return;
        }
}

	public void run() {
        while (true) {
            try {
                ClientSoc = serverSocket.accept();
                initializeStreams();
                System.out.println("Waiting for Command ...");
                String Command = din.readUTF();
                if (Command.compareTo("GET") == 0) {
                    System.out.println("\tGET Command Received ...");
                    SendFile();
                } else if (Command.compareTo("SEND") == 0) {
                    System.out.println("\tSEND Command Received ...");
                    ReceiveFile();
                } else if (Command.compareTo("DISCONNECT") == 0) {
                    System.out.println("\tDisconnect Command Received ...");
                    closeStreams();
                }
                closeStreams();
		    } catch (Exception ex) {
            }
        }
    }
}
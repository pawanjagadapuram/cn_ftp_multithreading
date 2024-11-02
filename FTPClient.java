import java.net.*;
import java.io.*;
import java.util.*;

class FTPClient {
    public static void main(String args[]) throws Exception {
        while (true) {
            try {
                System.out.println("Enter input as : ftpclient <Port number>");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String command = br.readLine();
                String[] str = command.split(" ");
                if (str[0].compareTo("ftpclient") == 0) {
                    int port = Integer.parseInt(str[1]);
                    if (port == 5100) {
                        transferfileClient t = new transferfileClient("localhost", port);
				System.out.println("Connected to the Server!!");
                        t.displayMenu();
                    } else {
                        System.out.println("Wrong port number!!");
                    }
                } else {
                    System.out.println("Type correct command!!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

class transferfileClient {
    Socket ClientSoc;
    DataInputStream din;
    boolean inStreamClosed;
    DataOutputStream dout;
    boolean outStreamClosed;
    BufferedReader br;

    String serverIP;
    int serverPort;

    transferfileClient(String serverIP, int serverPort) {
        try {
            inStreamClosed = true;
            outStreamClosed = true;
            this.serverPort = serverPort;
            this.serverIP = serverIP;

            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception ex) {
        }
    }

    void initializeStreams() {
        try {
            din = new DataInputStream(ClientSoc.getInputStream());
            inStreamClosed = false;
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            outStreamClosed = false;
        } catch (Exception ex) {

        }
    }

    void closeStreams() {
        try {
            if (!inStreamClosed)
                din.close();
            if (!outStreamClosed)
                dout.close();
        } catch (Exception ex) {

        }

    }

    void initialiseSocket() throws  IOException{
            ClientSoc = new Socket(serverIP, serverPort);
    }

    void SendFile() throws Exception {
        dout.writeUTF("SEND");
        String filename;
        System.out.print("Enter File Name :");
        filename = br.readLine();
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("File not Exists...");
            dout.writeUTF("File not found");
            return;
        }
        dout.writeUTF(filename);
        String msgFromServer = din.readUTF();
        if (msgFromServer.compareTo("File Already Exists") == 0) {
            String Option;
            System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
            Option = br.readLine();
            if (Option == "Y") {
                dout.writeUTF("Y");
            } else {
                dout.writeUTF("N");
                dout.close();
                return;
            }
        }
        System.out.println("Sending File ...");
        FileInputStream fin = new FileInputStream(f);
        long fileLength = f.length();
        int uploadedBytes = 0;
        while (uploadedBytes < fileLength) {
            int currentUploadBytes = 1000;
            if (fileLength - uploadedBytes < 1000) {
                currentUploadBytes = (int) (fileLength - uploadedBytes);
            }

            byte[] currentUpload = new byte[currentUploadBytes];
            fin.read(currentUpload, 0, currentUploadBytes);
            dout.write(currentUpload, 0, currentUploadBytes);

            uploadedBytes += currentUploadBytes;
        }
        fin.close();
        System.out.println("Upload done!");
    }
void ReceiveFile() throws Exception {
        dout.writeUTF("GET");
        String fileName;
        System.out.print("Enter File Name :");
        fileName = br.readLine();
        dout.writeUTF(fileName);
        String msgFromServer = din.readUTF();
        if (msgFromServer.compareTo("File Not Found") == 0) {
            System.out.println("File not found on Server ...");
            return;
        } else if (msgFromServer.compareTo("READY") == 0) {
            System.out.println("Receiving File ...");
            File f = new File("new" + fileName);
            if (f.exists()) {
                String Option;
                System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
                Option = br.readLine();
                if (Option == "N") {
                    return;
                }
            }
            FileOutputStream fout = new FileOutputStream(f);
            while (true) {
			byte[] currentReceive=new byte[1000];
			int bytesReceived = din.read(currentReceive, 0, 1000);
			fout.write(currentReceive, 0, 1000);
			if (bytesReceived == -1)
				break;
			}
			fout.close();
			System.out.println("Download done!");
		}else {
			return;
		}
	}

    public void displayMenu() throws Exception {
        while (true) {

            System.out.println("[ MENU ]");
            System.out.println("1. Send File");
            System.out.println("2. Receive File");
            System.out.println("3. Exit");
            System.out.print("\nEnter Choice :");
            int choice;
            choice = Integer.parseInt(br.readLine());
            initialiseSocket();
            initializeStreams();
            if (choice == 1) {
                SendFile();
            } else if (choice == 2) {
                ReceiveFile();
            } else if (choice == 3){
                dout.writeUTF("DISCONNECT");
                closeStreams();
                System.exit(1);
            }else {
		System.out.println("Enter valid choice!!");
		}
            closeStreams();
        }
    }
}
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner scanner = new Scanner(System.in);

    public Server() {
        try {
            this.serverSocket = new ServerSocket(1234);
            this.clientSocket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go() {
        Thread send = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                while (true) {
                    try {
                        msg = scanner.nextLine();
                        bufferedWriter.write(msg);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        send.start();

        Thread receive = new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                try {
                    msg = bufferedReader.readLine();
                    while (msg != null) {
                        System.out.println("Client: " + msg);
                        msg = bufferedReader.readLine();
                    }

                    System.out.println("Client disconnected");
                    bufferedWriter.close();
                    clientSocket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receive.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.go();
    }

}

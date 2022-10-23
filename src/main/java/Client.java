import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner scanner = new Scanner(System.in);

    public Client() {
        try {
            this.clientSocket = new Socket("127.0.0.1", 1234);
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
            String msg;
            @Override
            public void run() {
                try {
                    msg = bufferedReader.readLine();
                    while (msg != null) {
                        System.out.println("Server: " + msg);
                        msg = bufferedReader.readLine();
                    }
                    System.out.println("Server out of service");
                    bufferedReader.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receive.start();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.go();
    }
}

package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Main {
    private static final int PORT = 34721;
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        Main.clientConnection();
    }


    public static void clientConnection() {
        Main server = new Main();
        boolean running = true;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(running) {
                Socket clientSocket = serverSocket.accept();
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
                String request = input.readUTF();
                if (request.equals("EXIT")) {
                   break;
                }

                String response = server.requestHandling(request);
                output.writeUTF(response);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestHandling (String request) {
        String httpMethod = request.substring(0, request.indexOf(' '));
        String rest = request.substring(request.indexOf(' ') + 1);
        String fileName = rest.substring(0, rest.indexOf(' '));
        String content = rest.substring(rest.indexOf(' ') + 1);

        String message = "";
        try {
            message = switch (httpMethod) {
                case "GET" -> ServerFileService.getFileData(fileName);
                case "PUT" -> ServerFileService.putFileData(fileName, content);
                case "DELETE" -> ServerFileService.deleteFile(fileName);
                default -> null;
            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

}
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;


public class Main {
    private static final int SERVER_PORT = 34721;
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main.serverConnection();
    }

    public static void serverConnection() {
        Main client = new Main();
        boolean running = true;
        System.out.println("Server started!");
        while (running) {
            try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            ) {
                String request = client.actionHandling();
                output.writeUTF(request);
                if (request.equals("EXIT")) {
                    break;
                }
                String response = input.readUTF();
                client.responseHandling(response, request);

            } catch (ConnectException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    public String actionHandling() {
        System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
        String action = scanner.nextLine();
        String request = "";
        if (action.equals("exit") || action.equals("EXIT")) {
            return "EXIT";
        }

        System.out.println("Enter filename:");
        String filename = scanner.nextLine();
        String httpMethod = "";

        switch(action) {
            case "1" -> {
                httpMethod = "GET";
                request = String.format("%s %s ", httpMethod, filename);
            }
            case "2" -> {
                httpMethod = "PUT";
                System.out.println("Enter file content:");
                String content = scanner.nextLine();
                request = String.format("%s %s %s ", httpMethod, filename, content);
            }
            case "3" -> {
                httpMethod = "DELETE";
                request = String.format("%s %s ", httpMethod, filename);
            }
            default -> System.out.println("N/A");
        }

        return request;
    }

    public void responseHandling(String response, String request){
        String httpRequest = request.substring(0, request.indexOf(' '));
        String message = "";
        if (httpRequest.equals("GET")) {
            message = response.equals("404") ? "The response says that the file was not found!" :
                    String.format("The content of file is: %s", response);
        } else if (httpRequest.equals("PUT")) {
            message = response.equals("403") ? "The response says that creating the file was forbidden!" :
                    "The response says that the file was created!";
        } else if (httpRequest.equals("DELETE")) {
            message = response.equals("404") ? "The response says that the file was not found!" :
                    "The response says that the file was successfully deleted!";
        } else {
            message = "ERROR";
        }

        System.out.println(message);
    }


}

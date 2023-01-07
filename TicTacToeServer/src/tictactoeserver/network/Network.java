package tictactoeserver.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Network extends Thread {

    ServerSocket serverSocket;
    ConnectionHandler connectionHandler;

    public Network() {
        try {
            serverSocket = new ServerSocket(5005);
            start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            stop();
            if (ConnectionHandler.clientsVector.size() != 0) {
                connectionHandler.closeConnection();

            }
            serverSocket.close();
//            socket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            Socket socket;

            try {
                socket = serverSocket.accept();
                connectionHandler = new ConnectionHandler(socket);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

package tictactoeserver.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Network extends Thread {

    ServerSocket serverSocket;
    ConnectionHandler connectionHandler;
    boolean hasSocket = false;

    public Network() {
        try {
            serverSocket = new ServerSocket(5005);
            start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() throws IOException {
        stop();
        if (hasSocket) {
            connectionHandler.closeConnection();

        }
        serverSocket.close();
    }

    @Override
    public void run() {
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                hasSocket = true;
                connectionHandler = new ConnectionHandler(socket);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

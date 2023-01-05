package tictactoeserver.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.repository.PlayerRepository;

class ConnectionHandler implements Runnable {

    Thread thread;
    PrintStream printStream;
    DataInputStream dataInputStream;
    static Vector<ConnectionHandler> clientsVector = new Vector<ConnectionHandler>();
    static Vector<ConnectionHandler> unavailableClients = new Vector<ConnectionHandler>();
    String messageSentToClient;
    PlayerRepository playerRepository;
    boolean flag = true, noInput = true;
    String message;

    public ConnectionHandler(Socket socket) {
        System.out.println(socket);
        playerRepository = new PlayerRepository();

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
            ConnectionHandler.clientsVector.add(this);
            System.out.println("connection build ");
            thread = new Thread(this);
            thread.start();
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }

    }

    @Override
    public void run() {
        while (true && noInput) {

            try {
                String messageReceivedFromClient = dataInputStream.readLine();
                if (messageReceivedFromClient != null) {
                    System.out.println(messageReceivedFromClient);
                    if (messageReceivedFromClient.length() > 0) {
                        if (messageReceivedFromClient.equals("close")) {
//                            for (ConnectionHandler client : clientsVector) {
//                                if (!client.flag) {
//                                    unavailableClients.add(client);
//
//                                }
                            flag = false;
                            System.out.println("client closed ddddddddddddddddddddddddd");
//                            }

                            for (ConnectionHandler client : unavailableClients) {
                                clientsVector.remove(client);
                                if (clientsVector.isEmpty()) {
                                    noInput = false;
                                }
                            }

                        } else {
                            String[] arr = messageReceivedFromClient.split(",");
                            System.out.println(arr);
                            String username = arr[1];
                            String password = arr[2];
                            System.out.println("username: " + username + " password:" + password);

                            if (playerRepository.login(username, password)) {
                                messageSentToClient = "Login,Successfully";
                                System.out.println("successed");

                            } else {

                                messageSentToClient = "Login,failed";
                                System.out.println("failed");
                            }
                            printStream.println(messageSentToClient);
                        }
                    } else {
                        try {

                            printStream.close();
                            dataInputStream.close();

                        } catch (IOException ex1) {
                            ex1.printStackTrace();
                        }

                    }
                    for (ConnectionHandler client : clientsVector) {
                        if (!client.flag) {
                            unavailableClients.add(client);
                        }

                    }
                    for (ConnectionHandler client : unavailableClients) {
                        clientsVector.remove(client);
                        if (clientsVector.size() == 0) {
                            noInput = false;
                        }

                    }
                    messageSentToClient = null;

                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void closeConnection() {

        for (ConnectionHandler client : clientsVector) {

            try {
                thread.stop();
                printStream.close();
                dataInputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }

        }

    }
}

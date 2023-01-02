package tictactoeserver.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.repository.PlayerRepository;

class ConnectionHandler implements Runnable {

    ObjectInputStream objectInputStream;
    InputStream inputStream;
    Thread thread;
    OutputStream outputStream;
    ObjectOutputStream objectOutputStream;

    static Vector<ConnectionHandler> clientsVector = new Vector<ConnectionHandler>();
    static Vector<ConnectionHandler> unavailableClients = new Vector<ConnectionHandler>();

    List<String> messages;
    PlayerRepository playerRepository;
    boolean flag = true, noInput = true;
    List<String> recievedMessages;

    public ConnectionHandler(Socket socket) {
        System.out.println(socket);
        messages = new ArrayList<>();
        playerRepository = new PlayerRepository();

        try {
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            ConnectionHandler.clientsVector.add(this);
            System.out.println("adddd:" + messages + "+" + clientsVector.size());
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
               System.out.println(messages);
                    while (true) {
                        recievedMessages = (List<String>) objectInputStream.readObject();
                        if (recievedMessages.size() > 0) {
                            break;
                        }
                    }
                System.out.println("receive");
                System.out.println(recievedMessages);
                if (recievedMessages.size() > 0 && recievedMessages.get(0).equals("Login")) {
                    String username = recievedMessages.get(1);
                    String password = recievedMessages.get(2);
                   
                    if (playerRepository.login(username, password)) {
                        messages.add("Login");
                        messages.add("Login Successfully");
                        System.out.println("successed");
//                        objectOutputStream.writeObject(messages);
                    } else {
                        messages.add("Login");
                        messages.add("Login failed");
                        System.out.println("failed");
//                        objectOutputStream.writeObject(messages);
                    }
                    objectOutputStream.writeObject(messages);
                    messages.clear();
                    
                    recievedMessages.clear();
//                    messages.add("finish");
//                    messages.add("finish");
//                    objectOutputStream.writeObject(messages);
//                    objectOutputStream.writeObject(messages);

                } else if (recievedMessages.size() > 1 && recievedMessages.get(0).equals("close")) {
                    flag = false;
                }else{
                    System.out.println("no  ");
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
//            for (ConnectionHandler client : clientsVector) {
//                if (!client.flag) {
//                    unavailableClients.add(client);
//                }
//
//            }
//            for (ConnectionHandler client : unavailableClients) {
//                clientsVector.remove(client);
//                if (clientsVector.size() == 0) {
//                    noInput = false;
//                }
//
//            }
        }

    }

    public void closeConnection() throws IOException {

        for (ConnectionHandler client : clientsVector) {

            objectOutputStream.writeObject(new Object());
            try {
                thread.stop();
                inputStream.close();
                outputStream.close();
                objectInputStream.close();
                objectOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }

        }

    }
}

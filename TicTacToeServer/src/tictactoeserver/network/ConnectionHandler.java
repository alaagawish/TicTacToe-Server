package tictactoeserver.network;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.models.Message;
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

                Gson gson = new Gson();
                String messageReceivedFromClient = dataInputStream.readLine();
                messageReceivedFromClient = messageReceivedFromClient.replaceAll("\r?\n", "");
                if (messageReceivedFromClient != null) {

                    if (messageReceivedFromClient.length() > 0) {
                        Message messageReceived = new Gson().fromJson(messageReceivedFromClient, Message.class);

                        if (messageReceivedFromClient.equals("close")) {
                            flag = false;

                            for (ConnectionHandler client : unavailableClients) {
                                if (!flag) {
                                    clientsVector.remove(client);
                                }
                                if (clientsVector.isEmpty()) {
                                    noInput = false;
                                }
                            }

                        } else if (messageReceived.getOperation().equals("Login")) {
                            String username = messageReceived.getPlayers().get(0).getUsername();
                            String password = messageReceived.getPlayers().get(0).getPassword();
                            System.out.println("username: " + username + " password:" + password);

                            if (playerRepository.login(username, password)) {

                                Message messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus(true);

                                String messageSentToClient = gson.toJson(messageSent);

                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("successed");

                            } else {

                                Message messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus(false);

                                String messageSentToClient = gson.toJson(messageSent);

                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("failed");
                            }

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
//                    messageSentToClient = null;

                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void closeConnection() {

        for (ConnectionHandler client : clientsVector) {
            printStream.println("close");
            try {
                thread.sleep(100);
                thread.stop();
                printStream.close();
                dataInputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

package tictactoeserver.network;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.models.Message;
import tictactoeserver.models.Player;
import tictactoeserver.repository.PlayerRepository;

class ConnectionHandler implements Runnable {

    Thread thread;
    PrintStream printStream;
    DataInputStream dataInputStream;
    static Vector<ConnectionHandler> clientsVector = new Vector<ConnectionHandler>();
    static Vector<ConnectionHandler> unavailableClients = new Vector<ConnectionHandler>();
    String messageSentToClient, messageReceivedFromClient;
    public static PlayerRepository playerRepository;
    boolean flag = true, noInput = true;
    String message, password, username;
    int port;
    Gson gson;
    Message messageSent, messageReceived;

    public ConnectionHandler(Socket socket) {
        System.out.println(socket);
        playerRepository = new PlayerRepository();
        port = socket.getPort();
        System.out.println("port number: " + port);
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
            ConnectionHandler.clientsVector.add(this);
            System.out.println("connection build ");
            thread = new Thread(this);
            gson = new Gson();
            thread.start();
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }

    }

    @Override
    public void run() {
        while (true && noInput) {

            try {
                messageReceivedFromClient = dataInputStream.readLine();
                messageReceivedFromClient = messageReceivedFromClient.replaceAll("\r?\n", "");
                if (messageReceivedFromClient != null) {
                    if (messageReceivedFromClient.length() > 0) {
                        System.out.println("messsss" + messageReceivedFromClient);
                        messageReceived = new Gson().fromJson(messageReceivedFromClient, Message.class);

                        if (messageReceived.getOperation().equalsIgnoreCase("close")) {
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
                            username = messageReceived.getPlayers().get(0).getUsername();
                            password = messageReceived.getPlayers().get(0).getPassword();
                            System.out.println("username: " + username + " password:" + password);
                            Player player = this.playerRepository.login(username, password);
                            if (player != null) {

                                Message messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus("done");

                                String playerToString = gson.toJson(player);
                                System.out.println("playerToString" + playerToString);
                                messageSent.setPlayers(player);
                                messageSentToClient = gson.toJson(messageSent);
                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("successed");

                            } else {

                                messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus("wrong");

                                messageSentToClient = gson.toJson(messageSent);

                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("failed");
                            }

                        } else if (messageReceived.getOperation().equals("Edit")) {
                            username = messageReceived.getPlayers().get(0).getUsername();
                            password = messageReceived.getPlayers().get(0).getPassword();
                            System.out.println("username: " + username + " password:" + password);

                            if (playerRepository.editPassword(username, password) != null) {

                                Message messageSent = new Message();
                                messageSent.setOperation("Edit");
                                messageSent.setStatus("done");

                                Player player = playerRepository.login(username, password);

                                String playerToString = gson.toJson(player);
                                messageSent.setPlayers(player);
                                messageSentToClient = gson.toJson(messageSent);
                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("successed");

                            } else {

                                messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus("wrong");

                                messageSentToClient = gson.toJson(messageSent);

                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("failed");
                            }

                        } else if (messageReceived.getOperation().equals("register")) {
                            username = messageReceived.getPlayers().get(0).getUsername();
                            password = messageReceived.getPlayers().get(0).getPassword();
                            System.out.println("username: " + username + " password:" + password);
                            Player player = this.playerRepository.registerPlayer(username, password);
                            if (player != null) {

                                Message messageSent = new Message();
                                messageSent.setOperation("register");
                                messageSent.setStatus("done");

                                String playerToString = gson.toJson(player);
                                System.out.println("playerToString" + playerToString);
                                messageSent.setPlayers(player);
                                messageSentToClient = gson.toJson(messageSent);
                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("successed");

                            } else {

                                messageSent = new Message();
                                messageSent.setOperation("register");
                                messageSent.setStatus("wrong");

                                messageSentToClient = gson.toJson(messageSent);

                                System.out.println("msg json is " + messageSentToClient);
                                printStream.println(messageSentToClient);
                                System.out.println("failed");
                            }

                        }else if (messageReceived.getOperation().equals("getOnlineList")) {
                            
                            printStream.println(getOnlinePlayersMessage());
                            
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
                    messageReceivedFromClient = null;

                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void closeConnection() {

        for (ConnectionHandler client : clientsVector) {
            gson = new Gson();
            messageSent = new Message();
            messageSent.setOperation("close");

            messageSentToClient = gson.toJson(messageSent);

            printStream.println(messageSentToClient);
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
    
    public String getOnlinePlayersMessage() {

        List<Player> onlinePlayers = playerRepository.getOfflinePlayers();
                            
        if (onlinePlayers!= null) {

            messageSent = new Message();
            messageSent.setOperation("getOnlineList");
            messageSent.setStatus("done");
            messageSent.setPlayers(onlinePlayers);

            System.out.println("successed");

        } else {

            messageSent = new Message();
            messageSent.setOperation("getOnlineList");
            messageSent.setStatus("wrong");

            System.out.println("failed");
        }

        messageSentToClient = gson.toJson(messageSent);
        System.out.println("msg json is " + messageSentToClient);
        return messageSentToClient;
    }
    
}

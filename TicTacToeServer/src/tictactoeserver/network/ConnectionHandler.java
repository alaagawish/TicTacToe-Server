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
    int id;
    static Vector<ConnectionHandler> clientsVector = new Vector<ConnectionHandler>();
    static Vector<ConnectionHandler> unavailableClients = new Vector<ConnectionHandler>();
    String messageSentToClient, messageReceivedFromClient;
    public static PlayerRepository playerRepository;
    boolean flag = true, noInput = true;
    String message, password, username;
    int port;
    Gson gson;
    Message messageSent, messageReceived, messageSentToSecondPlayer, messageSentToFirstPlayer;
    Player playerAsk, playerReceive;

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
                System.out.println("messageReceivedFromClient::" + messageReceivedFromClient);

                if (messageReceivedFromClient != null) {
                    messageReceivedFromClient = messageReceivedFromClient.replaceAll("\r?\n", "");

                    if (messageReceivedFromClient.length() > 0) {
                        messageReceived = new Gson().fromJson(messageReceivedFromClient, Message.class);

                        if (messageReceived.getOperation().equalsIgnoreCase("close")) {
                            flag = false;

                            for (ConnectionHandler client : unavailableClients) {
                                if (!flag) {
                                    client.thread.stop();
                                    client.printStream.close();
                                    client.dataInputStream.close();
                                    clientsVector.remove(client);
                                }
                                if (clientsVector.isEmpty()) {
                                    noInput = false;
                                }
                            }

                        } else if (messageReceived.getOperation().equals("Login")) {
                            username = messageReceived.getPlayers().get(0).getUsername();
                            password = messageReceived.getPlayers().get(0).getPassword();
                            Player player = this.playerRepository.login(username, password);
                            if (player != null) {

                                Message messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus("done");

                                String playerToString = gson.toJson(player);
                                messageSent.setPlayers(player);
                                id = player.getId();
                                System.out.println("iddd" + id);
                                messageSentToClient = gson.toJson(messageSent);
                                printStream.println(messageSentToClient);

                            } else {

                                messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus("wrong");

                                messageSentToClient = gson.toJson(messageSent);
                                printStream.println(messageSentToClient);
                            }

                        } else if (messageReceived.getOperation().equals("Edit")) {
                            username = messageReceived.getPlayers().get(0).getUsername();
                            password = messageReceived.getPlayers().get(0).getPassword();

                            if (playerRepository.editPassword(username, password) != null) {

                                Message messageSent = new Message();
                                messageSent.setOperation("Edit");
                                messageSent.setStatus("done");

                                Player player = playerRepository.login(username, password);

                                String playerToString = gson.toJson(player);
                                messageSent.setPlayers(player);
                                messageSentToClient = gson.toJson(messageSent);
                                printStream.println(messageSentToClient);

                            } else {

                                messageSent = new Message();
                                messageSent.setOperation("Login");
                                messageSent.setStatus("wrong");
                                messageSentToClient = gson.toJson(messageSent);

                                printStream.println(messageSentToClient);
                            }

                        } else if (messageReceived.getOperation().equals("requestGame")) {
                            playerAsk = messageReceived.getPlayers().get(0);
                            playerReceive = messageReceived.getPlayers().get(1);
                            messageSentToSecondPlayer = new Message();
                            System.out.println("requestGame,server");
                            messageSentToSecondPlayer.setOperation("askToPlay");
                            System.out.println("asktoplay,server::" + messageSentToSecondPlayer);
                            sendInvitation(playerReceive.getId());

                        } else if (messageReceived.getOperation().equals("register")) {
                            username = messageReceived.getPlayers().get(0).getUsername();
                            password = messageReceived.getPlayers().get(0).getPassword();
                            System.out.println("username: " + username + " password:" + password);
                            Player player = this.playerRepository.registerPlayer(username, password);
                            if (player != null) {

                                messageSent = new Message();
                                messageSent.setOperation("register");
                                messageSent.setStatus("done");
                                id = player.getId();

                                String playerToString = gson.toJson(player);
                                messageSent.setPlayers(player);
                                messageSentToClient = gson.toJson(messageSent);
                                printStream.println(messageSentToClient);

                            } else {

                                messageSent = new Message();
                                messageSent.setOperation("register");
                                messageSent.setStatus("wrong");
                                messageSentToClient = gson.toJson(messageSent);
                                printStream.println(messageSentToClient);
                            }

                        } else if (messageReceived.getOperation().equals("responseGame")) {
                            messageSentToFirstPlayer = new Message();
                            System.out.println("responsegame");
                            playerAsk = messageReceived.getPlayers().get(0);
                            if (messageReceived.getStatus().equalsIgnoreCase("accept")) {

                                playerReceive = messageReceived.getPlayers().get(1);
                                messageSentToFirstPlayer.setStatus("accept");
                                System.out.println("responsegameserver:::accept");
                            } else if (messageReceived.getStatus().equalsIgnoreCase("reject")) {
                                messageSentToFirstPlayer.setStatus("reject");
                                System.out.println("responsegameserver:::reject");

                            }
                            sendInvitationResult(playerAsk.getId());
                        } else if (messageReceived.getOperation().equals("getOnlineList")) {

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
                        client.thread.stop();
                        client.printStream.close();
                        client.dataInputStream.close();
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
        thread.stop();
        for (ConnectionHandler client : clientsVector) {
            gson = new Gson();
            messageSent = new Message();
            messageSent.setOperation("close");
            messageSentToClient = gson.toJson(messageSent);
            printStream.println(messageSentToClient);

            try {
                thread.sleep(100);
                client.thread.stop();
                client.printStream.close();
                client.dataInputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {

            printStream.close();
            dataInputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();

        }

    }

    public String getOnlinePlayersMessage() {

        List<Player> onlinePlayers = this.playerRepository.getOfflinePlayers();

        if (onlinePlayers != null) {

            messageSent = new Message();
            messageSent.setOperation("getOnlineList");
            messageSent.setStatus("done");
            messageSent.setPlayers(onlinePlayers);
            System.out.println("successed");

        } else {

            messageSent = new Message();
            messageSent.setOperation("getOnlineList");
            messageSent.setStatus("wrong");
        }

        messageSentToClient = gson.toJson(messageSent);
        return messageSentToClient;
    }

    public void sendInvitation(int id) {
        for (ConnectionHandler client : clientsVector) {
            if (client.id == id) {
                messageSentToSecondPlayer.setStatus("nothing");
                messageSentToSecondPlayer.setPlayers(playerAsk);
                messageSentToSecondPlayer.setPlayers(playerReceive);
                System.out.println("sendInvitation" + playerAsk);
                messageSentToClient = gson.toJson(messageSentToSecondPlayer);
                client.printStream.println(messageSentToClient);
            }

        }
    }

    public void sendInvitationResult(int id) {
        for (ConnectionHandler client : clientsVector) {
            if (client.id == id) {
                messageSentToFirstPlayer.setPlayers(playerAsk);
                messageSentToFirstPlayer.setPlayers(playerReceive);

                messageSentToFirstPlayer.setOperation("requestGamefeedback");
                System.out.println("responseGame,server:::" + messageSentToFirstPlayer.getStatus());
                messageSentToClient = gson.toJson(messageSentToFirstPlayer);
                client.printStream.println(messageSentToClient);

            }

        }
    }

}

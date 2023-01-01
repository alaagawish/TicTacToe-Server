/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author moazk
 */

class ConnectionHandler extends Thread
{
    
    ObjectInputStream ois;
    InputStream inputStream;
    
    OutputStream outputStream ;
    ObjectOutputStream objectOutputStream;
    
    static Vector<ConnectionHandler> clientsVector = new Vector<ConnectionHandler>();
    
    List<String> messages;

    public ConnectionHandler(Socket socket){
        messages = new ArrayList<>();
        messages.add("Login Successfully");
        
        try {
            inputStream = socket.getInputStream();
            ois = new ObjectInputStream(inputStream);
            
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            
            ConnectionHandler.clientsVector.add(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        while(true){
            try {
                List<String> listOfMessages = (List<String>) ois.readObject();
                
                if(listOfMessages.size() > 0){
                    System.out.println("Received [" + listOfMessages.size() + "]");
                    System.out.println("All messages:");
                    listOfMessages.forEach((msg)-> System.out.println(msg));
                }
                
                listOfMessages.clear();
                
                objectOutputStream.writeObject(messages);
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    

}

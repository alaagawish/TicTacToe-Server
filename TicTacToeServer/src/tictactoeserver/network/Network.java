/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author moazk
 */
public class Network {
    ServerSocket serverSocket;
    
    public Network() throws IOException{
        serverSocket = new ServerSocket(5005);
        
        while(true)
        {
            System.err.println("Hello Server");
            Socket s = serverSocket.accept();
            new ConnectionHandler(s);
        }
    }
    
}

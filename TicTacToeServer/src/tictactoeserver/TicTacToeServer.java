/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;


import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import tictactoeserver.repository.PlayerRepository;
import tictactoeserver.screens.splash.SplashBase;

/**
 *
 * @author LAPTOP
 */
public class TicTacToeServer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        PlayerRepository playerRepository = new PlayerRepository();
        playerRepository.selectAllPlayer();
       
        Parent root = new SplashBase(stage);
  
        Scene scene = new Scene(root);
      
        stage.setScene(scene);
        stage.show();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        launch(args);
        //new Network();
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import tictactoeserver.repository.PlayerRepository;

/**
 *
 * @author Arwa
 */
public class Main {
    
    public static void main(String[] args) {
        int numOn = new PlayerRepository().selectOffline();
        int numOff = new PlayerRepository().selectOffline();
        PlayerRepository playerRepository = new PlayerRepository();
        
        System.out.println(playerRepository.logout("Arwa"));
//        System.out.println(numOn);
    }
    
}

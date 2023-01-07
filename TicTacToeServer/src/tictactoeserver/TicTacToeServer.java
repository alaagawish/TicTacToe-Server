package tictactoeserver;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoeserver.network.Network;
import tictactoeserver.repository.PlayerRepository;
import tictactoeserver.screens.home.HomeScreenBase;
import tictactoeserver.screens.splash.SplashBase;

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

    @Override
    public void stop() {
        HomeScreenBase.network.close();
    }

    public static void main(String[] args) throws IOException {
        launch(args);

    }
}

package org.example.server;
import org.springframework.context.annotation.ComponentScan;
import java.io.IOException;
import java.util.Scanner;

@ComponentScan
public class StartServer3 {

    public static void main(String[] args) throws IOException {

        int n = 0;

        while (n != 3) {
            System.out.println("Press 1 to register this server with Registry Server : ");
            System.out.println("Press 2 to Become Client of Another Server : ");
            System.out.println("Press 3 to Exit : ");
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();

            switch (n) {
                case 1:
                    Server.registerServerWithRegistryServer("localhost", 9090);
                    break;
                case 2:
                    Server.becomeClient();
                    break;
                case 3 :
                    System.exit(0);
                    break;
            }
        }
    }
}

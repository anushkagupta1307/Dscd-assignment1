package org.example.client;

import java.util.Scanner;

public class StartClient1 {
    public static void main(String[] args) {

        int n=0;

        while(n!=6) {
            System.out.println("Welcome to Distributed Systems PubSub : ");
            System.out.println("Press 1 to Get the List of Servers ");
            System.out.println("Press 2 to Join a Server ");
            System.out.println("Press 3 to Leave a Server : ");
            System.out.println("Press 4 to Get Articles from Server : ");
            System.out.println("Press 5 to Publish an Article to a Server : ");
            System.out.println("Press 6 to Exit ");
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();

            switch (n) {
                case 1:
                    Client.getServerList();
                    break;
                case 2:
                    Client.joinServer(1);
                    break;
                case 3:
                    Client.leaveServer(1);
                    break;
                case 4 :
                   Client.getArticles(1);
                    break;
                case 5 :
                   Client.publishArticle(1);
                    break;
                case 6 :
                    System.exit(0);
                    break;
            }
        }



    }
}

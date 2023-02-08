package org.example.server;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import org.example.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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
                    System.out.println("Register With The Registry Server : ");
                    String host = "localhost";
                    int port = 9090;
                    io.grpc.Server server = ServerBuilder
                            .forPort(port)
                            .addService(new Server()).build();
                    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
                    ServerRegistrationServiceGrpc.ServerRegistrationServiceBlockingStub blockingStub = ServerRegistrationServiceGrpc.newBlockingStub(managedChannel);
                    ServerRegistrationResponse response = blockingStub.registerServer(ServerRegistrationRequest.newBuilder().setHost(host).setPort(port).build());
                    System.out.println("Server Registered :  = " + response.getHost() + " " + response.getPort() + " " + response.getRegistrationResponse());
                    server.start();
                    break;
                case 2:
                    System.out.println("Enter host of server which you want to connect to : ");
                    String host2 = sc.next();
                    System.out.println("Enter Port of Server which you want to connect : ");
                    int port2 = sc.nextInt();
                    ManagedChannel managedChannel1 = ManagedChannelBuilder.forAddress(host2, port2).usePlaintext().build();
                    ArticleServiceGrpc.ArticleServiceBlockingStub articleServiceBlockingStub = ArticleServiceGrpc.newBlockingStub(managedChannel1);
                    ArticleResponse articleResponse = articleServiceBlockingStub.serverBecomesClient(Empty.newBuilder().build());
                    List<Article> articlesRetrieved = articleResponse.getArticlesList();
                    System.out.println("Articles Retrieved : " + articlesRetrieved.size());
                    System.out.println("All Articles on Server 3 before adding " + Server.allArticles.size());
                    Server.allArticles.addAll(articlesRetrieved);
                    System.out.println("All Articles on Server 3 after adding " + Server.allArticles.size());
                    break;
                case 3 :
                    System.exit(0);
                    break;
            }
        }
    }
}

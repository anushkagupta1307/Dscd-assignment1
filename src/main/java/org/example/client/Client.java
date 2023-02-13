package org.example.client;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void getServerList(){
        System.out.println("Servers List Registered with Registry Server : ");
        ManagedChannel managedChannel4= ManagedChannelBuilder.forAddress("localhost",6565).usePlaintext().build();
        ServerRegistrationServiceGrpc.ServerRegistrationServiceBlockingStub serverRegistrationServiceBlockingStub=ServerRegistrationServiceGrpc.newBlockingStub(managedChannel4);
        ListOfServersResponse response=serverRegistrationServiceBlockingStub.getServerList(Empty.newBuilder().build());
        List<ServerRegistrationRequest> registeredServers=response.getServersListList();
        for(int i=0;i<registeredServers.size();i++){
            System.out.print("Server " +(i+1)+" : ");
            System.out.print(registeredServers.get(i).getHost()+" ");
            System.out.print(registeredServers.get(i).getPort());
            System.out.println();
        }
    }

    public static void joinServer(int client_id){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter host of Server to Join : ");
        String host_join = sc.next();
        System.out.println("Enter port of Server to Join : ");
        int port_join = sc.nextInt();
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host_join, port_join).usePlaintext().build();
        ArticleServiceGrpc.ArticleServiceBlockingStub blockingStub = ArticleServiceGrpc.newBlockingStub(managedChannel);
        ClientRegistrationResponse clientRegistrationResponse = blockingStub.joinServer(ClientRegistrationRequest.newBuilder().setClientId(client_id).build());
        System.out.println(clientRegistrationResponse.getClientId() + " " + clientRegistrationResponse.getRegistrationResponse());
    }

    public static void leaveServer(int client_id){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter host of Server to Leave : ");
        String host_leave = sc.next();
        System.out.println("Enter port of Server to Leave : ");
        int port_leave = sc.nextInt();
        ManagedChannel managedChannel1 = ManagedChannelBuilder.forAddress(host_leave, port_leave).usePlaintext().build();
        ArticleServiceGrpc.ArticleServiceBlockingStub blockingStub1 = ArticleServiceGrpc.newBlockingStub(managedChannel1);
        ClientRegistrationResponse clientRegistrationResponse_leave = blockingStub1.leaveServer(ClientRegistrationRequest.newBuilder().setClientId(client_id).build());
        System.out.println(clientRegistrationResponse_leave.getClientId() + " " + clientRegistrationResponse_leave.getRegistrationResponse());
    }

    public static void getArticles(int client_id){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Host of Server to Get Articles : ");
        String host_articles=sc.next();
        System.out.println("Enter port of Server to Get Articles : ");
        int port_articles = sc.nextInt();
        System.out.println("Enter the TYPE of Articles : (Enter NA if you do not wish to specify)" );
        String type= sc.next();
        System.out.println("Enter the DATE after which you want articles (Format : YYYY-MM-DD) : (Enter NA if you do not wish to specify)");
        String date= sc.next();
        System.out.println("Enter Name of Author : (Enter NA if you do not wish to specify)");
        String author=sc.next();
        ManagedChannel managedChannel2= ManagedChannelBuilder.forAddress(host_articles,port_articles).usePlaintext().build();
        ArticleServiceGrpc.ArticleServiceBlockingStub blockingStub2= ArticleServiceGrpc.newBlockingStub(managedChannel2);
        ArticleResponse articleResponse=  blockingStub2.getArticles(ArticleRequest.newBuilder().setClientId(client_id).setType(type).setDate(date).setAuthor(author).build());
        List<Article> articleList=articleResponse.getArticlesList();
        if(articleList.size()==0){
            System.out.println("This Client is Not Subscribed to the Server from which Articles were requested ");
        }else {
            System.out.println("ARTICLES - ");
            for (int i = 0; i < articleList.size(); i++) {
                System.out.println("Type : "+articleList.get(i).getType());
                System.out.println("Publishing Date : "+articleList.get(i).getDate());
                System.out.println("Author : "+articleList.get(i).getAuthor());
                System.out.println("Article : "+articleList.get(i).getArticle());
                System.out.println();
            }
        }
    }

    public static void publishArticle(int client_id){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Host of Server to Which you want to Publish Article : ");
        String host_publish=sc.next();
        System.out.println("Enter port of Server to which you want to Publish Article : ");
        int port_publish=sc.nextInt();
        System.out.println("Enter Type of Article : ");
        String type_article=sc.next();
        System.out.println("Enter Author name : ");
        String author_article=sc.next();
        sc.nextLine();
        System.out.println("Enter Article : ");
        String article=sc.nextLine();
        String date_article= LocalDate.now().toString();
        if(!type_article.equals("NA") && !type_article.equals("") && !type_article.equals(null)) {
            Article article1 = Article.newBuilder().setType(type_article).setDate(date_article).setAuthor(author_article).setArticle(article).build();
            ManagedChannel managedChannel3 = ManagedChannelBuilder.forAddress(host_publish, port_publish).usePlaintext().build();
            ArticleServiceGrpc.ArticleServiceBlockingStub blockingStub3 = ArticleServiceGrpc.newBlockingStub(managedChannel3);
            PublishArticleResponse publishArticleResponse = blockingStub3.publishArticle(PublishArticleRequest.newBuilder().setClientId(client_id).setArticle(article1).build());
            System.out.println(publishArticleResponse.getPublishArticleResponseCase());
        }
        else{
            System.out.println("Illegal Request format. Please specify Article type");
        }
    }

}

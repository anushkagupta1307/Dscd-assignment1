package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.SportsArticleRequest;
import org.example.SportsArticleResponse;
import org.example.SportsArticleServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client1 {

    static Logger logger= LoggerFactory.getLogger(Client1.class);
    public static void main(String[] args) {

        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",6565).usePlaintext().build();

        SportsArticleServiceGrpc.SportsArticleServiceBlockingStub blockingStub= SportsArticleServiceGrpc.newBlockingStub(managedChannel);
        SportsArticleResponse sportsArticleResponse=  blockingStub.getSportsArticle(SportsArticleRequest.newBuilder().setId(1).build());
        logger.info("response = "+sportsArticleResponse.getType()+" "+sportsArticleResponse.getAuthor()+" "+sportsArticleResponse.getTime()+" "+sportsArticleResponse.getArticle());
    }
}

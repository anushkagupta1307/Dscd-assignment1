package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client2 {

    static Logger logger= LoggerFactory.getLogger(Client1.class);
    public static void main(String[] args) {

        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",6565).usePlaintext().build();

        FashionArticleServiceGrpc.FashionArticleServiceBlockingStub blockingStub= FashionArticleServiceGrpc.newBlockingStub(managedChannel);
        FashionArticleResponse fashionArticleResponse=  blockingStub.getFashionArticle(FashionArticleRequest.newBuilder().setId(1).build());
        logger.info("response = "+fashionArticleResponse.getType()+" "+fashionArticleResponse.getAuthor()+" "+fashionArticleResponse.getTime()+" "+fashionArticleResponse.getArticle());

    }
}

package org.example.server;

import io.grpc.stub.StreamObserver;
import org.example.*;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@GRpcService
public class Server2 extends FashionArticleServiceGrpc.FashionArticleServiceImplBase {

    Logger logger= LoggerFactory.getLogger(Server1.class);
    @Override
    public void getFashionArticle(FashionArticleRequest request, StreamObserver<FashionArticleResponse> responseObserver) {

        logger.info("Got request = "+request.getId());
        FashionArticleResponse articleResponse = FashionArticleResponse.newBuilder().setType("FASHION").setAuthor("Anushka")
                .setTime(LocalDateTime.now().toString()).setArticle("Vogue Fashion Week is Here!").build();

        responseObserver.onNext(articleResponse);
        responseObserver.onCompleted();


    }
}

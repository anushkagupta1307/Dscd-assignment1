package org.example.server;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import org.example.*;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@GRpcService
public class Server1 extends SportsArticleServiceGrpc.SportsArticleServiceImplBase {
    static Logger logger= LoggerFactory.getLogger(Server1.class);

    public static void main(String[] args) {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",6565).usePlaintext().build();

        ServerRegistrationServiceGrpc.ServerRegistrationServiceBlockingStub blockingStub= ServerRegistrationServiceGrpc.newBlockingStub(managedChannel);
        ServerRegistrationResponse response=  blockingStub.registerServer(ServerRegistrationRequest.newBuilder().setServerId(1).build());
        logger.info("Server Registered : Server ID = "+response.getServerId()+" Response = "+response.getRegistrationResponse());
    }

    @Override
    public void getSportsArticle(SportsArticleRequest request, StreamObserver<SportsArticleResponse> responseObserver) {

        logger.info("Got request = "+request.getId());
        SportsArticleResponse articleResponse = SportsArticleResponse.newBuilder().setType("SPORTS").setAuthor("Anushka")
                .setTime(LocalDateTime.now().toString()).setArticle("India vs NZ Cricket Score").build();

        responseObserver.onNext(articleResponse);
        responseObserver.onCompleted();


    }
}

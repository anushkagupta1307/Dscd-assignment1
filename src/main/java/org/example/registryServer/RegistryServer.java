package org.example.registryServer;
import org.example.*;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@GRpcService
public class RegistryServer extends ServerRegistrationServiceGrpc.ServerRegistrationServiceImplBase {
    Logger logger= LoggerFactory.getLogger(RegistryServer.class);
    static int MAX_SERVERS = 5;

    public int registered_servers;
@Override
public void registerServer(org.example.ServerRegistrationRequest request,
                           io.grpc.stub.StreamObserver<org.example.ServerRegistrationResponse> responseObserver) {

    logger.info("Got Registration Request For Server ID : "+request.getServerId());

    ServerRegistrationResponse response;
    if(registered_servers<=MAX_SERVERS) {
        registered_servers++;
         response = ServerRegistrationResponse.newBuilder().setServerId(request.getServerId()).setRegistrationResponse("SUCCESS").build();
    }
    else{
         response = ServerRegistrationResponse.newBuilder().setServerId(request.getServerId()).setRegistrationResponse("FAILED").build();
    }
    responseObserver.onNext(response);
    responseObserver.onCompleted();

}
}
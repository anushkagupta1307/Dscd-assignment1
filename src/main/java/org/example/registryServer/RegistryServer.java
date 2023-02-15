package org.example.registryServer;
import org.example.*;
import org.lognet.springboot.grpc.GRpcService;
import java.util.ArrayList;
import java.util.List;


@GRpcService
public class RegistryServer extends ServerRegistrationServiceGrpc.ServerRegistrationServiceImplBase {
    static int MAX_SERVERS = 2;
    public static List<ServerRegistrationRequest> serversList=new ArrayList<>();

@Override
public void registerServer(ServerRegistrationRequest request,
                           io.grpc.stub.StreamObserver<org.example.ServerRegistrationResponse> responseObserver) {


    ServerRegistrationResponse response;
    if(serversList.size()<MAX_SERVERS) {
        serversList.add(request);
         response = ServerRegistrationResponse.newBuilder().setHost(request.getHost()).setPort(request.getPort()).setRegistrationResponse("SUCCESS").build();
    }
    else{
         response = ServerRegistrationResponse.newBuilder().setHost(request.getHost()).setPort(request.getPort()).setRegistrationResponse("FAILED").build();
    }
    responseObserver.onNext(response);
    responseObserver.onCompleted();
}
@Override
public void getServerList(com.google.protobuf.Empty request,
                          io.grpc.stub.StreamObserver<org.example.ListOfServersResponse> responseObserver) {

    ListOfServersResponse servers= ListOfServersResponse.newBuilder().addAllServersList(serversList).build();
    responseObserver.onNext(servers);
    responseObserver.onCompleted();
    }
}
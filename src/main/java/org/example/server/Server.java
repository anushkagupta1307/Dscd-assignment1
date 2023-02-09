package org.example.server;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.*;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@GRpcService
@Component
public class Server extends ArticleServiceGrpc.ArticleServiceImplBase {
    static Logger logger= LoggerFactory.getLogger(Server.class);
    int MAX_CLIENTS=2;
    public static List<Article> allArticles= new ArrayList<>();
    {
        Article article1 = Article.newBuilder().setType("SPORTS").setAuthor("Anushka").setDate(LocalDate.now().toString()).setArticle("Watch India vs NZ Cricket Score!").build();
        Article article2 = Article.newBuilder().setType("FASHION").setAuthor("Shaguftha").setDate(LocalDate.now().toString()).setArticle("Vogue Fashion week is live!").build();
        Article article3 = Article.newBuilder().setType("POLITICS").setAuthor("Anushka").setDate(LocalDate.now().toString()).setArticle("BJP rally in Delhi!").build();
        Article article4 = Article.newBuilder().setType("SPORTS").setAuthor("Harsh").setDate(LocalDate.now().toString()).setArticle("Badminton Tournament from tomorrow!").build();
        Article article5 = Article.newBuilder().setType("FASHION").setAuthor("Shaguftha").setDate(LocalDate.now().toString()).setArticle("London Fashion Week begins!").build();
        Article article6 = Article.newBuilder().setType("SPORTS").setAuthor("Anushka").setDate(LocalDate.now().minusDays(2).toString()).setArticle("India vs Australia Cricket Match Cancelled!").build();
        allArticles.add(article1);
        allArticles.add(article2);
        allArticles.add(article3);
        allArticles.add(article4);
        allArticles.add(article5);
        allArticles.add(article6);
    }
    public List<Integer> clients=new ArrayList<>();

    @Override
    public void joinServer(ClientRegistrationRequest request, StreamObserver<ClientRegistrationResponse> responseObserver){
        logger.info("Got Registration Request For Client ID : "+request.getClientId());
        System.out.println("Join Server Request from client : "+request.getClientId());
        ClientRegistrationResponse response;
        if(clients.size()<MAX_CLIENTS) {
            clients.add(request.getClientId());
            response = ClientRegistrationResponse.newBuilder().setClientId(request.getClientId()).setRegistrationResponse("SUCCESS").build();
        }
        else{
            response = ClientRegistrationResponse.newBuilder().setClientId(request.getClientId()).setRegistrationResponse("FAILED").build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void leaveServer(ClientRegistrationRequest request, StreamObserver<ClientRegistrationResponse> responseObserver){
        System.out.println("Leave Server Request from client : "+request.getClientId());
        ClientRegistrationResponse response;
        if(clients.contains(request.getClientId()))
        {
            clients.remove(Integer.valueOf(request.getClientId()));
            response= ClientRegistrationResponse.newBuilder().setClientId(request.getClientId()).setRegistrationResponse("SUCCESS").build();
        }
        else{
            response = ClientRegistrationResponse.newBuilder().setClientId(request.getClientId()).setRegistrationResponse("FAILED").build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void publishArticle(PublishArticleRequest request, StreamObserver<PublishArticleResponse> responseObserver){
        System.out.println("Publish Article Request from client : "+request.getClientId());
        PublishArticleResponse response;
        if(clients.contains(request.getClientId())) {
            allArticles.add(request.getArticle());
            response = PublishArticleResponse.newBuilder().setSuccessResponse("SUCCESS").build();
        }
        else{
            response = PublishArticleResponse.newBuilder().setErrorResponse("FAILED").build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void getArticles(ArticleRequest request, StreamObserver<ArticleResponse> responseObserver) {
        System.out.println("Get Article Request from client : "+request.getClientId());
        List<Article> articleList = new ArrayList<>();
        if(clients.contains(request.getClientId())) {

            if (!request.getType().equals("NA") && !request.getDate().equals("NA") && !request.getAuthor().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (allArticles.get(i).getType().equals(request.getType()) && formatDate(allArticles.get(i).getDate()).isAfter(formatDate(request.getDate())) && allArticles.get(i).getAuthor().equals(request.getAuthor()))
                        articleList.add(allArticles.get(i));
                }
            } else if (!request.getType().equals("NA") && !request.getDate().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (allArticles.get(i).getType().equals(request.getType()) && formatDate(allArticles.get(i).getDate()).isAfter(formatDate(request.getDate())))
                        articleList.add(allArticles.get(i));
                }
            } else if (!request.getType().equals("NA") && !request.getAuthor().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (allArticles.get(i).getType().equals(request.getType()) && allArticles.get(i).getAuthor().equals(request.getAuthor()))
                        articleList.add(allArticles.get(i));
                }
            } else if (!request.getDate().equals("NA") && !request.getAuthor().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (formatDate(allArticles.get(i).getDate()).isAfter(formatDate(request.getDate())) && allArticles.get(i).getAuthor().equals(request.getAuthor()))
                        articleList.add(allArticles.get(i));
                }
            } else if (!request.getType().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (allArticles.get(i).getType().equals(request.getType()))
                        articleList.add(allArticles.get(i));
                }

            } else if (!request.getDate().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (formatDate(allArticles.get(i).getDate()).isAfter(formatDate(request.getDate())))
                        articleList.add(allArticles.get(i));
                }

            } else if (!request.getAuthor().equals("NA")) {
                for (int i = 0; i < allArticles.size(); i++) {
                    if (allArticles.get(i).getAuthor().equals(request.getAuthor()))
                        articleList.add(allArticles.get(i));
                }
            } else {
                articleList.addAll(allArticles);
            }
        }
        else {
            articleList=new ArrayList<>();
        }

            ArticleResponse articleResponse = ArticleResponse.newBuilder().addAllArticles(articleList).build();
            responseObserver.onNext(articleResponse);
            responseObserver.onCompleted();
        }



    public static LocalDate formatDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate;
    }

    //THIS METHOD IS FOR BONUS PART OF ASSIGNMENT- ONE SERVER CAN BECOME CLIENT OF ANOTHER SERVER
    @Override
    public void serverBecomesClient(Empty request, StreamObserver<ArticleResponse> responseObserver) {
        ArticleResponse articleResponse=ArticleResponse.newBuilder().addAllArticles(allArticles).build();
        responseObserver.onNext(articleResponse);
        responseObserver.onCompleted();
    }
}

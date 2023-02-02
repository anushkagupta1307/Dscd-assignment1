package org.example.queue;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class TopicExchange {
    public static void declareExchange() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.exchangeDeclare("my-topic-exchange", BuiltinExchangeType.TOPIC, true);
        channel.close();
    }

    public static void declareQueues() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.queueDeclare("FashionQ", true, false, false, null);
        channel.queueDeclare("SportsQ", true, false, false, null);
        channel.queueDeclare("PoliticsQ", true, false, false, null);
        channel.close();
    }

    public static void declareBindings() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create bindings - (queue, exchange, routingKey) - routingKey != null
        channel.queueBind("FashionQ", "my-topic-exchange", "fashion");
        channel.queueBind("SportsQ", "my-topic-exchange", "sports");
        channel.queueBind("PoliticsQ", "my-topic-exchange", "politics");
        channel.close();
    }

    public static void subscribeFashionMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume("FashionQ", true, ((consumerTag, message) -> {
            System.out.println("\n\n=========== Fashion Queue ==========");
            System.out.println(consumerTag);
            System.out.println("FashionQ: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }

    public static void subscribeSportsMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume("SportsQ", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ Sports Queue ==========");
            System.out.println(consumerTag);
            System.out.println("SportsQ: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }

    public static void subscribePoliticsMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume("PoliticsQ", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ Politics Queue ==========");
            System.out.println(consumerTag);
            System.out.println("PoliticsQ: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }

    public static void publishFashionMessage(String message) throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicPublish("my-topic-exchange", "fashion", null, message.getBytes());
        channel.close();
    }

    public static void publishSportsMessage(String message) throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicPublish("my-topic-exchange", "sports", null, message.getBytes());
        channel.close();
    }

    public static void publishPoliticsMessage(String message) throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicPublish("my-topic-exchange", "politics", null, message.getBytes());
        channel.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        TopicExchange.declareExchange();
        TopicExchange.declareQueues();
        TopicExchange.declareBindings();
        Thread subscribe = new Thread(() -> {
            try {
                TopicExchange.subscribeFashionMessage();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        Thread publish = new Thread(() -> {
            try {
                TopicExchange.publishFashionMessage("Vogue Fashion Week is Here!!");
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        subscribe.start();
        publish.start();
    }
}

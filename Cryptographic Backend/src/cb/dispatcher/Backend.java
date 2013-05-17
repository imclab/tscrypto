package cb.dispatcher;

import java.io.IOException;

import cb.backend.ResponseMessage;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Backend {
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	public static void main(String[] args) 
			throws ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		String queueName = "";
		String hostName = "";
		
		// Manejo de argumentos...
		switch (args.length) {
		case 0:
			hostName = "localhost";
			queueName = "rpc_queue";
			break;

		case 1:
			hostName = args[0];
			queueName = "rpc_queue";
			break;
			
		case 2:
			hostName = args[0];
			queueName = args[1];
			break;
			
		default:
			System.err.println("Muchos argumentos.");
			System.exit(1);
		}
		
		Gson gson = new Gson();
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostName);
		Connection connection;
		Channel channel;
		QueueingConsumer consumer;
		
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);
			channel.basicQos(1);
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, false, consumer);
		}
		catch (IOException e) {
			System.err.println("No se puede conectar al servidor...");
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
			return; // Mañas de java :).
		}
		
		System.err.println("Esperando solicitudes...");
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			BasicProperties props = delivery.getProperties();
			BasicProperties replyProps = new BasicProperties()
				.builder()
				.correlationId(props.getCorrelationId())
				.build();
			
			String message = new String(delivery.getBody());
			System.err.println(message);
			MethodMessage mmessage;
			try {
				mmessage = gson.fromJson(message, MethodMessage.class);
			}
			catch (JsonSyntaxException e) {
				ResponseMessage.ErrorMessage(e.getLocalizedMessage());
				System.err.println(e.getLocalizedMessage());
				continue;
			}
			System.err.println("Ejecutando " + mmessage.getMethod() + "...");
			MethodDispatcher dispatcher = new MethodDispatcher(mmessage);
			String response = dispatcher.dipatch();
			try {
				channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes());
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
			catch (Exception e) {
				System.err.println("No se pudo enviar mensaje al servidor...");
				System.err.println(e.getLocalizedMessage());	
			}
		}

	}

}

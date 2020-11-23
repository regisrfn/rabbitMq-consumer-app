package com.rufino.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;

import com.rufino.server.api.RabbitMqController;
import com.rufino.server.databaseConn.DatabaseConnection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class ServerApplicationTests {

	@Value("${sample.rabbitmq.exchange}")
	private String exchange;
	@Value("${sample.rabbitmq.routingkey}")
	private String routingkey;
	@Autowired
	private AmqpTemplate rabbitTemplate;
	@Autowired
	private RabbitMqController rabbitMqController;

	private static Connection conn;

	// INTEGRATION - send message to real server
	@Test
	public void sendMessage() {
		String message = "{ \"idClient\":111111,\"orderAddress\": \"rua de baixo\" }";
		rabbitTemplate.convertAndSend(exchange, routingkey, message);
		System.out.println("Send msg to consumer= " + message + " ");
	}

	@BeforeAll
	public static void openConnection() throws SQLException {
		conn = DatabaseConnection.getInstance().getConnection();
		assertNotNull(conn);
	}

	@AfterAll
	public static void closeConnection() throws SQLException {
		assertNotNull(conn);
		conn.close();
		assertEquals(true, conn.isClosed());
	}

	//////////////////////////////////////////////////////////////////////////////
	// UNIT TEST
	@Test
	void contextLoads() {
		rabbitMqController.receivedMessage("{ \"idClient\":456,\"orderAddress\": \"rua de baixo\" }");
	}

	@Test
	public void rabbitTestUrl() {
		URI rabbitMq;
		String rabbitMqUrl = getEnv("CLOUDAMQP_URL");
		if (rabbitMqUrl == null) {
			rabbitMq = URI.create("amqp://guest:guest@localhost:5672");
			assertEquals("amqp://guest:guest@localhost:5672", rabbitMq.toString());
			assertEquals("localhost", rabbitMq.getHost());
			assertEquals("guest", rabbitMq.getUserInfo().split(":")[0]);
			assertEquals("guest", rabbitMq.getUserInfo().split(":")[1]);
			assertEquals(5672, rabbitMq.getPort());
			assertEquals("", rabbitMq.getPath());
		} else {
			rabbitMq = URI.create(rabbitMqUrl);
		}
	}

	private String getEnv(String name) {
		final String env = System.getenv(name);
		if (env == null) {
			System.out.println("Environment variable [" + name + "] is not set.");
		}
		return env;
	}

}

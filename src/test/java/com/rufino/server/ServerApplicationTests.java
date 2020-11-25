package com.rufino.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.api.RabbitMqController;
import com.rufino.server.databaseConn.DatabaseConnection;
import com.rufino.server.model.Delivery;
import com.rufino.server.services.DeliveryService;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MockMvc mockMvc;

	private static Connection conn;

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

	@BeforeEach
	void clearTable() {
		jdbcTemplate.update("DELETE FROM Delivery");
	}

	// INTEGRATION - send message to real server - test connection do database
	@Test
	public void sendMessage() {
		String message = "{ \"idClient\":111111,\"orderAddress\": \"rua de baixo\" }";
		rabbitTemplate.convertAndSend(exchange, routingkey, message);
		System.out.println("Send msg to consumer= " + message + " ");
	}

	//////////////////////////////////////////////////////////////////////////////
	// UNIT TEST
	////////////////////// RABBITMQ TEST
	@Test
	void rabbitMqReceivedMessage() {
		rabbitMqController.receivedMessage("{ \"idOrder\":456,\"orderAddress\": \"rua de baixo\" }");
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

	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\TEST SELECT ALL
	@Test
	public void selectAllDeliveryDAO() {
		Delivery delivery = new Delivery();
		createAndAssert(delivery);
		List<Delivery> Db = deliveryService.getAll();
		assertEquals(delivery.getIdDelivery(), Db.get(0).getIdDelivery());
		assertEquals(delivery.getIdOrder(), Db.get(0).getIdOrder());
		assertEquals(delivery.getOrderAddress(), Db.get(0).getOrderAddress());
	}

	@Test
	void selectAllDeliveryHttp() throws Exception {
		Delivery delivery = new Delivery();
		createAndAssert(delivery);
		MvcResult result = mockMvc.perform(get("/api/v1/delivery").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String contentString = result.getResponse().getContentAsString();
		ObjectMapper om = new ObjectMapper();
		List<Delivery> deliveryList = Arrays.asList(om.readValue(contentString, Delivery[].class));
		List<Delivery> Db = deliveryService.getAll();
		assertEquals(deliveryList.get(0).getIdDelivery(), Db.get(0).getIdDelivery());
		assertEquals(deliveryList.get(0).getIdOrder(), Db.get(0).getIdOrder());
		assertEquals(deliveryList.get(0).getOrderAddress(), Db.get(0).getOrderAddress());

	}

	@Test
	void selectAllDeliveryHttp_Empty() throws Exception {
		long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from delivery", Long.class);
		assertEquals(0, countBeforeInsert);
		MvcResult result = mockMvc.perform(get("/api/v1/delivery").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String contentString = result.getResponse().getContentAsString();
		ObjectMapper om = new ObjectMapper();
		List<Delivery> deliveryList = Arrays.asList(om.readValue(contentString, Delivery[].class));
		assertEquals(new ArrayList<>(), deliveryList);

	}

	private String getEnv(String name) {
		final String env = System.getenv(name);
		if (env == null) {
			System.out.println("Environment variable [" + name + "] is not set.");
		}
		return env;
	}

	// -----------------------------------------------------
	private void createAndAssert(Delivery delivery) {
		UUID id = UUID.randomUUID();
		delivery.setIdOrder(id);
		delivery.setOrderAddress("Rua de cima");
		long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from delivery", Long.class);
		assertEquals(0, countBeforeInsert);
		deliveryService.addDelivery(delivery);
		long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from delivery", Long.class);
		assertEquals(1, countAfterInsert);
	}

}

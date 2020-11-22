package com.rufino.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.SocketUtils;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		setRandomPort(5000, 5100);
		SpringApplication.run(ServerApplication.class, args);
	}

	public static void setRandomPort(int minPort, int maxPort) {
		try {
			 String userDefinedPort = System.getProperty("server.port", System.getenv("SERVER_PORT"));
			 if (userDefinedPort == null) {
				int port = SocketUtils.findAvailableTcpPort(minPort, maxPort);
				System.setProperty("server.port", String.valueOf(port));
				System.out.printf("Server port set to %d.", port);
			  }
		 } catch (IllegalStateException e) {
			System.out.printf("No port available in range %d-%d. Default embedded server configuration will be used.",minPort,maxPort);
		 }
	 }

}

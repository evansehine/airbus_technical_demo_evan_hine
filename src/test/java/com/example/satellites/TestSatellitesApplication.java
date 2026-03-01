package com.example.satellites;

import org.springframework.boot.SpringApplication;

public class TestSatellitesApplication {

	public static void main(String[] args) {
		SpringApplication.from(SatellitesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

package com.ronanski11.teezinator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Debugger implements CommandLineRunner{
	
	@Value("${spring.data.mongodb.uri}")
	String mongoUri;

	@Override
	public void run(String... args) throws Exception {
		log.debug(mongoUri);
		
	}

}
package ru.bkolomin.priceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ru.bkolomin.priceLoader.Service.PriceService;

@SpringBootApplication
public class PriceLoaderApplication implements CommandLineRunner {

	static Logger logger = LoggerFactory.getLogger(PriceLoaderApplication.class);

	@Autowired
	PriceService priceService;

	public static void main(String[] args) {

		logger.error("start app");

		SpringApplication.run(PriceLoaderApplication.class, args);

		logger.error("end app");
	}

	@Override
	public void run(String... args) {

		priceService.loadAllFiles();

	}
}
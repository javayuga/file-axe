package br.dev.marcosilva.fileaxe;

import br.dev.marcosilva.fileaxe.csv.CSVAxerImpl;
import br.dev.marcosilva.fileaxe.csv.fs.SimpleFSCreateTimeStampFolder;
import br.dev.marcosilva.fileaxe.csv.fs.SimpleCSVFileSystemSave;
import br.dev.marcosilva.fileaxe.configuration.FileAxeConfigurations;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
@Slf4j
class SimpleFileSystemAxerTests {

	@Autowired
	FileAxeConfigurations fileAxeConfigurations;

	@Autowired
	SimpleFSCreateTimeStampFolder simpleFSCreateTimeStampFolder;

	@Autowired
	SimpleCSVFileSystemSave simpleFSSave;

	@Test
	void testSimpleFileSystemAxer() throws IOException {

		log.info("testing a CSV file axer which reads from and writes to the local file system");

		// starts with a small version
		File initialFile = new File("src/test/resources/inf_diario_fi_201911_mini.csv");
		CSVAxerImpl simpleFileSystemAxer =
				CSVAxerImpl.builder()
						.filePreAxingStrategy(simpleFSCreateTimeStampFolder)
						.fileAxingStrategy(simpleFSSave)
						.chunkSize(400L).build();

		InputStream targetStream = new FileInputStream(initialFile);
		simpleFileSystemAxer.processStream("in_diario_fi_201911_mini.csv", targetStream);

		// gets bigger
		initialFile = new File("src/test/resources/inf_diario_fi_201911.csv");
		simpleFileSystemAxer =
				CSVAxerImpl.builder()
						.filePreAxingStrategy(simpleFSCreateTimeStampFolder)
						.fileAxingStrategy(simpleFSSave)
						.chunkSize(fileAxeConfigurations.getChunkSize()).build();

		targetStream = new FileInputStream(initialFile);
		simpleFileSystemAxer.processStream("in_diario_fi_201911.csv", targetStream);

	}

}

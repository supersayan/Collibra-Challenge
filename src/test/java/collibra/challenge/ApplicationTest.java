package collibra.challenge;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

	@Autowired
	private AssetController assetController;

	@Test
	void contextLoads() throws Exception {
		assertNotNull(assetController);
	}

}

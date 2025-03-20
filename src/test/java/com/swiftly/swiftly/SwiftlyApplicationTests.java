package com.swiftly.swiftly;

import com.swiftly.swiftly.model.SwiftCode;
import com.swiftly.swiftly.repository.SwiftCodeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SwiftlyApplicationTests {

	@Autowired
	private SwiftCodeRepository repository;

	@Test
	void contextLoads() {
		SwiftCode code = new SwiftCode("TESTPL01", "Test Bank", "PL", "POLAND", "Test Address", true);
		repository.save(code);
		SwiftCode found = repository.findBySwiftCode("TESTPL01");
		Assertions.assertThat(found).isNotNull();
	}
}

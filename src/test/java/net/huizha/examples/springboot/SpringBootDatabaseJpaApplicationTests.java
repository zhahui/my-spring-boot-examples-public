package net.huizha.examples.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
class SpringBootDatabaseJpaApplicationTests {

    private final ApplicationContext applicationContext;

    @Test
    void applicationContext_should_load() {
        assertThat(applicationContext).isNotNull();
    }
}

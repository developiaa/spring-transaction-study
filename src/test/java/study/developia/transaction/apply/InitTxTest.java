package study.developia.transaction.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@SpringBootTest
public class InitTxTest {
    @Autowired
    Hello hello;

    @Test
    void go() {
        // 초기화코드는 스프링이 초기화 시점에 호출한다.

//        hello.initV1(); // 직접호출하면 트랜잭션 적용 된다.
    }

    @TestConfiguration
    static class InitTxTestConfig {

        @Bean
        Hello hello() {
            return new Hello();
        }
    }


    @Slf4j
    static class Hello {

        @PostConstruct // 초기화 코드가 먼저 호출되고 이후에 트랜잭션 AOP가 적용된다.
        @Transactional
        public void initV1() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello initV1 @PostConstruct tx active={}", isActive);
        }

        @EventListener(ApplicationReadyEvent.class) // 스프링 컨테이너가 완전히 구동된 이후에 실행한다.
        @Transactional
        public void initV2() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello initV2 ApplicationReadyEvent tx active={}", isActive);
        }
    }
}

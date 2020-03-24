package shuaicj.example.micrometer;

import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The test controller.
 *
 * @author shuaicj 2020/03/20
 */
@RestController
public class HelloController {

    private final Counter counter;
    private final Timer timer;
    private final LongTaskTimer longTimer;

    public HelloController(MeterRegistry registry) {
        // counter
        this.counter = Counter.builder("hello.counter")
                              .description("This is a Counter.")
                              .tag("a_tag_of_this_counter", "counter_tag_value")
                              .register(registry);
        // gauge
        Gauge.builder("hello.gauge", this::random)
             .description("This is a Gauge.")
             .tag("a_tag_of_this_gauge", "gauge_tag_value")
             .register(registry);

        // timer
        this.timer = Timer.builder("hello.timer")
                          .description("This is a Timer.")
                          .publishPercentiles(0.5, 0.9)
                          .tag("a_tag_of_this_timer", "timer_tag_value")
                          .register(registry);

        // long timer
        this.longTimer = LongTaskTimer.builder("hello.timer.long")
                                      .description("This is a LongTaskTimer.")
                                      .tag("a_tag_of_this_long_timer", "long_timer_tag_value")
                                      .register(registry);
    }

    @GetMapping("/hello")
    public String hello() {
        counter.increment();
        timer.record(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException ignore) {
            }
        });
        return "hello";
    }

    @GetMapping("/hello-long")
    public String helloLong() {
        longTimer.record(() -> {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException ignore) {
            }
        });
        return "hello-long";
    }

    private double random() {
        return Math.random();
    }
}

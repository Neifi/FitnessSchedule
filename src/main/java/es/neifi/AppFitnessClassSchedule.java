package es.neifi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import es.neifi.schedule.controller.ScheduleController;

@SpringBootApplication
@ComponentScan(basePackageClasses = ScheduleController.class)

public class AppFitnessClassSchedule {
    public static void main(String[] args) {

        SpringApplication.run(AppFitnessClassSchedule.class,args);
    }

}
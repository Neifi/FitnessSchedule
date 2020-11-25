package es.neifi.schedule.controller;

import es.neifi.schedule.model.Schedule;
import es.neifi.schedule.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@AllArgsConstructor
public class ScheduleController{
    private final ScheduleRepository scheduleRepository = new ScheduleRepository();

    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> findOne(@PathVariable Long id){
        if (id == null) return ResponseEntity.badRequest().build();
        Optional<Schedule> result = scheduleRepository.findById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    public ResponseEntity<Schedule> createSchedule(@NonNull @RequestBody Schedule scheduleBody){

        scheduleRepository.save(scheduleBody);
        haveNullValues(scheduleBody);
        return  ResponseEntity.ok(scheduleBody);
    }

    private void haveNullValues(Schedule scheduleBody){
        if (scheduleBody.getId() == null && scheduleBody.getScheduleName() == null)
            throw new RuntimeException("ID and name of schedule cannot be empty ");
    }

    public ResponseEntity<Schedule> updateSchedule(@NonNull @RequestBody Schedule scheduleBody, @NonNull Long id){
        scheduleRepository.update(scheduleBody,id);
        Schedule result = findOne(id).getBody();
        if (result == null) throw new  RuntimeException("Cannot get the schedule");
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<HttpStatus> deleteSchedule(@NonNull @PathVariable Long id){
        Optional<Schedule> toDelete = scheduleRepository.findById(id);
        deleteFitnessClasses(toDelete.orElseThrow(RuntimeException::new));
        scheduleRepository.deleteById(id);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    private void deleteFitnessClasses(Schedule toDelete) {
        toDelete.getFitnessClasses().forEach(e -> {
            toDelete.removeFitnessClass(e.getId());
        });

    }


}

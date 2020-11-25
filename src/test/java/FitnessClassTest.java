
import es.neifi.fitnessClass.CardioClass;
import es.neifi.fitnessClass.model.FitnessClass;
import es.neifi.schedule.controller.ScheduleController;
import es.neifi.schedule.fitnessClassSchedule.CommonSchedule;
import es.neifi.schedule.model.Schedule;
import org.junit.Before;
import org.junit.Test;
import es.neifi.schedule.repository.ScheduleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class FitnessClassTest {

    private  Schedule emptySchedule;
    private  Schedule manyClassSchedule;
    private  FitnessClass cycling;
    private  FitnessClass zumba;
    private  FitnessClass bodyPump;

    private ScheduleRepository scheduleRepository;


    private ScheduleController scheduleController;



    @Before
    public  void setup(){
        emptySchedule = new CommonSchedule();
        emptySchedule.setId(1L);
        manyClassSchedule = new CommonSchedule();
        manyClassSchedule.setId(2L);


        cycling = new CardioClass();
        cycling.setId(1L);
        cycling.setName("Cycling");
        cycling.setTeachers(Collections.singleton(1L));
        cycling.setDuration(90);
        cycling.setDate(LocalDateTime.of(2020,11,27,19,30));

        zumba = new CardioClass();
        zumba.setId(2L);
        zumba.setName("Zumba");
        zumba.setDate(LocalDateTime.of(2020,11,27,17,30));

        bodyPump = new CardioClass();
        bodyPump.setId(3L);
        bodyPump.setName("BodyPump");
        bodyPump.setDate(LocalDateTime.of(2020,11,27,18,30));

        scheduleRepository = new ScheduleRepository();
        scheduleController = new ScheduleController();

    }

    @Test
    public void createEmptyClassSchedule(){
        scheduleRepository.save(emptySchedule);
        Optional<Schedule> expectedSchedule = scheduleRepository.findById(emptySchedule.getId());

        assertTrue(expectedSchedule.isPresent());
        assertEquals(emptySchedule,expectedSchedule.get());
        assertFalse(emptySchedule.haveClasses());
    }

    @Test
    public void addClassToSchedule(){
        manyClassSchedule.addFitnessClass(cycling);
        manyClassSchedule.addFitnessClass(bodyPump);
        manyClassSchedule.addFitnessClass(zumba);

        Long cyclingID = cycling.getId();
        Long bodyPumpID = bodyPump.getId();
        Long zumbaID = zumba.getId();

        assertTrue(manyClassSchedule.haveClasses());
        assertTrue(manyClassSchedule.isClassPresent(cyclingID));
        assertTrue(manyClassSchedule.isClassPresent(bodyPumpID));
        assertTrue(manyClassSchedule.isClassPresent(zumbaID));
    }

    @Test
    public void retrieveFitnessClassesFromSchedule(){
        manyClassSchedule.addFitnessClass(cycling);
        manyClassSchedule.addFitnessClass(bodyPump);
        manyClassSchedule.addFitnessClass(zumba);

        Long cyclingID = cycling.getId();
        Long bodyPumpID = bodyPump.getId();
        Long zumbaID = zumba.getId();

        assertTrue(manyClassSchedule.haveClasses());
        assertTrue(manyClassSchedule.getClassByID(cyclingID).isPresent());
        assertEquals(cycling,manyClassSchedule.getClassByID(cyclingID).get());
        assertTrue(manyClassSchedule.getClassByID(bodyPumpID).isPresent());
        assertEquals(bodyPump,manyClassSchedule.getClassByID(bodyPumpID).get());
        assertTrue(manyClassSchedule.getClassByID(zumbaID).isPresent());
        assertEquals(zumba,manyClassSchedule.getClassByID(zumbaID).get());
        assertEquals(3, manyClassSchedule.getFitnessClasses().size());
    }

    @Test
    public void deleteClassFromSchedule(){
        Schedule toDeleteClassSchedule = new CommonSchedule();
        toDeleteClassSchedule.addFitnessClass(cycling);
        toDeleteClassSchedule.addFitnessClass(bodyPump);

        Long cyclingID = cycling.getId();
        Long bodyPumpID = bodyPump.getId();

        toDeleteClassSchedule.removeFitnessClass(cyclingID);

        assertTrue(toDeleteClassSchedule.haveClasses());
        assertFalse(toDeleteClassSchedule.isClassPresent(cyclingID));
        assertTrue(toDeleteClassSchedule.isClassPresent(bodyPumpID));
        assertEquals(1,toDeleteClassSchedule.getFitnessClasses().size());
    }

    @Test
    public void modifyFitnessClassDataFromScheduleAndSAveScheduleToRepo(){
        manyClassSchedule.setId(1L);
        manyClassSchedule.addFitnessClass(cycling);
        manyClassSchedule.addFitnessClass(bodyPump);

        String cyclingName = "CYCLING_";
        String bodyPumpName = bodyPump.getName();
        Long cyclingID = cycling.getId();
        Long bodyPumpID = bodyPump.getId();
        manyClassSchedule.getClassByID(cyclingID).get().setName(cyclingName);
        scheduleRepository.save(manyClassSchedule);

        Optional<Schedule> expectedSchedule = scheduleRepository.findById(manyClassSchedule.getId());


        assertTrue(manyClassSchedule.haveClasses());
        assertTrue(manyClassSchedule.getClassByID(cyclingID).isPresent());
        assertEquals(cyclingName,manyClassSchedule.getClassByID(cyclingID).get().getName());
        assertTrue(manyClassSchedule.getClassByID(bodyPumpID).isPresent());
        assertEquals(bodyPumpName,manyClassSchedule.getClassByID(bodyPumpID).get().getName());
        assertEquals(cyclingID,manyClassSchedule.getClassByID(cyclingID).get().getId());
        assertEquals(bodyPumpID,manyClassSchedule.getClassByID(bodyPumpID).get().getId());
        assertTrue(expectedSchedule.isPresent());
        assertEquals(manyClassSchedule,expectedSchedule.get());
        assertEquals(2,manyClassSchedule.getFitnessClasses().size());
    }


    @Test
    public void deleteScheduleFromRepository(){
        Schedule toDeleteSchedule = new CommonSchedule();
        toDeleteSchedule.setId(3L);

        scheduleRepository.save(toDeleteSchedule);
        scheduleRepository.save(manyClassSchedule);
        scheduleRepository.save(emptySchedule);
        scheduleRepository.delete(toDeleteSchedule);


        assertFalse(scheduleRepository.findById(toDeleteSchedule.getId()).isPresent());

        assertTrue(scheduleRepository.findById(manyClassSchedule.getId()).isPresent());
        assertTrue(scheduleRepository.findById(emptySchedule.getId()).isPresent());
        assertEquals(manyClassSchedule,scheduleRepository.findById(manyClassSchedule.getId()).get());
        assertEquals(emptySchedule,scheduleRepository.findById(emptySchedule.getId()).get());

    }

    @Test
    public void sortScheduleFitnessClassByDate(){
        Schedule sortedSchedule = new CommonSchedule();
        sortedSchedule.addFitnessClass(cycling);
        sortedSchedule.addFitnessClass(zumba);
        sortedSchedule.addFitnessClass(bodyPump);

        Long cyclingID = cycling.getId();
        Long bodyPumpID = bodyPump.getId();
        Long zumbaID = zumba.getId();

        List<FitnessClass> sortedArray = sortedSchedule.sortSchedule();

        List<FitnessClass> expectedFitnessClassesArrayList = new ArrayList<>();
        expectedFitnessClassesArrayList.add(zumba);
        expectedFitnessClassesArrayList.add(bodyPump);
        expectedFitnessClassesArrayList.add(cycling);
        assertFalse(sortedSchedule.getFitnessClasses().isEmpty());
        assertTrue(sortedSchedule.getClassByID(cyclingID).isPresent());
        assertEquals(cycling,sortedSchedule.getClassByID(cyclingID).get());
        assertTrue(sortedSchedule.getClassByID(bodyPumpID).isPresent());
        assertEquals(bodyPump,sortedSchedule.getClassByID(bodyPumpID).get());
        assertTrue(sortedSchedule.getClassByID(zumbaID).isPresent());
        assertEquals(zumba,sortedSchedule.getClassByID(zumbaID).get());
        assertEquals(expectedFitnessClassesArrayList,sortedArray);
        assertEquals(3,sortedSchedule.getFitnessClasses().size());

    }



}

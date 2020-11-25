package es.neifi.schedule.model;

import es.neifi.fitnessClass.model.FitnessClass;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
public  class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String scheduleName;
    private LocalDateTime expiryDate;
    private final LocalDateTime creationDate = LocalDateTime.now();
    private final Map<Long,FitnessClass> fitnessClasses = new HashMap<>();

    public Schedule() {

    }

    public void addFitnessClass(FitnessClass fitnessClass) {
        if (fitnessClass == null) return;
        fitnessClasses.put(fitnessClass.getId(),fitnessClass);
    }

    public boolean haveClasses() {
        return !fitnessClasses.isEmpty();
    }

    public Optional<FitnessClass> getClassByID(Long fitnessClassID) {
            if (fitnessClasses.containsKey(fitnessClassID))
                return Optional.of(fitnessClasses.get(fitnessClassID));

        return Optional.empty();
    }

    public void removeFitnessClass(Long fitnessClassID) {
        if(getClassByID(fitnessClassID).isPresent())
            fitnessClasses.remove(fitnessClassID);
    }

    public boolean isClassPresent(Long cyclingID) {
        return getClassByID(cyclingID).isPresent();
    }

    public List<FitnessClass> getFitnessClasses() {

        return new ArrayList<>(fitnessClasses.values());
    }

    public List<FitnessClass> sortSchedule(){
        List<FitnessClass> sortedList = getFitnessClasses();
        Comparator<FitnessClass> dates = (f1, f2) -> {
            if(f1.getDate().isBefore(f2.getDate())) return -1;
            if(f1.getDate().isEqual(f2.getDate())) return 0;
            else return 1;
        };

        sortedList.sort(dates);

        return sortedList;
    }


}

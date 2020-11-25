package es.neifi.schedule.repository;

import es.neifi.schedule.model.Schedule;

import java.util.*;
import java.util.stream.Collectors;

public class ScheduleRepository extends MainRepository<Schedule,Long>
{
    private Map<Long,Schedule> data = new HashMap();

    @Override
    public Optional<Schedule> findById(Long id) {

        if(data.containsKey(id)) return Optional.of(data.get(id));

        return Optional.empty();
    }

    public  List<Schedule> findAll() {
        return new ArrayList<Schedule>(data.values());
    }

    @Override
    public void save(Schedule obj) {
        if(obj == null) return;
        data.put(obj.getId(),obj);
    }

    @Override
    public void update(Schedule obj, Long id) {
        if (obj == null) return;
        if(findById(id).isPresent())
            save(obj);
    }

    @Override
    public void delete(Schedule obj) {
        if (obj == null) return;
        if (data.containsValue(obj))
            data.remove(obj.getId());

    }

    @Override
    public void deleteById(Long id) {
        if (id == null) return;
        if (data.containsKey(id))
            delete(data.get(id));
    }
}

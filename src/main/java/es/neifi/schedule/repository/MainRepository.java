package es.neifi.schedule.repository;

import java.util.List;
import java.util.Optional;

public abstract class MainRepository<T, ID> {


    public abstract Optional<T> findById(ID id);
    public abstract void save(T obj);
    public abstract void update(T obj,Long id);
    public abstract void delete(T obj);
    public abstract void deleteById(ID id);




}

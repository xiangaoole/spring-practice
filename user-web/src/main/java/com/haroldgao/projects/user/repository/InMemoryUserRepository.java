package com.haroldgao.projects.user.repository;

import com.haroldgao.projects.user.domain.User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存型 {@link UserRepository} 实现
 *
 * @since 1.0
 */
public class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean save(User user) {
        return repository.put(user.getId(), user) == null;
    }

    @Override
    public boolean deleteById(Long userId) {
        return repository.remove(userId) != null;
    }

    @Override
    public boolean update(User user) {
        save(user);
        return true;
    }

    @Override
    public User getById(Long userId) {
        return repository.get(userId);
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return repository.values()
                .stream()
                .filter(user -> Objects.equals(user.getName(), userName)
                        && Objects.equals(user.getPassword(), password))
                .findFirst()
                .get();
    }

    @Override
    public Collection<User> getAll() {
        return repository.values();
    }
}

package com.haroldgao.projects.user.service;

import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.sql.LocalTransactional;

/**
 * UserService for CRUD operation on {@link User}
 *
 * @since v4.1
 */
public interface UserService {

    /**
     * Register User
     *
     * @param user
     * @return      return <code>true</code> if succeed
     */
    @LocalTransactional
    boolean register(User user);

    /**
     * Deregister User
     *
     * @param user
     * @return      return <code>true</code> if succeed
     */
    boolean deregister(User user);

    boolean update(User user);

    boolean queryUserById(long id);

    boolean queryUserByNameAndPassword(String name, String password);

}

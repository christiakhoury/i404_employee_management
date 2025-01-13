package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(EntityManager theEntityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = theEntityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


//    public User findByUserName(String theUserName) {
//
//        sql = "select r.name, u.id, u.email, u.first_name, u.last_name, u.password, u.username, u.role" +
//                " from uuser u full outer join users_roles m on u.id = m.user_id" +
//                " full outer join role r on m.role_id = r.id where u.username = ? ";
//        return jdbcTemplate.queryForObject(sql, userMapper, theUserName);
//    }
    @Override
    public User findByUserName(String theUserName) {

        // retrieve/read from database using username
        TypedQuery<User> theQuery = entityManager.createQuery("from User where userName=:uName", User.class);
        theQuery.setParameter("uName", theUserName);

        User theUser = null;
        try {
            theUser = theQuery.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }
        return theUser;
    }

    public int save(User user) {
        String sql = "insert into uuser (password, username, role, employee_id) " +
                "values (?,?,?,?)";
        return jdbcTemplate.update(sql,
                user.getPassword(), user.getUserName(), user.getRole(), user.getEmployee().getId());
    }
//    @Override
//    public User save(User user) {
//        entityManager.persist(user);
//        return user;
//    }
}


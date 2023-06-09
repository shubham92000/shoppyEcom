package com.shoppy.admin.user;

import com.shoppy.common.entity.Role;
import com.shoppy.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userJohn = new User(
                "john@gmail.com",
                "password",
                "John",
                "Doe"
        );
        userJohn.addRole(roleAdmin);
        User savedUser = userRepository.save(userJohn);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRole() {
        User userRavi = new User(
                "ravi@gmail.com",
                "password",
                "ravi",
                "kumar"
        );
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);

        User savedUser = userRepository.save(userRavi);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User john = userRepository.findById(1).get();
        System.out.println(john);
        assertThat(john).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User john = userRepository.findById(1).get();
        john.setEnabled(true);
        john.setEmail("johndoe@gmail.com");
        userRepository.save(john);
    }

    @Test
    public void testUpdateUserRoles() {
        User ravi = userRepository.findById(2).get();
        Role roleSalesPerson = new Role(2);
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
//        ravi.getRoles().removeIf(role -> role.getId() > 0);
//        ravi.addRole(roleSalesPerson);
//        ravi.addRole(roleEditor);
        ravi.removeRole(roleSalesPerson);
//        ravi.addRole(roleAssistant);
        User savedUser = userRepository.save(ravi);
        System.out.println(savedUser);
    }

    @Test
    public void testDeleteUser () {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "johndoe@gmail.com";
        User user = userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisable () {
        Integer id = 4;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnable () {
        Integer id = 4;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 1;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> users = page.getContent();
        users.forEach(System.out::println);
        assertThat(users.size()).isEqualTo(pageSize);
    }
}

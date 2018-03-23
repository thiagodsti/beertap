package org.teda.beertap.factory;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.teda.beertap.ApplicationTest;
import org.teda.beertap.entity.User;

@Component
public class UserFactory extends ApplicationTest {

    public User create(String email) {
        User user = new User();
        user.setName("Test");
        user.setEmail(email);
        return user;
    }

    public User createAndSave(String email) {
        User user = create(email);
        ResponseEntity<Long> response = restTemplate.postForEntity("/users", user, Long.class);
        user.setId(response.getBody());
        return user;
    }
}

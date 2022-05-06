package com.bepastem.emsys;

import com.bepastem.exceptions.UserNameExistException;
import com.bepastem.jpadao.UsersJpaDao;
import com.bepastem.models.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
/*@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //to show we will use real database */
public class DBTest {

    @Autowired
    private ServiceHelper serviceHelper;

    public void testUserNameAlreadyExistIntegrationTest() throws Exception {

    }

    @Test
    public void testUserNameAlreadyExist() throws Exception {

    }
}

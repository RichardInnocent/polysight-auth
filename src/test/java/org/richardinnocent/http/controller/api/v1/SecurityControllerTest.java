package org.richardinnocent.http.controller.api.v1;

import static org.mockito.Mockito.*;

import java.security.PublicKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richardinnocent.http.controller.ControllerEndpointTest;
import org.richardinnocent.security.PublicPrivateKeyProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("UNIT_TEST")
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class SecurityControllerTest extends ControllerEndpointTest {

  private final PublicPrivateKeyProvider keyProvider = mock(PublicPrivateKeyProvider.class);

  @Test
  public void testPublicKey() throws Exception {
    PublicKey publicKey = mock(PublicKey.class);
    String algorithm = "test-algorithm";
    String format = "test-format";
    String key = "test-public-key";
    when(publicKey.getAlgorithm()).thenReturn(algorithm);
    when(publicKey.getFormat()).thenReturn(format);
    when(publicKey.getEncoded()).thenReturn(key.getBytes());
    when(keyProvider.getPublicKey()).thenReturn(publicKey);

    mvc.perform(get("/api/v1/security/publickey"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
       .andExpect(
           content().json(
               "{\"algorithm\": \"" + algorithm
                   + "\",\"format\": \"" + format
                   + "\", \"key\": \"" + key + "\"}"
           )
       );
  }

  @Override
  protected Object getController() {
    return new SecurityController(keyProvider);
  }
}
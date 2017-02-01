package com.ge.predix.solsvc.boot;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * Spins up Spring Boot and accesses the URLs of the Rest apis
 * 
 * @author predix
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest(
{
        "server.port=0"
})
public class HelloControllerIT
{

    @Value("${local.server.port}")
    private int          localServerPort;

    private URL          base;
    private RestTemplate template;

    /**
     * @throws Exception
     *             -
     */
    @Before
    public void setUp()
            throws Exception
    {
        this.template = new TestRestTemplate();
    }

    /**
     * 
     * @throws Exception
     *             -
     */
    @SuppressWarnings("nls")
    @Test
    public void getHello()
            throws Exception
    {
        this.base = new URL("http://localhost:" + this.localServerPort + "/echo");
        ResponseEntity<String> response = this.template.getForEntity(this.base.toString(), String.class);
        assertThat(response.getBody(), startsWith("Greetings from Predix"));

    }
}

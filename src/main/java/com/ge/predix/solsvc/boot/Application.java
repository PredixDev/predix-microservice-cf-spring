package com.ge.predix.solsvc.boot;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This project uses a SpringBoot HelloWorld as a starting point. Then it adds
 * in the capability to cf push a
 * 
 * The idea is that you'll use this project as a starting point for creating
 * your own Rest service. You can change "predix-microservice-template" to
 * "my-rest-service" or a name that suits you.
 * 
 * We provide a Rest Service and tests that invoke them 1. HelloController -
 * example using a SpringRestTemplate
 * 
 * The project also provides a point of view around Property file management and
 * Spring Profiles. Here is the hierarchy such that each lower number overrides
 * the one's after it
 * http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-
 * external-config.html
 * 
 * 1. Command line arguments.
 * 2. JNDI attributes from java:comp/env.
 * 3. Java System properties (System.getProperties()).
 * 4. OS environment variables.
 * 5. A RandomValuePropertySource that only has properties in random.*.
 * 6. Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants)
 * 7. Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants)
 * 8. Application properties outside of your packaged jar (application.properties and YAML variants).
 * 9. Application properties packaged inside your jar (application.properties and YAML variants).
 * 10. @PropertySource annotations on your @Configuration classes.
 * 11. Default properties (specified using SpringApplication.setDefaultProperties)
 * 
 * Note that Spring Beans in other packages will not be automatically scanned.
 * You'll need to add this command if you have other packages this command will
 * do it \@ComponentScan(basePackages = {"com.ge.package1", "com.ge.package2"})
 * 
 * Or if you want to import Spring XMLs from other dependent projects you can
 * use the @ImportResource, e.g. \@ImportResource( {
 * "classpath*:META-INF/spring/predix-rest-client-scan-context.xml" })
 * 
 * For other Spring Features see:
 * http://docs.spring.io/spring-boot/docs/current/reference/html/
 * 
 * @author predix
 */
@EnableAutoConfiguration(exclude =
{
        // Add any configuration loading call you want to exclude

})
@PropertySource("classpath:application-default.properties")
@ComponentScan(basePackages = "com.ge.predix.solsvc")
@EnableSwagger2
@Controller
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    
    @SuppressWarnings("javadoc")
    @Value("${spring.profiles.active:local}")
    String profile ;
    
    @SuppressWarnings("javadoc")
    @Value("${java.docs.url:null}")
    String docsUrl ;



    /**
     * @param args
     *            -
     */
    @SuppressWarnings(
    {
    })
    public static void main(String[] args)
    {
        SpringApplication springApplication = new SpringApplication(Application.class);
        ApplicationContext ctx = springApplication.run(args);

        log.debug("Let's inspect the beans provided by Spring Boot:"); //$NON-NLS-1$
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames)
        {
            log.debug(beanName);
        }

        log.debug("Let's inspect the profiles provided by Spring Boot:"); //$NON-NLS-1$
        String profiles[] = ctx.getEnvironment().getActiveProfiles();
        for (int i = 0; i < profiles.length; i++)
            log.debug("profile=" + profiles[i]); //$NON-NLS-1$

        log.info("Let's inspect the properties provided by Spring Boot:"); //$NON-NLS-1$
        MutablePropertySources propertySources = ((StandardServletEnvironment) ctx.getEnvironment())
                .getPropertySources();
        Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources.iterator();
        while (iterator.hasNext())
        {
            Object propertySourceObject = iterator.next();
            if ( propertySourceObject instanceof org.springframework.core.env.PropertySource )
            {
                org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
                log.info("propertySource=" + propertySource.getName() + " values=" + propertySource.getSource() //$NON-NLS-1$ //$NON-NLS-2$
                        + "class=" + propertySource.getClass()); //$NON-NLS-1$
            }
        }
    }

    /**
     * @return -
     */
    @Bean
    public Docket documentation()
    {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(paths())
                .build().pathMapping("/") //$NON-NLS-1$
                .apiInfo(metadata());
    }

    /**
     * 
     * @return -
     */

    private Predicate<String> paths()
    {
        return or(regex("/echo/*"), //$NON-NLS-1$
                regex("/health")); //$NON-NLS-1$
        // regex("/info")); //$NON-NLS-1$
    }

    /**
     * @return -
     */
    @Bean
    UiConfiguration uiConfig()
    {
        return new UiConfiguration("validatorUrl", // url //$NON-NLS-1$
                "none", // docExpansion => none | list //$NON-NLS-1$
                "alpha", // apiSorter => alpha //$NON-NLS-1$
                "schema", // defaultModelRendering => schema //$NON-NLS-1$
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, true, // enableJsonEditor
                                                                        // =>
                                                                        // true
                                                                        // |
                                                                        // false
                true); // showRequestHeaders => true | false
    }

    /**
     * Ensure the Tomcat container comes up, not the Jetty one.
     * 
     * @return - the factory
     */
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory()
    {
        return new TomcatEmbeddedServletContainerFactory();
    }

    private ApiInfo metadata()
    {
        return new ApiInfoBuilder().title("Predix Microservice") //$NON-NLS-1$
                .description("Template for predix micro service") //$NON-NLS-1$
                .version("1.1.6") //$NON-NLS-1$
                .build();
    }

    /**
     * @param request -
     * @param name -
     * @param model -
     * @return -
     */
    @RequestMapping("/")
    public String greetings(HttpServletRequest request ,@RequestParam(value = "name", required = false, defaultValue = "Predix") String name,
            Model model)
    {   StringBuffer requesturi = request.getRequestURL();
        String applicationURl = requesturi.toString().replaceAll("http", "https");//$NON-NLS-1$ //$NON-NLS-2$ 
        if("local".equalsIgnoreCase(this.profile)) { //$NON-NLS-1$
            applicationURl = requesturi.toString(); // localhost support for http
        }
        model.addAttribute("api",applicationURl.toString()+"api");//$NON-NLS-1$ //$NON-NLS-2$ 
        model.addAttribute("health",applicationURl.toString()+"health");//$NON-NLS-1$ //$NON-NLS-2$ 
        model.addAttribute("docs",this.docsUrl);//$NON-NLS-1$ 
        return "index"; //$NON-NLS-1$
    }

    /**
     * 
     * @param request
     *            - HttpServletRequest
     * @param response
     *            - HttpServletResponse
     * @return - Model View
     * @throws Exception
     *             - Exception
     */
    @RequestMapping("/docs")
    protected ModelAndView docs(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        return new ModelAndView("redirect:/javadoc/index.html"); //$NON-NLS-1$

    }
    
    
    /**
     * @param request -
     * @param response -
     * @throws IOException -
     */
    @RequestMapping("/api")
    public @ResponseBody void api(HttpServletRequest request ,HttpServletResponse response ) throws IOException
    {   String applicationURl = getApplicationUrl(request);
        response.sendRedirect(applicationURl.replace("/api", "/swagger-ui.html")); //$NON-NLS-1$//$NON-NLS-2$

    }
    
    /**
     * 
     * @param request
     * @return - Application URL
     */
    private String getApplicationUrl (final HttpServletRequest request){
   
        String applicationURl = request.getRequestURL().toString().replaceAll("http", "https");//$NON-NLS-1$ //$NON-NLS-2$ 
        if("local".equalsIgnoreCase(this.profile)) { //$NON-NLS-1$
            applicationURl = request.getRequestURL().toString(); // localhost support for http
        }
        return applicationURl;
    }
    

}

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URL;

public class LoginJDBC {
    public static void main(String[] args) throws Exception {
        final Server server = new DefaultServer().build(3466);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextHandler.setContextPath("/");

        final URL resource = LoginService.class.getResource("/static");
        contextHandler.setBaseResource(Resource.newResource(resource.toExternalForm()));
        contextHandler.setWelcomeFiles(new String[] {"/static/example"});
        contextHandler.addServlet(new ServletHolder("default", DefaultServlet.class), "/*");

        final String jdbcConfig = LoginJDBC.class.getResource("/jdbc_config").toExternalForm();
        final JDBCLoginService jdbcLoginService = new JDBCLoginService("login", jdbcConfig);
        final ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(server,jdbcLoginService);
        securityHandler.setHandler(contextHandler);
        server.setHandler(securityHandler);

        server.start();
    }
}

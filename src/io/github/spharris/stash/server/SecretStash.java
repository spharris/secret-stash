package io.github.spharris.stash.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Server starter for secret-stash 
 */
public final class SecretStash {

  public static void main(String[] args) throws Exception {
    Server server = new Server(3000);
    ServletContextHandler handler = new ServletContextHandler(server, "/");
    
    Injector injector = Guice.createInjector(getModules());
    handler.addEventListener(injector.getInstance(
      GuiceResteasyBootstrapServletContextListener.class));

    ServletHolder sh = new ServletHolder(HttpServletDispatcher.class);
    handler.addServlet(sh, "/*");
    server.setHandler(handler);

    server.start();
    server.join();
  }
  
  private static ImmutableList<Module> getModules() {
    return ImmutableList.of();
  }
}

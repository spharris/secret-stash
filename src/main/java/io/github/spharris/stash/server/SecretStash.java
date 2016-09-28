package io.github.spharris.stash.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import io.github.spharris.stash.service.StashServiceModule;
import io.github.spharris.stash.service.db.DatabaseService;
import io.github.spharris.stash.service.db.StashDatabaseModule;

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

    injector.getInstance(DatabaseService.class).start();
    
    server.start();
    server.join();
  }
  
  private static ImmutableList<Module> getModules() {
    return ImmutableList.of(
      new StashDatabaseModule(),
      new StashServiceModule(),
      new StashServerModule(),
      new AbstractModule() {
        @Override
        protected void configure() {
          ObjectMapper mapper = new ObjectMapper()
              .setSerializationInclusion(Include.NON_NULL)
              .registerModule(new GuavaModule());
          
          bind(JacksonJsonProvider.class).toInstance(new JacksonJsonProvider(mapper));
        }
      });
  }
}

package io.github.spharris.stash.server;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
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
import com.google.inject.Key;
import com.google.inject.Module;

import io.github.spharris.stash.server.Annotations.Port;
import io.github.spharris.stash.service.StashServiceModule;
import io.github.spharris.stash.service.db.DatabaseService;
import io.github.spharris.stash.service.db.StashDatabaseModule;

/**
 * Server starter for secret-stash 
 */
public final class SecretStash {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(getModules(args));
    Server server = new Server(injector.getInstance(Key.get(Integer.class, Port.class)));
    
    HandlerCollection collection = new HandlerCollection();
    collection.addHandler(createApi(server, injector));
    collection.addHandler(createSite(server));
    
    server.setHandler(collection);
    
    server.start();
    server.join();
  }
  
  private static ServletContextHandler createApi(Server server, Injector injector)
      throws Exception {
    ServletContextHandler handler = new ServletContextHandler(server, "/api");
    handler.addEventListener(injector.getInstance(
      GuiceResteasyBootstrapServletContextListener.class));

    ServletHolder sh = new ServletHolder(HttpServletDispatcher.class);
    handler.addServlet(sh, "/*");

    injector.getInstance(DatabaseService.class).start();
    
    return handler;
  }
  
  private static ServletContextHandler createSite(Server server) 
      throws URISyntaxException, IOException {
    ServletContextHandler handler = new ServletContextHandler(server, "/");

    handler.setResourceBase(
      SecretStash.class.getClassLoader().getResource("webapp").toExternalForm());
    handler.addServlet(DefaultServlet.class, "/*");
    
    return handler;
  }
  
  private static ImmutableList<Module> getModules(String[] args) throws ParseException {
    return ImmutableList.of(
      new StashFlagsModule(args),
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

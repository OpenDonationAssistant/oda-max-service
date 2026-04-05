package io.github.opendonationassistant;

import static io.github.opendonationassistant.rabbit.Exchange.Exchange;

import io.github.opendonationassistant.max.commands.Webhook;
import io.github.opendonationassistant.rabbit.AMQPConfiguration;
import io.github.opendonationassistant.rabbit.Exchange;
import io.github.opendonationassistant.rabbit.Queue;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.context.annotation.Factory;
import io.micronaut.openapi.annotation.OpenAPIExclude;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import java.util.List;
import java.util.Map;

@OpenAPIDefinition(info = @Info(title = "oda-max-service"))
@OpenAPIExclude(classes = Webhook.class)
@Factory
public class Application {

  @ContextConfigurer
  public static class DefaultEnvironmentConfigurer
    implements ApplicationContextConfigurer {

    @Override
    public void configure(ApplicationContextBuilder builder) {
      builder.defaultEnvironments("standalone");
    }
  }

  public static void main(String[] args) {
    Micronaut.build(args).mainClass(Application.class).banner(false).start();
  }

  @Bean
  public ChannelInitializer channelInitializer() {
    var eventsQueue = new Queue("max.events");
    return new AMQPConfiguration(
      List.of(
        Exchange(
          Exchange.TWITCH,
          Map.of(
            "event.TwitchStreamStartedEvent",
            eventsQueue,
            "event.TwitchStreamEndedEvent",
            eventsQueue
          )
        )
      )
    );
  }
}

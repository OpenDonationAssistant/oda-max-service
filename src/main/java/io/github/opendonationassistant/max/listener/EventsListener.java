package io.github.opendonationassistant.max.listener;

import io.github.opendonationassistant.events.MessageProcessor;
import io.micronaut.messaging.annotation.MessageHeader;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.rabbitmq.bind.RabbitAcknowledgement;
import jakarta.inject.Inject;

@RabbitListener
public class EventsListener {

  private final MessageProcessor processor;

  @Inject
  public EventsListener(MessageProcessor processor) {
    this.processor = processor;
  }

  @Queue("max.events")
  public void onEvent(
    @MessageHeader("type") String type,
    byte[] event,
    RabbitAcknowledgement ack
  ) {
    processor.process(type, event, ack);
  }
}

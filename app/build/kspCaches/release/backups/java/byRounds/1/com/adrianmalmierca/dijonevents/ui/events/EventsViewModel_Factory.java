package com.adrianmalmierca.dijonevents.ui.events;

import com.adrianmalmierca.dijonevents.data.repository.EventRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class EventsViewModel_Factory implements Factory<EventsViewModel> {
  private final Provider<EventRepository> eventRepositoryProvider;

  public EventsViewModel_Factory(Provider<EventRepository> eventRepositoryProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
  }

  @Override
  public EventsViewModel get() {
    return newInstance(eventRepositoryProvider.get());
  }

  public static EventsViewModel_Factory create(Provider<EventRepository> eventRepositoryProvider) {
    return new EventsViewModel_Factory(eventRepositoryProvider);
  }

  public static EventsViewModel newInstance(EventRepository eventRepository) {
    return new EventsViewModel(eventRepository);
  }
}

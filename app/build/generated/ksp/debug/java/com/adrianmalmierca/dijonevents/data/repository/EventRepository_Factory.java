package com.adrianmalmierca.dijonevents.data.repository;

import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class EventRepository_Factory implements Factory<EventRepository> {
  private final Provider<DijonEventsApi> apiProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public EventRepository_Factory(Provider<DijonEventsApi> apiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.apiProvider = apiProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public EventRepository get() {
    return newInstance(apiProvider.get(), tokenManagerProvider.get());
  }

  public static EventRepository_Factory create(Provider<DijonEventsApi> apiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new EventRepository_Factory(apiProvider, tokenManagerProvider);
  }

  public static EventRepository newInstance(DijonEventsApi api, TokenManager tokenManager) {
    return new EventRepository(api, tokenManager);
  }
}

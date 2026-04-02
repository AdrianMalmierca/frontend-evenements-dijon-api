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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<DijonEventsApi> apiProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public AuthRepository_Factory(Provider<DijonEventsApi> apiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.apiProvider = apiProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiProvider.get(), tokenManagerProvider.get());
  }

  public static AuthRepository_Factory create(Provider<DijonEventsApi> apiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new AuthRepository_Factory(apiProvider, tokenManagerProvider);
  }

  public static AuthRepository newInstance(DijonEventsApi api, TokenManager tokenManager) {
    return new AuthRepository(api, tokenManager);
  }
}

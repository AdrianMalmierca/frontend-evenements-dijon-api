package com.adrianmalmierca.dijonevents;

import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi;
import com.adrianmalmierca.dijonevents.data.repository.TokenManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DijonFirebaseMessagingService_MembersInjector implements MembersInjector<DijonFirebaseMessagingService> {
  private final Provider<DijonEventsApi> apiProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public DijonFirebaseMessagingService_MembersInjector(Provider<DijonEventsApi> apiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.apiProvider = apiProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  public static MembersInjector<DijonFirebaseMessagingService> create(
      Provider<DijonEventsApi> apiProvider, Provider<TokenManager> tokenManagerProvider) {
    return new DijonFirebaseMessagingService_MembersInjector(apiProvider, tokenManagerProvider);
  }

  @Override
  public void injectMembers(DijonFirebaseMessagingService instance) {
    injectApi(instance, apiProvider.get());
    injectTokenManager(instance, tokenManagerProvider.get());
  }

  @InjectedFieldSignature("com.adrianmalmierca.dijonevents.DijonFirebaseMessagingService.api")
  public static void injectApi(DijonFirebaseMessagingService instance, DijonEventsApi api) {
    instance.api = api;
  }

  @InjectedFieldSignature("com.adrianmalmierca.dijonevents.DijonFirebaseMessagingService.tokenManager")
  public static void injectTokenManager(DijonFirebaseMessagingService instance,
      TokenManager tokenManager) {
    instance.tokenManager = tokenManager;
  }
}

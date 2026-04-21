package com.adrianmalmierca.dijonevents.ui.auth;

import com.adrianmalmierca.dijonevents.data.repository.AuthRepository;
import com.adrianmalmierca.dijonevents.data.repository.TokenManager;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), tokenManagerProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, tokenManagerProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      TokenManager tokenManager) {
    return new AuthViewModel(authRepository, tokenManager);
  }
}

package com.adrianmalmierca.dijonevents.di;

import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class AppModule_ProvideApiFactory implements Factory<DijonEventsApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvideApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public DijonEventsApi get() {
    return provideApi(retrofitProvider.get());
  }

  public static AppModule_ProvideApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvideApiFactory(retrofitProvider);
  }

  public static DijonEventsApi provideApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideApi(retrofit));
  }
}

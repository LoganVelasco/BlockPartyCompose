package logan.blockpartycompose.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import logan.blockpartycompose.utils.UserProgressService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

        @Singleton
        @Provides
        fun provideUserProgressService(@ApplicationContext appContext: Context): UserProgressService {
            return UserProgressService(appContext)
        }

}
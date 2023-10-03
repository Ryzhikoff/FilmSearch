package evgeniy.ryzhikov.remote_module

interface RemoteProvider {
    fun provideRemote(): TmdbApi
}
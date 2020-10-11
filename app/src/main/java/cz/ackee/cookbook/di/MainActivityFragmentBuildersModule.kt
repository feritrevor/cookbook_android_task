package cz.ackee.cookbook.di

import cz.ackee.cookbook.screens.add.AddFragment
import cz.ackee.cookbook.screens.detail.DetailFragment
import cz.ackee.cookbook.screens.list.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributeAddFragment(): AddFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
}
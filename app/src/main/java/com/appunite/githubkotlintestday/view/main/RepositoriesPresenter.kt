package com.appunite.githubkotlintestday.view.main

import com.appunite.githubkotlintestday.dao.GithubDao
import com.appunite.rx.ResponseOrError
import com.appunite.rx.dagger.UiScheduler
import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import rx.Scheduler
import rx.subjects.PublishSubject
import javax.inject.Inject

class RepositoriesPresenter @Inject constructor(
        @UiScheduler uiScheduler: Scheduler,
        githubDao: GithubDao
) {
    private val openIssuesForRepository = PublishSubject.create<Any>()

    private val repositoriesObservable = githubDao.getUserRepositoriesObservable()
            .observeOn(uiScheduler)
            .replay(1)
            .refCount()

    val itemsObservable = repositoriesObservable
            .compose(ResponseOrError.onlySuccess())
            .map { repositories -> repositories.mapTo(mutableListOf<BaseAdapterItem>()) { RepositoryAdapterItem(it, openIssuesForRepository) } }

    val errorObservable = repositoriesObservable
            .compose(ResponseOrError.onlyError())
}

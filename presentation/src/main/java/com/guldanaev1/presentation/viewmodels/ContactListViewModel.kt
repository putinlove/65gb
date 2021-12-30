package com.guldanaev1.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guldanaev1.domain.entities.ContactModel
import com.guldanaev1.domain.interactors.ContactListInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactListViewModel @Inject constructor(private val interactor: ContactListInteractor) :
    ViewModel() {
    private val mutableContactList: MutableLiveData<List<ContactModel>> =
        MutableLiveData<List<ContactModel>>()
    val contactList = mutableContactList as LiveData<List<ContactModel>>
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mutableIsLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading = mutableIsLoading as LiveData<Boolean>

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun requestContactList(query: String) {
        disposable.add(Single.fromCallable {
            interactor.loadContacts(query)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mutableIsLoading.postValue(true) }
            .doFinally { mutableIsLoading.postValue(false) }
            .subscribe(
                {
                    mutableContactList.postValue(it)
                }, {
                    mutableIsLoading.postValue(false)
                    Timber.e(it)
                })
        )
    }
}

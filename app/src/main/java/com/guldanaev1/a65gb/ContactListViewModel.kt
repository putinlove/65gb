package com.guldanaev1.a65gb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactProviderRepository = ContactRepository(application)
    private val mutableContactList: MutableLiveData<List<ContactModel>> =
        MutableLiveData<List<ContactModel>>()
    val contactList = mutableContactList as LiveData<List<ContactModel>>
    private var disposable: Disposable? = null
    private val mutableIsLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading = mutableIsLoading as LiveData<Boolean>

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun requestContactList(query: String) {
        disposable?.dispose()
        disposable = (contactProviderRepository.loadContactList(query)
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

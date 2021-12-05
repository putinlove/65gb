package com.guldanaev1.a65gb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val contactProviderRepository = ContactRepository(application)
    private val mutableContactDetails = MutableLiveData<ContactModel>()
    val contactDetails = mutableContactDetails as LiveData<ContactModel>
    private val mutableIsLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var disposable: Disposable? = null
    val isLoading = mutableIsLoading as LiveData<Boolean>

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun requestContactDetails(id: String) {
        disposable?.dispose()
        disposable = (contactProviderRepository.loadContactDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mutableIsLoading.postValue(true) }
            .doFinally { mutableIsLoading.postValue(false) }
            .subscribe(
                {
                    mutableContactDetails.postValue(it)
                }, {
                    mutableIsLoading.postValue(false)
                    Timber.e(it)
                })
        )
    }
}

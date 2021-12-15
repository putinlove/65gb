package com.guldanaev1.a65gb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {
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
        disposable = (repository.loadContactDetails(id)
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

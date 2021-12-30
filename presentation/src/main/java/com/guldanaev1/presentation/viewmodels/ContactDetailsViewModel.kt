package com.guldanaev1.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guldanaev1.domain.entities.ContactModel
import com.guldanaev1.domain.interactors.ContactDetailsInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(private val interactor: ContactDetailsInteractor) :
    ViewModel() {
    private val mutableContactDetails = MutableLiveData<ContactModel>()
    val contactDetails = mutableContactDetails as LiveData<ContactModel>
    private val mutableIsLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()
    val isLoading = mutableIsLoading as LiveData<Boolean>

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun requestContactDetails(id: String) {
        disposable.add(Single.fromCallable {
            interactor.loadContactDetails(id)
        }
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

package cz.ackee.cookbook.repository.common

import androidx.lifecycle.MutableLiveData
import cz.ackee.cookbook.util.api.*
import cz.ackee.cookbook.vo.api.Resource
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

open class RestTask<T>(
        val liveData: MutableLiveData<Resource<ApiResponse<T>>>,
        private val restAction: SupplierWithException<Response<T>>
) : Runnable {

    override fun run() {
        liveData.postValue(Resource.loading(null))
        var apiResponse: ApiResponse<T>
        var response: Response<T>? = null
        var liveDataToPost = Resource.error<ApiResponse<T>>("", null)

        try {
            response = restAction.get()
            apiResponse = ApiResponse.create(response)
            liveDataToPost = when (apiResponse) {
                is ApiSuccessResponse -> Resource.success(onSuccess(apiResponse))
                is ApiEmptyResponse -> Resource.success(onSuccess(apiResponse))
                is ApiErrorResponse -> Resource.error(apiResponse.errorMessage, apiResponse)
            }
        } catch (e: Exception) {
            Timber.e(e, "Caught exception during REST api call!")
            apiResponse = ApiResponse.create(e)
            liveDataToPost = Resource.error(apiResponse.errorMessage, apiResponse)
        }

        try {
            alwaysExecute(response, apiResponse)
        } catch (e: IOException) {
            Timber.e(e, "Caught exception in RestTask alwaysExecute() method!")
        }

        liveData.postValue(liveDataToPost)
    }

    protected open fun onSuccess(apiResponse: ApiSuccessResponse<T>) = apiResponse
    protected open fun onSuccess(apiResponse: ApiEmptyResponse<T>) = apiResponse

    protected open fun alwaysExecute(response: Response<T>?, apiResponse: ApiResponse<T>?) {}
}
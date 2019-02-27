package com.khalifa.locateme.retrofit.repository

import android.content.Context
import com.khalifa.locateme.cloud.payload.MessagePayload
import com.khalifa.locateme.cloud.payload.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.lang.Exception

private const val BASE_URL = "https://fcm.googleapis.com/"

class TrackingRepository(context: Context?) : BaseRemoteRepository(context, BASE_URL) {

    interface LocationService {

        @Headers(
            "Content-Type:application/json",
            "Authorization:key=AAAAvJe-6aY:APA91bGnQmBwwZfhusVxVr4R8Z1iWYLdJBzcvxIpbcBjqBXjzrqVs_Dxt61jL4r0K-YXXLsrJ1Wluaj83QPMFxoQUhrJzOEzh7aoopBfhtgucbdzXfPxziVBmC_ZbNGT9l-0ouec_iSB"
        )

        @POST("fcm/send")
        fun sendNotification(@Body messagePayload: MessagePayload): Call<Response>
    }

    fun sendNotification(messagePayload: MessagePayload,
                         onSuccess: () -> Unit = {},
                         onFailure: (Throwable) -> Unit = {}) {
        try {
            create(LocationService::class.java).sendNotification(messagePayload).enqueue(object : Callback<Response?> {
                override fun onFailure(call: Call<Response?>, t: Throwable) {

                }

                override fun onResponse(call: Call<Response?>, response: retrofit2.Response<Response?>) {
                    if (response.code() == 200) {
                        if (response.body()?.success == 1) {
                            onSuccess()
                        } else {
                            onFailure(Exception())
                        }
                    }
                }

            })
        } catch (throwable: Throwable) {
            onFailure(throwable)
        }
    }
}
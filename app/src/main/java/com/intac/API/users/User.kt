package com.intac.API.users


import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.BaseTypes
import android.util.Log
import com.google.protobuf.Timestamp
import com.intac.authorization.AuthorizeServiceGrpc
import com.intac.authorization.AuthorizationProto
import com.intac.conf
import com.intac.makeposts.PostMakerProto
import com.intac.registration.RegistrationProto
import com.intac.registration.RegistrationServiceGrpc
import io.grpc.okhttp.OkHttpChannelBuilder
import com.intac.PostSession.postSessionsServiceGrpc
import  com.intac.baseTypes.baseTypesProto
import com.intac.PostSession.PostSessionProto
import okio.ByteString
import java.util.logging.Handler
import kotlin.concurrent.thread


class User(
    var id: Int = 0,
    var login: String,
    var pass: String,
    var name: String,
    var surname: String,
    var company: String = "",
    var birth: String
) {
    // Здесь дальше появяться  различные методы работы с классом пользователя
}


fun SingIn(user: User): RegistrationProto.SingUpResponse {


    var host: String = conf.HOST
    var port: Int = conf.PORT

    if (conf.DEBAG) {
        host = conf.DEBAG_HOST
        port = conf.DEBAG_PORT

    }
    var response: RegistrationProto.SingUpResponse =
        RegistrationProto.SingUpResponse.getDefaultInstance()

    val singUpThread: Thread = Thread(Runnable {

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()


        val client = RegistrationServiceGrpc.newBlockingStub(channel)


        val request =
            RegistrationProto.SingUpRequest.newBuilder().setLogin(user.login)
                .setPassword(user.pass.hashCode().toString())
                .setName(user.name).setCompany(user.company).setSurname(user.surname)
                .setBirthDate(Timestamp.getDefaultInstance()).build()


        response = client.singUp(request)
        channel.shutdownNow()

    })

    singUpThread.start()
    singUpThread.join()
    return response

}


fun SingUp(login: String, pass: String): baseTypesProto.UserResponse {

    var response:baseTypesProto.UserResponse =
        baseTypesProto.UserResponse.getDefaultInstance()

    val singUpThread: Thread = Thread(Runnable {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }
        println(host)
        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = AuthorizeServiceGrpc.newBlockingStub(channel)


        val request =
            AuthorizationProto.SingInRequest.newBuilder().setLogin(login)
                .setPassword(pass.hashCode().toString()).build()

        response = client.singIn(request)

        channel.shutdownNow()
    })

    singUpThread.start()
    singUpThread.join()
    return response
}

fun SingUpByToken(
    token: com.google.protobuf.ByteString,
    callback: (baseTypesProto.UserResponse) -> Unit
) {
    var response: baseTypesProto.UserResponse

    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }
        println(host)
        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = AuthorizeServiceGrpc.newBlockingStub(channel)

        val request =
            AuthorizationProto.SingInByTokenRequest.newBuilder().setToken(token)
                .build()

        response = client.singInByToken(request)


        channel.shutdownNow()

        android.os.Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }
    }

}

fun CreateSession(userId: Int, callback: (baseTypesProto.BaseResponse) -> Unit) {


    var response: baseTypesProto.BaseResponse

    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }
        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = postSessionsServiceGrpc.newBlockingStub(channel)

        val request =
            PostSessionProto.CreatePostSessionRequest.newBuilder().setUserId(userId)
                .build()

        response = client.createPostSession(request)


        channel.shutdownNow()

        android.os.Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }
    }


}


fun DropSessionSync(userId: Int) {
    var response: baseTypesProto.BaseResponse

    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }
        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = postSessionsServiceGrpc.newBlockingStub(channel)

        val request =
            PostSessionProto.DropSessionRequest.newBuilder().setUserId(userId)
                .build()

        response = client.dropPostSession(request)


        channel.shutdownNow()
    }.join()
}


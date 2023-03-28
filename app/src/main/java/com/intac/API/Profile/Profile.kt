package com.intac.API.Profile

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.google.protobuf.ByteString
import com.intac.API.posts.getByteArrFromPhoto
import com.intac.baseTypes.baseTypesProto.BaseResponse
import com.intac.baseTypes.baseTypesProto.UserResponse
import com.intac.conf
import com.intac.makeposts.PostMakerProto
import com.intac.makeposts.postGetterGrpc
import com.intac.user_get.userGetterGrpc
import  com.intac.profile.profileProto
import  com.intac.profile.userUpdateGrpc
import  com.intac.user_get.UserGetProto
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlin.concurrent.thread



class Profile() {
    // хз, надо ли будет выделять объект профиля как что-то отдельное
    // но хуже не будет
}

fun GetUserPosts(
    user_id: Int,
    limit: Int,
    lastPostId: Int,
    callback: (PostMakerProto.GetPostPaginatedResponse) -> Unit
) {
    var response: PostMakerProto.GetPostPaginatedResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = postGetterGrpc.newBlockingStub(channel)

        val request =
            PostMakerProto.GetUserPostRequest.newBuilder().setUserId(user_id).setPostId(lastPostId)
                .setLimit(limit)
                .build()

        response = client.getUserPosts(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun GetUserPostsSync(
    user_id: Int,
    limit: Int,
    lastPostId: Int
): PostMakerProto.GetPostPaginatedResponse {

    var response: PostMakerProto.GetPostPaginatedResponse =
        PostMakerProto.GetPostPaginatedResponse.getDefaultInstance()


    var host: String = conf.HOST
    var port: Int = conf.PORT

    if (conf.DEBAG) {
        host = conf.DEBAG_HOST
        port = conf.DEBAG_PORT

    }

    val channel =
        OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

    val client = postGetterGrpc.newBlockingStub(channel)

    val request =
        PostMakerProto.GetUserPostRequest.newBuilder().setUserId(user_id).setPostId(lastPostId)
            .setLimit(limit)
            .build()
    response = client.getUserPosts(request)
    channel.shutdownNow()




    return response


}

fun GetUserById(user_id: Int, callback: (UserResponse) -> Unit) {
    var response:UserResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userGetterGrpc.newBlockingStub(channel)

        val request =UserGetProto.GetUserRequest.newBuilder().setUserId(user_id).build()


        response = client.getUserById(request)
        channel.shutdownNow()


        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}


fun UpdateAbout(
    user_id: Int,
    about: String,
    callback: (BaseResponse) -> Unit
) {
    var response:BaseResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdateGrpc.newBlockingStub(channel)

        val request =
            profileProto.UpdateAboutRequest.newBuilder().setUserId(user_id).setAbout(about)
                .build()

        response = client.updateAbout(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdateName(user_id: Int, name: String, callback: (BaseResponse) -> Unit) {
    var response: BaseResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdateGrpc.newBlockingStub(channel)

        val request =
            profileProto.UpdateNameRequest.newBuilder().setUserId(user_id).setName(name)
                .build()

        response = client.updateName(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdateLogin(
    user_id: Int,
    login: String,
    callback: (BaseResponse) -> Unit
) {
    var response: BaseResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdateGrpc.newBlockingStub(channel)

        val request =
            profileProto.UpdateLoginRequest.newBuilder().setUserId(user_id).setLogin(login)
                .build()

        response = client.updateLogin(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdatePassword(
    user_id: Int,
    password: String,
    callback: (BaseResponse) -> Unit
) {
    var response: BaseResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdateGrpc.newBlockingStub(channel)

        val request =
            profileProto.UpdatePasswordRequest.newBuilder().setUserId(user_id)
                .setPassword(password.hashCode().toString())
                .build()

        response = client.updatePassword(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdatePhoto(
    user_id: Int,
    photo: Bitmap,
    callback: (BaseResponse) -> Unit
) {
    var response: BaseResponse

    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdateGrpc.newBlockingStub(channel)

        val request =
            profileProto.UpdatePhotoRequest.newBuilder().setUserId(user_id)
                .setPhotoBytes(ByteString.copyFrom(getByteArrFromPhoto(photo)))
                .build()

        response = client.updatePhoto(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}
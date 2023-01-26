package com.intac.API.Profile

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.google.protobuf.ByteString
import com.intac.API.posts.getByteArrFromPhoto
import com.intac.conf
import com.intac.makeposts.PostMakerProto
import com.intac.makeposts.postGetterGrpc
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlin.concurrent.thread
import com.intac.profile.ProfileProto
import com.intac.profile.userGetterGrpc
import com.intac.profile.userUpdaterGrpc

class Profile() {
    // хз, надо ли будет выделять объект профиля как что-то отдельное
    // но хуже не будет
}

fun GetUserPosts(
    user_id: Long,
    limit: Long,
    lastPostId: Long,
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
    user_id: Long,
    limit: Long,
    lastPostId: Long
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

fun GetUserById(user_id: Long, callback: (ProfileProto.GetUserResponse) -> Unit) {
    var response: ProfileProto.GetUserResponse
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

        val request =
            ProfileProto.GetUserRequest.newBuilder().setUserId(user_id)
                .build()

        response = client.getUserById(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}


fun UpdateAbout(
    user_id: Long,
    about: String,
    callback: (ProfileProto.UpdateAboutResponse) -> Unit
) {
    var response: ProfileProto.UpdateAboutResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdaterGrpc.newBlockingStub(channel)

        val request =
            ProfileProto.UpdateAboutRequest.newBuilder().setUserId(user_id).setAbout(about)
                .build()

        response = client.updateAbout(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdateName(user_id: Long, name: String, callback: (ProfileProto.UpdateNameResponse) -> Unit) {
    var response: ProfileProto.UpdateNameResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdaterGrpc.newBlockingStub(channel)

        val request =
            ProfileProto.UpdateNameRequest.newBuilder().setUserId(user_id).setName(name)
                .build()

        response = client.updateName(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdateLogin(
    user_id: Long,
    login: String,
    callback: (ProfileProto.UpdateLoginResponse) -> Unit
) {
    var response: ProfileProto.UpdateLoginResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdaterGrpc.newBlockingStub(channel)

        val request =
            ProfileProto.UpdateLoginRequest.newBuilder().setUserId(user_id).setLogin(login)
                .build()

        response = client.updateLogin(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}

fun UpdatePassword(
    user_id: Long,
    password: String,
    callback: (ProfileProto.UpdatePasswordResponse) -> Unit
) {
    var response: ProfileProto.UpdatePasswordResponse
    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdaterGrpc.newBlockingStub(channel)

        val request =
            ProfileProto.UpdatePasswordRequest.newBuilder().setUserId(user_id)
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
    user_id: Long,
    photo: Bitmap,
    callback: (ProfileProto.UpdatePhotoResponse) -> Unit
) {
    var response: ProfileProto.UpdatePhotoResponse

    thread {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = userUpdaterGrpc.newBlockingStub(channel)

        val request =
            ProfileProto.UpdatePhotoRequest.newBuilder().setUserId(user_id)
                .setPhotoBytes(ByteString.copyFrom(getByteArrFromPhoto(photo)))
                .build()

        response = client.updatePhoto(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }
}
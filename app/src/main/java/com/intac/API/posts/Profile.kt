package com.intac.API.posts

import android.os.Handler
import android.os.Looper
import com.intac.conf
import com.intac.makeposts.PostMakerProto
import com.intac.makeposts.postGetterGrpc
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlin.concurrent.thread

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
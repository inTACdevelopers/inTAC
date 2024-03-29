package com.intac.API.posts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.View
import com.intac.makeposts.PostMakerProto
import com.intac.makeposts.postGetterGrpc
import com.intac.makeposts.postMakerGrpc

import com.google.protobuf.ByteString
import com.intac.makeposts.LikePostGrpc
import com.intac.conf
import io.grpc.okhttp.OkHttpChannelBuilder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.concurrent.thread

class Post(
    var id: Int = 0,
    var title: String,
    var description: String,
    var sellerContact: String,
    var photoBitmap: Bitmap,
    var from_user: Int,
    var creation_time: String = "",
    var likes: Int = 0,
    var was_like: Int = 0,
    var like: Boolean = false,
    var weight: Double = 0.0
) {
    fun Like(userId: Int, callback: (PostMakerProto.LikePostResponse) -> Unit) {
        var response: PostMakerProto.LikePostResponse

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

            val client = LikePostGrpc.newBlockingStub(channel)

            val request =
                PostMakerProto.LikePostRequest.newBuilder().setPostId(this.id).setFromUser(userId)
                    .build()

            response = client.sendLike(request)

            Handler(Looper.getMainLooper()).post {
                callback.invoke(response)
            }

        }
    }

    fun UnLike(userId: Int, callback: (PostMakerProto.UnLikePostResponse) -> Unit) {
        var response: PostMakerProto.UnLikePostResponse

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

            val client = LikePostGrpc.newBlockingStub(channel)

            val request =
                PostMakerProto.UnLikePostRequest.newBuilder().setPostId(this.id).setFromUser(userId)
                    .build()

            response = client.unLike(request)

            Handler(Looper.getMainLooper()).post {
                callback.invoke(response)
            }

        }
    }
}

fun makePost(post: Post): PostMakerProto.makePostResponse {
    // Функция создания нового поста
    // На вход принимает объект типа Post
    // Возвращает makePostResponse
    // Выходные параметры makePostResponse
    //
    // int32 code - выходной код функции
    // 0 - Всё хорошо, ошибок нет (state = OK)
    // 1 - Ошибка в файловой системе сервера, пользователю надо сказать попробовать ещё раз (state = File Exists error (#db))
    // 2 - Серверная ошибка при работе с базой (state = Server Error (#db))
    //


    var response: PostMakerProto.makePostResponse =
        PostMakerProto.makePostResponse.getDefaultInstance()

    val makePostThread: Thread = Thread(Runnable {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = postMakerGrpc.newBlockingStub(channel)

        val request = PostMakerProto.makePostRequest.newBuilder()
            .setPhotoBytes(ByteString.copyFrom(getByteArrFromPhoto(post.photoBitmap)))
            .setFileName("TEST")
            .setPostTitle(post.title).setPostDescription(post.description)
            .setSellerContact(post.sellerContact).setUserId(post.from_user)
            .build()

        response = client.makePost(request)

        channel.shutdownNow()
    })

    makePostThread.start()
    makePostThread.join()

    return response
}

fun getPost(post_id: Int): PostMakerProto.GetPostResponse {

    // Функция получения поста из базы данных (пока не нужна, поэтому чуть недоделана)
    // На вход принимает id поста, который нужно получится
    // Возвращает GetPostResponse
    // Выходные параметры GetPostResponse
    // int32 code
    // 0 - Всё хорошо, ошибок нет (state = OK)
    // 1 - Ошибка на сервере: Скорее всего пользователя не существует (state = Error (#server))
    //
    // string state - текстовое пояснение к code
    //
    // bytes photo_bytes = 3; Будет декодироваться в BitMap, который может отрисовать андройд
    // string post_title = 4;
    // string post_description = 5;
    // string seller_contact = 6;
    // string creation_time = 7;

    var response: PostMakerProto.GetPostResponse =
        PostMakerProto.GetPostResponse.getDefaultInstance()
    val postGetterThread: Thread = Thread(Runnable {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val clinet = postGetterGrpc.newBlockingStub(channel)

        val request = PostMakerProto.GetPostRequest.newBuilder().setPostId(post_id).build()
        response = clinet.getPost(request)

    })
    postGetterThread.start()
    postGetterThread.join()
    return response
}

fun getByteArrFromPhoto(bitmap: Bitmap): ByteArray {

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)

    return stream.toByteArray()
}

fun getPostPaginated(
    post_id: Int,
    limit: Int,
    session_name: String,
    user_id: Int,
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
            PostMakerProto.GetPostRequest.newBuilder().setPostId(post_id)
                .setSessionName(session_name).setLimit(limit).setUserId(user_id).build()
        response = client.getPostPaginated(request)
        channel.shutdownNow()

        Handler(Looper.getMainLooper()).post {

            callback.invoke(response)
        }

    }

}

fun getPostPaginatedSync(
    post_id: Int,
    limit: Int,
    session_name: String,
    user_id: Int
): PostMakerProto.GetPostPaginatedResponse {

    val response: PostMakerProto.GetPostPaginatedResponse


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
        PostMakerProto.GetPostRequest.newBuilder().setSessionName(session_name).setUserId(user_id)
            .setPostId(post_id)
            .setLimit(limit).build()
    response = client.getPostPaginated(request)
    channel.shutdownNow()




    return response


}


fun PhotoDecoder(bytes: ByteString?): Bitmap {
    val stream: InputStream = ByteArrayInputStream(bytes!!.toByteArray())
    return BitmapFactory.decodeStream(stream)

}



fun makeListFromPaginationResponse(response: PostMakerProto.GetPostPaginatedResponse): ArrayList<Post> {
    val out_list = ArrayList<Post>()

    for (item in response.postsList) {
        val post = Post(
            item.postId, item.postTitle, item.postDescription, item.sellerContact,
            PhotoDecoder(item.photoBytes), item.userId, item.creationTime
        )
        post.like = item.like
        out_list.add(post)
    }
    return out_list
}







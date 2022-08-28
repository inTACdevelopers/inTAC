package com.intac.API.posts

import android.graphics.Bitmap
import android.net.Uri
import com.example.makeposts.PostMakerProto
import com.example.makeposts.postGetterGrpc
import com.example.makeposts.postMakerGrpc
import com.google.protobuf.ByteString
import com.intac.API.users.User
import com.intac.CreatePost
import com.intac.conf
import io.grpc.okhttp.OkHttpChannelBuilder
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor

class Post(
    var id: Int = 0,
    var title: String,
    var descriptor: String,
    var sellerContact: String,
    var photoBitmap: Bitmap,
    var from_user: User,
    var creation_time: String
) {
// Здесь
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
            .setPostTitle(post.title).setPostDescription(post.descriptor)
            .setSellerContact(post.sellerContact).setUserId(post.from_user.id.toLong())
            .build()

        response = client.makePost(request)

        channel.shutdownNow()
    })

    makePostThread.start()
    makePostThread.join()

    return response
}

fun getPost(post_id: Long): PostMakerProto.GetPostResponse {

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


// Эта функция тебе не нужна
fun getByteArrFromPhoto(bitmap: Bitmap): ByteArray {

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)

    return stream.toByteArray()
}
package com.intac.users


import authorization.Authorization
import authorization.authorizerGrpc
import com.example.registration.RegistrarProto
import com.example.registration.registrarGrpc
import  com.intac.conf
import io.grpc.okhttp.OkHttpChannelBuilder

// Класс описываюсщий сущность пользователя
// Company - не обязательный параметр
class User(
    val id: Int = 0,
    val login: String,
    val pass: String,
    val name: String,
    val surname: String,
    val company: String = ""
) {
    // Здесь дальше появяться  различные методы работы с классом пользователя
}
// Принимает объект User с заполнеными полями (кроме id)
// Возращает объект singInResponse с параметрами
//  int code = 1
// string state = 2;
//
// Параметр state:
// - Текстовое значение для code
// Параметр code:
// 0 - всё хорошо (state = OK)
// 1 - Ошибка существования пользователя с таким логином (state = User already exist)
// 2 - Ошибка существования комании с таким названием (state = Company already exist)
// 3 - Ошибка при работе с базой данных (state = Server Error (#db))

fun SingIn(user: User): RegistrarProto.SingInResponse {


    var host: String = conf.HOST
    var port: Int = conf.PORT

    if (conf.DEBAG) {
        host = conf.DEBAG_HOST
        port = conf.DEBAG_PORT

    }
    var response: RegistrarProto.SingInResponse = RegistrarProto.SingInResponse.getDefaultInstance()

    val singUpThread: Thread = Thread(Runnable {

        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = registrarGrpc.newBlockingStub(channel)


        val request =
            RegistrarProto.SingInRequest.newBuilder().setLogin(user.login).setPassword(user.pass)
                .setName(user.name).setCompany(user.company).setSurname(user.surname).build()


        client.singIn(request).also { response = it }


    })

    singUpThread.start()
    singUpThread.join()
    return response

}

// Возращает объект SingUpResponse с параметрами
//  int code = 1
//  string state = 2;
//  int32 user_type = 3;
//
//  int32 id = 4;
//  string login = 5;
//  string password = 6;
//  string name = 7;
//  string surname = 8;
//  string company = 9;
//
// Параметр state:
// - Текстовое значение для code
// Параметр code:
// 0 - всё хорошо (state = OK)
// 1 - Ошибка, пользователя не существует (state = No such user)
//
// Параметр user_type:
// Говорит о типе пользователя
// 0 - покупаетль
// 1 - продавец (при регистрации было указано поле company)
//
// Все остальные параметры характеризуют сущность пользователя

fun SingUp(login: String, pass: String): Authorization.SingUpResponse {
    var response: Authorization.SingUpResponse = Authorization.SingUpResponse.getDefaultInstance()

    val singUpThread: Thread = Thread(Runnable {
        var host: String = conf.HOST
        var port: Int = conf.PORT

        if (conf.DEBAG) {
            host = conf.DEBAG_HOST
            port = conf.DEBAG_PORT

        }
        val channel =
            OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build()

        val client = authorizerGrpc.newBlockingStub(channel)


        val request =
            Authorization.SingUpRequest.newBuilder().setLogin(login).setPassword(pass).build()

        response = client.singUp(request)


    })

    singUpThread.start()
    singUpThread.join()
    return response
}
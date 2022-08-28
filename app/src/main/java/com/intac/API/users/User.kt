package com.intac.API.users


import com.example.authorization.authorizerGrpc
import com.example.authorization.AuthorizerProto
import com.example.registration.RegistrarProto
import com.example.registration.registrarGrpc
import  com.intac.conf
import io.grpc.okhttp.OkHttpChannelBuilder

// Класс описываюсщий сущность пользователя
// Company - не обязательный параметр
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


fun SingIn(user: User): RegistrarProto.SingInResponse {

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
            RegistrarProto.SingInRequest.newBuilder().setLogin(user.login)
                .setPassword(user.pass.hashCode().toString())
                .setName(user.name).setCompany(user.company).setSurname(user.surname)
                .setBirthDate(user.birth).build()


        client.singIn(request).also { response = it }
        channel.shutdownNow()

    })

    singUpThread.start()
    singUpThread.join()
    return response

}


fun SingUp(login: String, pass: String): AuthorizerProto.SingUpResponse {
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
    var response: AuthorizerProto.SingUpResponse =
        AuthorizerProto.SingUpResponse.getDefaultInstance()

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
            AuthorizerProto.SingUpRequest.newBuilder().setLogin(login)
                .setPassword(pass.hashCode().toString()).build()

        response = client.singUp(request)

        channel.shutdownNow()
    })

    singUpThread.start()
    singUpThread.join()
    return response
}
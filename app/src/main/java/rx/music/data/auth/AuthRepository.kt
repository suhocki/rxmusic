package rx.music.data.auth

import io.reactivex.Completable
import io.reactivex.Single
import rx.music.net.models.Credentials

/** Created by Maksim Sukhotski on 3/27/2017.*/
interface AuthRepository {
    fun getCredentials(username: String,
                       password: String,
                       captchaSid: String?,
                       captchaKey: String?,
                       code: Int?): Single<Credentials>

    fun validatePhone(sid: String): Completable
}
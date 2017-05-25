package rx.music.net.models.vk

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/** Created by Maksim Sukhotski on 4/9/2017. */

@RealmClass
open class User(@PrimaryKey var id: Long = 0,
        //                var audioList: RealmList<Audio> = RealmList(),
                var sex: Int = 0,
                var online: Int = 0,
                @SerializedName("screen_name") var screenName: String = "",
                @SerializedName("first_name") var firstName: String = "",
                @SerializedName("first_name_gen") var firstNameGen: String = "",
                @SerializedName("last_name") var lastName: String = "",
                @SerializedName("photo_50") var photo50: String = "",
                @SerializedName("photo_100") var photo100: String = "",
                @SerializedName("photo_200") var photo200: String = "") : RealmObject()
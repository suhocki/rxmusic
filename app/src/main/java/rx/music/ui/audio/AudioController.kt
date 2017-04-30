package rx.music.ui.audio

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import kotlinx.android.synthetic.main.controller_audio.view.*
import rx.music.App
import rx.music.R
import rx.music.data.net.models.AudioResponse
import rx.music.ui.auth.AuthController
import rx.music.ui.base.MoxyController


/** Created by Maksim Sukhotski on 4/8/2017. */
class AudioController : MoxyController(), AudioView {

    @InjectPresenter
    lateinit var presenter: AudioPresenter

    private var offset: Int = 0
    private var adapter: AudioAdapter = AudioAdapter(onClick = { presenter.playAudio(it) })

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_audio, container, false)
    }

    override fun onViewBound(view: View) {
        with(view) {
            val linearLayoutManager = LinearLayoutManager(activity)
            audioRecyclerView.setHasFixedSize(true)
            audioRecyclerView.layoutManager = linearLayoutManager
            audioRecyclerView.adapter = adapter
            audioRecyclerView.addOnScrollListener(InfiniteScrollListener({
                offset += 30
                presenter.getAudio(offset = offset.toString())
            }, linearLayoutManager))
        }
    }

    override fun showAudio(audioResponse: AudioResponse) {
        adapter.items.addAll(audioResponse.items)
        adapter.notifyDataSetChanged()
    }

    override fun showAuthController() {
        router.setRoot(RouterTransaction.with(AuthController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        App.instance.userComponent = null
    }
}
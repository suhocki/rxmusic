package rx.music.ui.auth

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.controller_auth.view.*
import kotlinx.android.synthetic.main.part_captcha.view.*
import kotlinx.android.synthetic.main.part_login.view.*
import kotlinx.android.synthetic.main.part_validation.view.*
import me.ext.hideKeyboard
import me.ext.onClick
import rx.music.R
import rx.music.data.net.models.Captcha
import rx.music.data.net.models.Validation
import rx.music.ui.audio.AudioController
import rx.music.ui.base.MoxyController


class AuthController : MoxyController(), AuthView {

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_auth, container, false)
    }

    override fun onViewBound(view: View) {
        view.loginButton.onClick {
            presenter.login(view.usernameEditText.text.toString(),
                    view.passwordEditText.text.toString())
        }
    }

    override fun showSnackbar(text: String) {
        with(view!!) {
            Snackbar.make(loginLayout, text, Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    override fun showLogin(string: Any) {
        with(view!!) {
            flipLayout.showView(flipLayout.loginView!!)
            showSnackbar(string as String)
        }
    }

    override fun showCaptcha(captcha: Captcha) {
        with(view!!) {
            flipLayout.showView(flipLayout.captchaView!!)
            Glide.with(activity!!)
                    .load(captcha.captcha_img)
                    .error(R.drawable.oh)
                    .into(captchaImageView)
            loginButton.onClick {
                presenter.login(usernameEditText.text.toString(),
                        passwordEditText.text.toString(),
                        captcha.captcha_sid,
                        captchaEditText.text.toString())
                captchaEditText.text.clear()
            }
        }
    }

    override fun showValidation(validation: Validation) {
        with(view!!) {
            flipLayout.showView(flipLayout.validationView!!)
            validationTextView.text = context.getString(R.string.code_sent, validation.phone_mask)
            loginButton.onClick {
                presenter.login(usernameEditText.text.toString(),
                        passwordEditText.text.toString(),
                        code = validationEditText.text.toString())
            }
        }
    }

    override fun showNextController() {
        view?.hideKeyboard()
        router.setRoot(RouterTransaction.with(AudioController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }
}
const MODAL_LOGIN_WINDOW_SELECTOR = ".login-modal";

const MODAL_SIGN_UP_WINDOW_SELECTOR = ".sign-up-modal";

class UserBarView {
    constructor(userModel) {
        this.userModel = userModel;
    }

    loginForm = {
        login: "",
        password: "",
    }

    signUpForm = {
        login: "",
        password: ""
    }

    init() {
        $(".dropdown-button").click(event => {
            $(".dropdown-content").toggleClass("hide");
        })
        $('.log-out-link').click(event => {
            this.userModel.signOut();
        })
        $(".sign-in-link").click(event => {
            $(MODAL_LOGIN_WINDOW_SELECTOR).removeClass("hide");
        })
        $(".sign-up-link").click(event => {
            $(MODAL_SIGN_UP_WINDOW_SELECTOR).removeClass("hide");
        })

        $(".login-form .login-button").click(async event => {

            try {
                await this.userModel.signIn(this.loginForm);
                $(".error").text("");
                $(MODAL_LOGIN_WINDOW_SELECTOR).addClass("hide");
            } catch (e) {
                console.log(e);
            }
        })
        $(".login-form .sign-up-button").click(event => {
            $(MODAL_LOGIN_WINDOW_SELECTOR).addClass("hide");
            $(MODAL_SIGN_UP_WINDOW_SELECTOR).removeClass("hide");
        })

        $(".login-form .login-field .login-input").on('input', event => {
            this.loginForm.login = event.target.value;
        })
        $(".login-form .password-field .password-input").on('input', event => {
            this.loginForm.password = event.target.value;
        })

        $(".sign-up-form .login-field .login-input").on("input", event => {
            this.signUpForm.login = event.target.value;
        })
        $(".sign-up-form .password-field .password-input").on('input', event => {
            this.signUpForm.password = event.target.value;
        })
        $(".sign-up-form .sign-up-button").click(async event => {
            try {
                await this.userModel.signUp(this.signUpForm);
                $(".error").text("");
                $(MODAL_SIGN_UP_WINDOW_SELECTOR).addClass('hide');
            } catch (e) {
                console.log(e);
            }

        })

        this.userModel.onUserChanged.subscribe(() => {
            this.renderBar();
        })
        this.userModel.onErrorCatched.subscribe((action, status, error) => {
            let $form;
            if (action.description === UserModel.ACTIONS.SIGN_UP.description) {
                $form = $(".sign-up-form")
            } else if (action.description === UserModel.ACTIONS.SIGN_IN.description) {
                $form = $(".login-form")
            }
            $(".error").text("");
            if (status === 401) {
                $form.find(".auth-error").text(error.message);
            } else {
                for (const violation of error.violations) {
                    if (violation.fieldName.includes('login')) {
                        $form.find(".login-error").text(violation.message)
                    }
                    if (violation.fieldName.includes('password')) {
                        $form.find(".password-error").text(violation.message)
                    }
                }
            }
        })
        this.renderBar()
    }

    renderBar() {
        if (_.isEqual(this.userModel.user, UserModel.USERS.NO_USER)) {
            this.hideProfile()
        } else {
            this.setProfile(this.userModel.user);
            this.showProfile();
        }
    }

    hideProfile() {
        $(".header-option.login,.header-option.sign-up").removeClass("hide");
        $(".header-option.profile").addClass("hide");
    }

    showProfile() {
        $(".header-option.login,.header-option.sign-up").addClass("hide");
        $(".header-option.profile").removeClass("hide");
    }

    setProfile(user) {
        $(".profile-picture img").attr('src',`/static/images/avatars/${user.picture}`);
        $(".header-option.profile .user-name").text(user.login);
    }
}
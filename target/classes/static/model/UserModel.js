//TODO token stuff + server logout - TBD ON SPRING
class UserModel {
    static get USERS() {
        return Object.freeze({
            NO_USER: {
                login: 'anonymous',
                role: 'no'
            }
        })
    }

    static get ACTIONS() {
        return Object.freeze({
            SIGN_IN: Symbol('signIn'),
            SIGN_UP: Symbol('signUp'),
            SIGN_OUT: Symbol('signOut'),
            USER_GET: Symbol("userGet")
        })
    }

    constructor() {
        this.user = UserModel.USERS.NO_USER;

        this.onUserChanged = new EventEmitter();
        this.onErrorCatched = new EventEmitter();
    }

    init() {
        this.getUser();
    }

    async getUser() {
        try {
            await $.ajax({
                url: '/api/auth',
                method: 'GET',
                success: (response, textStatus, xhr) => {
                    console.log("succ")
                    console.log(response)
                    this.user = response;
                    this.onUserChanged.notify(UserModel.ACTIONS.USER_GET)
                },
                error: (xhr, ajaxOptions, thrownError) => {
                    console.log("fail")
                    console.log(JSON.parse(xhr.responseText))
                    this.user = UserModel.USERS.NO_USER;
                    this.onUserChanged.notify(UserModel.ACTIONS.USER_GET)
                }
            })
        } catch (err) {
            console.log(err);
        }
    }

    async signIn(loginForm) {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

        await $.ajax({
            url: '/api/auth',
            method: 'POST',
            data: JSON.stringify(loginForm),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            headers: {'X-XSRF-TOKEN': csrfToken},
            success: (response, textStatus, xhr) => {
                console.log("succ")
                console.log(response)
                this.user = response;
                this.onUserChanged.notify(UserModel.ACTIONS.SIGN_IN)
            },
            error: (xhr, ajaxOptions, thrownError) => {
                console.log("fail")
                console.log(xhr.status);
                const error = JSON.parse(xhr.responseText);
                console.log(error);
                this.onErrorCatched.notify(UserModel.ACTIONS.SIGN_IN, xhr.status, error);
            }
        })

    }

    async signOut() {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

        await $.ajax({
            url: '/api/auth/',
            method: 'DELETE',
            headers: {'X-XSRF-TOKEN': csrfToken},
            success: (response, textStatus, xhr) => {
                console.log("succ")
                console.log(response)
                this.user = UserModel.USERS.NO_USER;
                this.onUserChanged.notify('logout');
            },
            error: (xhr, ajaxOptions, thrownError) => {
                console.log("fail")
                this.user = UserModel.USERS.NO_USER;
                this.onUserChanged.notify('logout');
            }
        })

    }

    async signUp(signUpForm) {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

        await $.ajax({
            url: '/api/auth/create',
            method: 'POST',
            data: JSON.stringify(signUpForm),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            headers: {'X-XSRF-TOKEN': csrfToken},
            success: (response, textStatus, xhr) => {
                console.log("succ")
                console.log(response)
                this.user = response === undefined ? UserModel.USERS.NO_USER : response;
                this.onUserChanged.notify('signup')
            },
            error: (xhr, ajaxOption, thrownError) => {
                console.log("fail")
                console.log(xhr.status);
                const error = JSON.parse(xhr.responseText);
                console.log(error);
                this.onErrorCatched.notify(UserModel.ACTIONS.SIGN_UP, xhr.status, error);
            }
        })
    }

}
class CaptchaModel {
    static get ACTIONS() {
        return Object.freeze({
            CAPTCHA_CREATE: Symbol('CAPTCHA_CREATE'),
            CAPTCHA_EDIT: Symbol('CAPTCHA_EDIT'),
            TAG_CREATE: Symbol('TAG_CREATE')
        });
    }

    constructor() {
        this.callbackFactory = new CallbackFactory();

        this.captchas = [];

        this.onCaptchasChanged = new EventEmitter();

    }

    init() {
        // init something?
    }

    async updateCaptcha(captcha) {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
        await $.ajax({
            url: `/api/captchas/${captcha.name}`,
            method: 'PUT',
            data: JSON.stringify(captcha),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            headers: { 'X-XSRF-TOKEN': csrfToken },
            success: (response, textStatus, xhr) => {
                this.captchas[this.captchas.indexOf(captcha)] = response;
                this.onCaptchasChanged.notify(response, CaptchaModel.ACTIONS.CAPTCHA_EDIT);
            }
        })
    }

    async addCaptcha(addCaptchaForm) {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
        const formData = new FormData();

        formData.append("picture", addCaptchaForm.imageFile, "picture");
        formData.append("user", addCaptchaForm.user);
        await $.ajax({
            url: '/api/captchas',
            method: 'POST',
            // TODO uncomment when tokens added
            // data: addCaptchaForm.imageFile,
            data: formData,
            contentType: false,
            processData: false,
            headers: { 'X-XSRF-TOKEN': csrfToken },
            success: (response, textStatus, xhr) => {
                if (response !== undefined) {
                    this.captchas.push(response);
                    this.onCaptchasChanged.notify(response, CaptchaModel.ACTIONS.CAPTCHA_CREATE);
                }
            }
        })
    }

    async requestCaptchas(filter, tag, name, user = undefined) {
        const params = new URLSearchParams({
            filter, name
        });
        if (tag !== undefined) {
            params.append("tag", tag);
        }
        if (user !== undefined) {
            params.append("user", user);
        }
        const promise = this.callbackFactory.getPromise();

        await $.ajax({
            url: `/api/captchas/?${params}`,
            method: 'GET',
            dataType: 'json',
            success: async (response, textStatus, xhr) => {
                await promise(response => this.captchas = response)
                    .then(func => func.call(this, response), error => console.log(error));
            }
        })
    }
}
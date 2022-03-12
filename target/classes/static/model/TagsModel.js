class TagsModel {
    constructor() {
        this.tags = [];

        this.onTagsChanged = new EventEmitter();
    }
    init () {

    }

    async requestTags() {
        await $.ajax({
            url: '/api/captchas/tags',
            method: 'GET',
            success: async (response, textStatus, xhr) => {
                this.tags = response;
            }
        })
    }

    async addTag(tag) {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
        await $.ajax({
            url: '/api/captchas/tags',
            method: 'POST',
            data: JSON.stringify({name: tag}),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            headers: { 'X-XSRF-TOKEN': csrfToken },
            success: (response, textStatus, xhr) => {
                this.onTagsChanged.notify(tag, CaptchaModel.ACTIONS.TAG_CREATE);
            }
        })
    }

}
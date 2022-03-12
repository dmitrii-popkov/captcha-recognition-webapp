class HistoryScreenView {
    constructor(historyModel, userModel, captchaModel, tagsModel) {
        this.historyModel = historyModel;
        this.userModel = userModel;
        this.captchaModel = captchaModel;
        this.tagsModel = tagsModel;
        this.historyNoteService = new HistoryNoteService();
        this.captchaService = new CaptchaService();
    }

    init() {
        this.historyModel.onNotesChanged.subscribe(() => {
            this.renderNotes();
        })
        this.userModel.onUserChanged.subscribe(() => {
            this.renderNotes();
        })
        this.captchaModel.onCaptchasChanged.subscribe(async () => {
            await this.historyModel.getHistoryNotes(this.userModel.user);
            this.renderNotes();
        });
        this.tagsModel.onTagsChanged.subscribe(async () => {
            await this.historyModel.getHistoryNotes(this.userModel.user);
            this.renderNotes();
        });
        this.renderNotes();
    }


    async renderNotes() {
        const user = this.userModel.user.login

        await this.historyModel.getHistoryNotes(user);
        this.historyModel.historyNotes.sort((a, b) => new Date(b.stamp) - new Date(a.stamp))

        this.insertNotes($(".main.history-content"), this.historyModel.historyNotes);
    }

    insertNotes($container, historyNotes) {
        $container.empty();
        for (const historyNote of historyNotes) {
            $container.append(this.createNoteHtml(historyNote));
        }
    }

    createNoteHtml(historyNote) {
        const newItem = $(".history-screen-item.template").clone();
        this.addGeneralInfoHtml(newItem, historyNote);

        if (historyNote.action === CaptchaModel.ACTIONS.TAG_CREATE.description) {
            this.addTagNoteHtml(newItem, historyNote);
        } else {
            this.addCaptchaNoteHtml(newItem, historyNote);
        }
        newItem.removeClass("template").removeClass("hide");
        return newItem.get()
    }
    addTagNoteHtml($noteDom, historyNote) {
        $noteDom.find(".change-tag .value").text(historyNote.tag);
    }
    addCaptchaNoteHtml($noteDom, historyNote) {

        $noteDom.append(this.captchaService.createCaptchaHtml(historyNote.captcha));
        if (historyNote.action === CaptchaModel.ACTIONS.CAPTCHA_EDIT.description) {
            this.captchaService.setCaptchasDifference($noteDom, historyNote.captcha, historyNote.oldCaptcha);
        }

    }
    addGeneralInfoHtml($noteDom, historyNote) {
        $noteDom.find(".change-author .value").text(historyNote.user);
        $noteDom.find(".change-action .value").text(historyNote.action);
        $noteDom.find(".change-stamp .value").text(new Date(historyNote.stamp).toLocaleString());
    }
}
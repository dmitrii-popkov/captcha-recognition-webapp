class HistoryBarView {
    constructor(historyModel, userModel, captchaModel, tagsModel) {
        this.historyModel = historyModel;
        this.userModel = userModel;
        this.captchaModel = captchaModel;
        this.tagsModel = tagsModel;
        this.historyNoteService = new HistoryNoteService();
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
        this.historyNoteService.insertNotes(this.historyModel.historyNotes.slice(0, 3), $(".history-bar"));
    }
}
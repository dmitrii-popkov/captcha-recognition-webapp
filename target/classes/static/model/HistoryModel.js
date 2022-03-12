class HistoryModel {
    constructor() {

        this.historyNotes = [];

        this.onNotesChanged = new EventEmitter();
    }

    init() {

    }

    async getHistoryNotes(user) {
        const params = new URLSearchParams({user});

        await $.ajax({
            url: `/api/history/?${params}`,
            method: 'GET',
            success: async (response, textStatus, xhr) => {
                this.historyNotes = response;
            }
        })
    }
}
class HistoryNoteService {

    insertNotes(historyNotes, $notesContainer) {
        $notesContainer.empty();
        for (const note of historyNotes) {
            $notesContainer.append(this.createNoteHtml(note));
        }
    }

    createNoteHtml(note) {

        const newItem = $("#history-item-template").clone();

        this.updateNoteDom(newItem, note);

        newItem.removeClass("hide").removeAttr("id");
        return newItem.get();
    }

    updateNoteDom(noteDom, note) {
        noteDom.find(".history-item-label .history-info").text(note.description)
        noteDom.find(".history-item-label .history-stamp").text(new Date(note.stamp).toLocaleString());
    }
}
class EventEmitter {
    constructor() {
        this.listeners = [];
    }

    subscribe(handler) {
        this.listeners.push(handler);
    }

    notify(data) {
        for (let i = 0; i < this.listeners.length; i++) {
            this.listeners[i](...arguments);
        }
    }
}
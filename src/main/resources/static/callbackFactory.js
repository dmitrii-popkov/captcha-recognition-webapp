class CallbackFactory {
    constructor() {
        this.currentStamp = undefined;
        this.id = 0;
    }

    getPromise() {
        const functionStamp = Date.now();
        this.currentStamp = 0
        const functionId = this.id;
        this.id++
        const promiseFunction = function(func) {
            let returnFunc;
            if (this.currentStamp <= functionStamp) {
                this.currentStamp = functionStamp;
                returnFunc =  Promise.resolve(func)
            } else {
                returnFunc = Promise.reject(new Error(functionId + "skip"));
            }
            return returnFunc
        }
        return promiseFunction.bind(this);
    }
}
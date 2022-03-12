

class CaptchaService {
    static CONTENT_RESULT_SELECTOR = ".content-result";
    static CONTENT_AUTHOR_SELECTOR = ".content-author";
    static CONTENT_TYPE_SELECTOR = ".content-type";
    static CONTENT_ANSWER_SELECTOR = ".content-name";
    static CONTENT_IMAGE_SELECTOR = ".content-image img";

    constructor() {
    }

    createCaptchaHtml(captcha) {

        const newItem = $("#content-item-template").clone();

        this.updateCaptchaDom(newItem, captcha);

        newItem.removeClass("hide").removeAttr("id");
        return newItem.get();
    }

    updateCaptchaDom($captchaDom, captcha) {
        const resultStatus = this.getIsRightLabel(captcha.isRight);
        $captchaDom.find(CaptchaService.CONTENT_IMAGE_SELECTOR).attr("src", `/static/images/captchas/${captcha.name}`);
        $captchaDom.find(CaptchaService.CONTENT_ANSWER_SELECTOR).text(captcha.answer);
        $captchaDom.find(CaptchaService.CONTENT_TYPE_SELECTOR).text(captcha.type.join(' '));
        $captchaDom.find(CaptchaService.CONTENT_AUTHOR_SELECTOR).text(`By ${captcha.author}`);
        $captchaDom.find(CaptchaService.CONTENT_RESULT_SELECTOR).text(resultStatus);
    }
    getIsRightLabel(isRight) {
        let resultStatus;
        if (!isRight) {
            resultStatus = "Not solved"
        } else {
            resultStatus = "Solved";
        }
        return resultStatus;
    }
    setCaptchasDifference(newItem, captcha) {
        for (const entry of Object.entries(captcha)) {
            const entryField = entry[0];
            const newValue = entry[1];
                newItem.find(`.content-${entryField}`).text(`${newValue}`);
        }
        newItem.find(CaptchaService.CONTENT_RESULT_SELECTOR).text(
                `${this.getIsRightLabel(captcha.isRight)}`)
    }

    getCaptchaFromDom($editedCaptcha) {
        return {
            name: $editedCaptcha.find(CaptchaService.CONTENT_IMAGE_SELECTOR).attr("src").split("/").splice(-1)[0],
            author: $editedCaptcha.find(CaptchaService.CONTENT_AUTHOR_SELECTOR).text().split(" ").slice(-1)[0],
            type: $editedCaptcha.find(CaptchaService.CONTENT_TYPE_SELECTOR).text().split(","),
            answer: $editedCaptcha.find(CaptchaService.CONTENT_ANSWER_SELECTOR).text(),
            isRight: $editedCaptcha.find(CaptchaService.CONTENT_RESULT_SELECTOR).text() === 'Solved'
        }
    }
}
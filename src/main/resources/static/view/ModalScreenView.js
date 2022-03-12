class ModalScreenView {
    constructor() {
    }

    $addCaptchaModal = $(".add-captcha-modal");

    init() {
        $(".add-captcha-button").click(this.showAddScreen());
        $(window).click(event => {
            if (event.target.classList.contains('modal')) {
                this.hideModalScreen()
            }
        })
        $(".cancel-button").click(event => this.hideModalScreen());

    }
    showAddScreen() {
        return () => {
            this.$addCaptchaModal.removeClass("hide")
            this.hideRateForm();
        };
    }

    hideModalScreen() {
        $(".modal").addClass("hide")
    }

    hideRateForm() {
        $('.add-captcha-page').removeClass('hide');
        $('.rate-captcha-page').addClass('hide');
    }
}
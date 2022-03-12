//TODO Profile: edits? TBD ON SPRING
//TODO profile: stats? TBD ON SPRING
class ProfilePageView {
    static get TABS() {
        return Object.freeze({
            ABOUT: Symbol('about'),
            CAPTCHAS: Symbol('captchas'),
            STATISTICS: Symbol('statistics')
        });
    }
    constructor(captchaModel, historyModel, userModel) {
        this.captchaModel = captchaModel;
        this.historyModel = historyModel;
        this.userModel = userModel;
    }

    selectedTab = ProfilePageView.TABS.ABOUT;

    init() {
        $('.profile-nav .main-nav-option').click(event => {
            this.selectedTab = Object.values(ProfilePageView.TABS)
                .filter(page => event.target.classList.contains(`profile-${page.description}`))[0];
                this.renderPage();
        })

        this.renderPage();
    }

    renderPage() {
        this.setPageSelected();
        this.setMenuBarSelected();
    }

    addProfileInfo() {

    }

    addCaptchaList() {

    }

    addStatistics() {

    }

    setMenuBarSelected() {
        $('.profile-nav .main-nav-option').removeClass("main-nav-selected")
        $(`.profile-nav .main-nav-option.profile-${this.selectedTab.description}`).addClass("main-nav-selected")
    }

    setPageSelected() {
        $('.profile-content').addClass("hide");
        $(`.profile-content.profile-content-${this.selectedTab.description}`).removeClass("hide");
    }
}
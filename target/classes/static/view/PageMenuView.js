class PageMenuView {
    static get PAGES() {
        return Object.freeze({
            PROFILE: Symbol('profile'),
            BROWSE: Symbol('browse'),
            BUY: Symbol('buy'),
            HISTORY: Symbol('history')
        });
    }

    constructor(userModel) {
        this.selectedPage = PageMenuView.PAGES.BROWSE;
        this.userModel = userModel;
    }

    init() {
        $('.menu-option').click(event => {
            if (event.target.parentElement.classList.contains("profile")
                && _.isEqual(this.userModel.user, UserModel.USERS.NO_USER)) {
                $(".login-modal").removeClass("hide");
            } else {
                this.selectedPage = Object.values(PageMenuView.PAGES)
                    .filter(page => event.target.parentElement.classList.contains(page.description))[0];
                this.renderPage();
            }
        })
        this.renderPage();
    }

    renderPage() {
        this.setPageSelected();
        this.setMenuBarSelected();
        this.setTagBarVisibility();
    }
    setTagBarVisibility() {
        const $tagsBar = $(".tags-bar");
        if (this.selectedPage.description === PageMenuView.PAGES.BROWSE.description) {
            $tagsBar.removeClass("hide");
            // $tagsBar.css("visibility", "visible")
        } else {
            // $tagsBar.css("visibility", "hidden")
            $tagsBar.addClass("hide");
        }
    }
    setMenuBarSelected() {
        $('.menu-option').removeClass("selected")
        $(`.menu-option.${this.selectedPage.description}`).addClass("selected")
    }

    setPageSelected() {
        $('.main').addClass("hide");
        $(`.main.${this.selectedPage.description}`).removeClass("hide");
    }
}
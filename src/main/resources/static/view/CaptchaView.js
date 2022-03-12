const ONLY_MY_FILTER_SELECTOR = ".only-my-filter";

const CAPTCHA_TAG_CONTAINER_SELECTOR = ".captcha-tags-container";

const TAG_CONTAINER_SELECTOR = ".tag-list-container";

const MAIN_NAV_SELECTION_CLASS_SELECTOR = "main-nav-selected";

const RATE_CAPTCHA_PAGE_SELECTOR = ".rate-captcha-page";

class CaptchaView {

    static get FILTERS() {
        return Object.freeze({
            ALL: Symbol('all'),
            UNCHECKED: Symbol('unchecked'),
            SOLVED: Symbol('solved'),
            NOT_SOLVED: Symbol('not_solved')
        });
    }

    constructor(captchaModel, historyModel, userModel, tagsModel) {
        this.captchaModel = captchaModel;
        this.historyModel = historyModel;
        this.userModel = userModel;
        this.tagsModel = tagsModel;

        this.captchaService = new CaptchaService();
        this.tagService = new TagService();
    }

    debouncedSearch = this.debounce(this.renderCaptchas, this);



    selectedFilter = CaptchaView.FILTERS.ALL;
    selectedTag = "none"
    searchValue = "";

    onlyMyFilter = false;

    addCaptchaForm = {
        imageFile: undefined,
        user: undefined
    };

    updateCaptchaForm = {}
    newTagValue = "";

    debounce(func, context, timeout = 200) {
        let timer;
        return (...args) => {
            clearTimeout(timer);
            timer = setTimeout(() => {
                func.apply(context, args);
            }, timeout);
        };
    }

    async init() {

        $(".browse.main .main-nav-option.filter").click(this.changeFilterSelected())
        $(".search-input").first().on('input', this.changeSearchValue())
        $("#add-captcha-image").change(this.loadPreviewImage())
        $(".submit-captcha-button").click(this.addCaptcha())
        $(".is-right-input").on('change', this.changeRateSelected())
        $(".submit-rate-button").click(this.rateCaptcha())
        $(".create-tag-input").on('change', this.setNewTagValue())
        $(".create-tag-button").click(this.addTag())
        $(".submit-change-tag-button").click(this.updateCaptchaTag());
        $(ONLY_MY_FILTER_SELECTOR).click(this.toggleUserFilter())
        $(".add-tag-button").click(this.addTagToCaptchaTagList())
        $(".remove-tag-button").click(this.removeTagFromCaptchaTagList())
        $(CAPTCHA_TAG_CONTAINER_SELECTOR)
            .on('dragover', event => { event.preventDefault()})
            .on('drop', this.addTagToCaptchaTagList());
        $(TAG_CONTAINER_SELECTOR)
            .on('dragover', event => { event.preventDefault()})
            .on('drop', this.removeTagFromCaptchaTagList());
        this.captchaModel.onCaptchasChanged.subscribe(async (captcha, action) => {
            await this.tagsModel.requestTags();
            this.renderTagBar();
            this.renderTagSelectBar();
            if (action.description === CaptchaModel.ACTIONS.CAPTCHA_CREATE.description) {
                this.updateCaptchaForm = captcha;
                this.addCaptchaResolved(captcha);
                this.showRateForm();
            }
            this.renderCaptchas();
        });

        this.userModel.onUserChanged.subscribe(() => {
            this.addCaptchaForm.user = this.userModel.user.login
            this.renderCaptchas();
            this.renderFilterCheckbox();
        })

        this.tagsModel.onTagsChanged.subscribe(async (tag, action) => {
            await this.tagsModel.requestTags();
            this.renderTagBar()
            this.renderTagSelectBar()
        })
        await this.tagsModel.requestTags();
        this.renderTagSelectBar();
        this.renderTagBar();
        await this.renderCaptchas();
        this.renderFilterCheckbox();
    }

    removeTagFromCaptchaTagList() {
        return event => {
            this.updateCaptchaForm.type =
                this.updateCaptchaForm.type.filter(v => v !== this.selectedCaptchaTag)
            this.renderTagCaptchaSelectBar();

        };
    }

    addTagToCaptchaTagList() {
        return event => {
            if (!this.updateCaptchaForm.type.includes(this.selectedEditTag)) {
                this.updateCaptchaForm.type.push(this.selectedEditTag);
                this.renderTagCaptchaSelectBar();
            }
        };
    }

    toggleUserFilter() {
        return event => {
            $(ONLY_MY_FILTER_SELECTOR).toggleClass(MAIN_NAV_SELECTION_CLASS_SELECTOR);
            this.onlyMyFilter = event.target.classList.contains(MAIN_NAV_SELECTION_CLASS_SELECTOR);

            this.renderCaptchas();
        };
    }

    renderFilterCheckbox() {
        if (this.userModel.user.login === UserModel.USERS.NO_USER.login) {
            $(ONLY_MY_FILTER_SELECTOR).addClass("hide")
        } else {
            $(ONLY_MY_FILTER_SELECTOR).removeClass("hide")
        }
    }


    changeFilterSelected() {
        return async event => {
            const id = event.target.id
            this.selectedFilter = this.getSelectedFilter(id);
            this.selectFilter(id);

            await this.renderCaptchas();

        };
    }



    changeSearchValue() {
        return async event => {
            this.searchValue = event.target.value;
            await this.debouncedSearch(this.searchValue);
        };
    }

    loadPreviewImage() {
        return event => {
            const files = event.target.files;
            if (files.length > 0) {
                this.addCaptchaForm.imageFile = files[0];
                $(".add-captcha-image-preview").attr("src", URL.createObjectURL(files[0]));
            }
        };
    }

    addCaptcha() {
        return async () => {
            this.addCaptchaForm.user = this.userModel.user.login;
            this.captchaModel.addCaptcha(this.addCaptchaForm);
        };
    }

    changeRateSelected() {
        return event => {
            this.updateCaptchaForm.isRight = event.target.checked;
        };
    }

    rateCaptcha() {
        return async () => {
            await this.captchaModel.updateCaptcha(this.updateCaptchaForm)
            this.captchaService.updateCaptchaDom($(RATE_CAPTCHA_PAGE_SELECTOR), this.updateCaptchaForm)
        };
    }

    setNewTagValue() {
        return event => {
            this.newTagValue = event.target.value;
        };
    }

    updateCaptchaTag() {
        return async () => {
            const $editedCaptcha = $(".edit-captcha-container .content-item");
            await this.captchaModel.updateCaptcha(this.updateCaptchaForm);
            this.captchaService.updateCaptchaDom($editedCaptcha, this.updateCaptchaForm);
        };
    }

    async renderCaptchas() {
        const user = this.onlyMyFilter ? this.userModel.user.login : undefined;
        const tag = this.selectedTag === "none" ? undefined : this.selectedTag;
        await this.captchaModel.requestCaptchas(
            this.selectedFilter.description, tag, this.searchValue, user);
        this.insertCaptchas(this.captchaModel.captchas);
    }

    getSelectedTag(selectedTagId) {
        return this.tagsModel.tags.filter(tag => tag.name === selectedTagId)[0].name;
    }

    getSelectedFilter(selectedFilterId) {
        return Object.values(CaptchaView.FILTERS).filter(filter => filter.description === selectedFilterId)[0];
    }

    insertCaptchas(captchas) {
        const $mainContent = $(".main-content");
        $mainContent.empty();
        for (const captcha of captchas) {
            const captchaHtml = this.captchaService.createCaptchaHtml(captcha);
            $mainContent.append(captchaHtml);
            captchaHtml[0].addEventListener('click', this.onCaptchaClick(captcha));
        }
    }

    onCaptchaClick(captcha) {
        return () => {
            this.showEditForm(captcha);
            this.updateCaptchaForm = captcha;
        };
    }

    selectFilter(selectedFilter) {
        const $filtersBar = $(".main-nav");
        $filtersBar.find(".filter").removeClass(MAIN_NAV_SELECTION_CLASS_SELECTOR);
        $filtersBar.find(`#${selectedFilter}`).addClass(MAIN_NAV_SELECTION_CLASS_SELECTOR);
    }

    selectTag($container, selectedTag) {
        const $tagsBar = $container;
        $tagsBar.children().removeClass("selected");
        $tagsBar.find(`#${$.escapeSelector(selectedTag)}`).parent().addClass("selected");
    }

    showRateForm() {
        $('.add-captcha-page').addClass('hide');
        $(RATE_CAPTCHA_PAGE_SELECTOR).removeClass('hide');
    }

    addCaptchaResolved(recentCaptcha) {
        const $rateCaptchaPage = $(RATE_CAPTCHA_PAGE_SELECTOR);
        $rateCaptchaPage.find(".content-item").remove()
        $rateCaptchaPage.prepend(this.captchaService.createCaptchaHtml(recentCaptcha));
    }

    showEditForm(captcha) {
        $(".edit-captcha-modal").removeClass("hide");
        const $editCaptchaContainer = $(".edit-captcha-container");
        $editCaptchaContainer.find(".content-item").remove();
        $editCaptchaContainer.prepend(this.captchaService.createCaptchaHtml(captcha))
        this.updateCaptchaForm = captcha;
        this.renderTagCaptchaSelectBar()

    }

    renderTagCaptchaSelectBar() {
        this.tagService.addTags(
            $(CAPTCHA_TAG_CONTAINER_SELECTOR),
            this.tagService.getTagsFromText(this.updateCaptchaForm.type));
        $(".captcha-tags-container .filter-option .filter-option-label").click(this.changeEditCaptchaTagSelected())
            .attr('draggable', true)
            .on('dragover', event => { event.preventDefault()})
            .on('dragstart', this.changeEditCaptchaTagSelected())
    }
    renderTagBar() {
        this.tagsModel.tags.sort((a, b) => b.count - a.count);

        this.tagService.addTags($(".tags-bar"), this.tagsModel.tags.slice(0, 4));
        $(".tags-bar .filter-option .filter-option-label").click(this.changeTagSelected())

    }

    renderTagSelectBar() {
        this.tagsModel.tags.sort((a, b) => b.count - a.count);
        this.tagService.addTags($(TAG_CONTAINER_SELECTOR), this.tagsModel.tags);
        $(".tag-list-container .filter-option .filter-option-label").click(this.changeEditTagSelected()).attr('draggable', true)
            .on('dragover', event => { event.preventDefault()})
            .on('dragstart', this.changeEditTagSelected())
    }

    changeTagSelected() {
        return async event => {
            const id = event.target.id;
            this.selectedTag = this.getSelectedTag(id);
            this.selectTag($(".tags-bar"), id);
            await this.renderCaptchas();
        };
    }

    changeEditTagSelected() {
        return event => {
            const id = event.target.id;
            this.selectedEditTag = this.getSelectedTag(id);
            this.selectTag($(TAG_CONTAINER_SELECTOR), id);
        }
    }

    addTag() {
        return event => {
            this.tagsModel.addTag(this.newTagValue);
        }
    }


    changeEditCaptchaTagSelected() {
        return async event => {
            const id = event.target.id;
            this.selectedCaptchaTag = this.getSelectedTag(id);
            this.selectTag($(CAPTCHA_TAG_CONTAINER_SELECTOR), id);
        }
    }
}

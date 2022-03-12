//TODO set tag - save
//TODO cancel button closer
//TODO moving on click remove buttons in tag edit screen
//TODO remove tag from list of all tags when it is moved
//TODO alphabetical sort of tags in tag edit screen
const app = {
    models: {},
    views: {},
    init: () => {
        app.models.captchaModel = new CaptchaModel();
        app.models.historyModel = new HistoryModel();
        app.models.tagsModel = new TagsModel();
        app.models.userModel = new UserModel();
        app.views.captchaView = new CaptchaView(app.models.captchaModel, app.models.historyModel, app.models.userModel, app.models.tagsModel);
        app.views.modalScreenView = new ModalScreenView();
        app.views.historyBarView = new HistoryBarView(app.models.historyModel, app.models.userModel, app.models.captchaModel, app.models.tagsModel)
        app.views.historyScreenView = new HistoryScreenView(app.models.historyModel, app.models.userModel, app.models.captchaModel, app.models.tagsModel)
        app.views.pageMenuView = new PageMenuView(app.models.userModel)
        app.views.userBarView = new UserBarView(app.models.userModel)
        app.views.profilePageView = new ProfilePageView(app.models.captchaModel, app.models.historyModel, app.models.userModel)
    },
    start: () => {
      Object.values(app.models).forEach(model => model.init());
      Object.values(app.views).forEach(view => view.init());
    }
};
app.init();
app.start();

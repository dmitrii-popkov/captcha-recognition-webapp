class TagService {
    constructor() {
    }

    addTags($container, tags) {
        $container.empty();
        for (const tag of tags) {
            $container.append(this.createTagHtml(tag));
        }
    }
    getTagsFromText(tags) {
        return tags.map(v => new Object({name: v}));
    }
    createTagHtml(tag) {
        const newTagItem = $(".tag-option-template").clone();

        newTagItem.find(".dot").addClass(['red', 'orange', 'cyan', 'violet'][Math.floor(Math.random() * 4)]);
        const $link = newTagItem.find(".filter-option-label");
        $link.attr('id', tag.name).text(tag.name);
        newTagItem.removeClass("hide").removeClass("tag-option-template");

        return newTagItem.get();
    }

}
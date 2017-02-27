/**
 * Created by Farzin on 11/8/2015.
 */


function onKeyUpForHashAuthor(ed, e) {

    if (e.key == 'Escape') {
        $('#authorSearchResults').hide();
    }
    else if (isInHashAuthor(ed)) {
        if (e.key == 'Backspace') {
            var lastHashTagNode = $(ed.selection.getNode());
            if ((lastHashTagNode.text() == ''))
                lastHashTagNode.remove();
        } else {
            var phrase = getHashAuthorSearchPhrase(ed);
            if (e.key == ' ') {
                if (phrase != null) {

                }
            }
            else {
                doHashAuthorSearch(ed, phrase);
            }
        }
    } else {
        $('#authorSearchResults').hide();
    }
}

function onKeyDownForHashAuthor(ed, e) {

    if (isInHashAuthor(ed)) {
        if (e.key == '@') {
            e.preventDefault();
            e.stopPropagation();
        }
        else if ((e.key == ' '
            || e.key == 'Enter'
            || e.key == '.'
            || e.key == ','
            || e.key == ';'
            || e.key == '،'
            || e.key == '؛')) {

            e.preventDefault();
            e.stopPropagation();

            var container = $('#authorSearchResults');
            if (container.is(':visible')) {
                var lastHashAuthorNode = $(ed.selection.getNode());
                var link = $('#authorSearchResultsTab').find('.k-content.k-state-active a.k-state-active');
                if (normalize('#' + link.attr('data-tag')) == normalize(lastHashAuthorNode.text())) {
                    lastHashAuthorNode.text('@' + link.attr('data-author'));
                    lastHashAuthorNode.attr('href', link.attr('data-link'));
                    lastHashAuthorNode.attr('data-clazz', link.attr('data-typeClass'));
                    lastHashAuthorNode.attr('data-id', link.attr('data-id'));
                } else {
                    lastHashAuthorNode.text('@برچسب_اشتباه');
                    lastHashAuthorNode.attr('href', '#');
                    lastHashAuthorNode.attr('data-clazz', '');
                    lastHashAuthorNode.attr('data-id', '');
                }
                container.hide();

                var currentPosition = getCursorPosition(ed);
                var content = ed.getContent();
                while (content[++currentPosition] != '>');
                setCursorPosition(ed, currentPosition + 1);
                var character = e.key;
                if (e.key == ' ')
                    character = '&nbsp;';
                if (e.key == 'Enter')
                    character = '&nbsp;';
                console.debug('mceInsertContent', character);
                ed.execCommand('mceInsertContent', true, character);
            }
        }
        else if (e.key == 'ArrowDown') {

            e.preventDefault();
            e.stopPropagation();

            var resultsTab = $('#authorSearchResultsTab').find('.k-content.k-state-active');
            var link = resultsTab.find('a.k-state-active').next('#authorSearchResultsTab .k-content.k-state-active a');
            if (link.length) {
                resultsTab.find('a').removeClass('k-state-active');
                link.addClass('k-state-active');
                resultsTab.scrollTop(resultsTab.scrollTop() + link.position().top - resultsTab.height() / 2 + link.height() / 2);
            }
        }
        else if (e.key == 'ArrowUp') {

            e.preventDefault();
            e.stopPropagation();

            var resultsTab = $('#authorSearchResultsTab').find('.k-content.k-state-active');
            var link = resultsTab.find('a.k-state-active').prev('#authorSearchResultsTab .k-content.k-state-active a');
            if (link.length) {
                resultsTab.find('a').removeClass('k-state-active');
                link.addClass('k-state-active');
                resultsTab.scrollTop(resultsTab.scrollTop() + link.position().top - resultsTab.height() / 2 + link.height() / 2);
            }

        }
        else if (e.key == 'Tab') {

            e.preventDefault();
            e.stopPropagation();

            var tabStrip = $("#authorSearchResultsTab").data("kendoTabStrip");
            var tabsCount = $('#authorSearchResults').find('.k-tabstrip-items li').length;
            var currentIndex = tabStrip.select().index();
            if (e.shiftKey) {
                currentIndex = (currentIndex - 1 + tabsCount) % tabsCount;
            }
            else {
                currentIndex = (currentIndex + 1) % tabsCount;
            }
            tabStrip.select(currentIndex);
            return false;
        }
        return false;
    }
    else if (e.key == '@') {
        e.preventDefault();
        e.stopPropagation();
        if (!isInHashTag(ed)) {
            ed.execCommand('mceInsertContent', false, '<a href="#" class="hashAuthor">@</a>');
            setCursorPosition(ed, getCursorPosition(ed) - 4);
        }
        return false;
    }
}

function onClickForHashAuthor(ed, e) {
    if (isInHashAuthor(ed)) {
        var phrase = getHashAuthorSearchPhrase(ed);
        doHashAuthorSearch(ed, phrase);
    }
}

function onBlurForHashAuthor(ed, e) {
}

function doHashAuthorSearch(ed, phrase) {
    if (phrase != null) {
        showHashAuthorSearchBox(ed);
        showAuthorSearchResults(ed, phrase);
    }
    else {
        $('#authorSearchResults').hide();
    }
}

function insertHashAuthor(label, link, clazz, id) {
    var container = $('#authorSearchResults');
    if (!container.is(':visible'))
        return;
    var node = $(tinyMCE.activeEditor.selection.getNode());
    if (node.prop('nodeName').toString().toLowerCase() != 'a')
        node = node.find('a.hashAuthor');
    node.text('@' + label);
    node.attr('href', link);
    node.attr('data-clazz', clazz);
    node.attr('data-id', id);
    container.hide();
}

function isInHashAuthor(ed) {
    var element = $(ed.selection.getNode());
    return element.prop('nodeName').toString().toLowerCase() == 'a' && element.text().startsWith('@');
}

function getHashAuthorSearchPhrase(ed) {
    var cursorPosition = getCursorPosition(ed);
    var content = ed.getContent();
    var hashAuthorPosition = content.substr(0, cursorPosition).lastIndexOf('@');
    if (hashAuthorPosition == -1)
        return null;
    var phrase = content.substring(hashAuthorPosition + 1, cursorPosition);
    if (phrase.indexOf(' ') != -1
        || phrase.indexOf('.') != -1
        || phrase.indexOf(',') != -1
        || phrase.indexOf(';') != -1
        || phrase.indexOf('،') != -1
        || phrase.indexOf('؛') != -1
        || phrase.indexOf('<') != -1
        || phrase.indexOf('>') != -1)
        return null;
    return phrase;
}

function showHashAuthorSearchBox(ed) {
    var cursorPosition = getCurrentCursorPositionOfEditor(ed);
    $('#tagSearchResults').hide();
    $('#authorSearchResults').css('top', cursorPosition.top + ($(ed.editorContainer).parent().hasClass('inline') ? 10 : 50)).css('left', cursorPosition.left - 400).show();
}
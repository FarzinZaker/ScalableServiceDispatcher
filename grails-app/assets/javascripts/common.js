/**
 * Created by root on 8/26/14.
 */
function handleScroll() {

    if ($(window).scrollTop() >= $('#header').height()) {
        $('.menuContainer').addClass('fixed');
        $('.backToTop').css('opacity', 0.9);
    }
    else {
        $('.menuContainer').removeClass('fixed');
        $('.backToTop').css('opacity', 0);
    }
}
$(function () {
    handleScroll();
    $(window).scroll(function () {
        handleScroll();
    });
});

var notLocked = true;
$.fn.animateHighlight = function (highlightColor, duration) {
    var highlightBg = highlightColor || "#FFFF9C";
    var animateMs = duration || 1500;
    var originalBg = this.css("backgroundColor");
    if (notLocked) {
        notLocked = false;
        this.stop().css("background-color", highlightBg)
            .animate({backgroundColor: originalBg}, animateMs);
        setTimeout(function () {
            notLocked = true;
        }, animateMs);
    }
};

if (window.Prototype) {
    delete Object.prototype.toJSON;
    delete Array.prototype.toJSON;
    delete Hash.prototype.toJSON;
    delete String.prototype.toJSON;
}

$.fn.fadeInWithDelay = function () {
    var delay = 0;
    return this.each(function () {
        $(this).delay(delay).animate({opacity: 1}, 200);
        delay += 100;
    });
};

function isElementInViewport(el) {

    //special bonus for those using jQuery
    if (typeof jQuery === "function" && el instanceof jQuery) {
        el = el[0];
    }

    var rect = el.getBoundingClientRect();

    return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /*or $(window).height() */
        rect.right <= (window.innerWidth || document.documentElement.clientWidth) /*or $(window).width() */
    );
}

function onVisibilityChange(el, callback) {
    if (isElementInViewport(el))
        callback();
}

function compareSearchResults(a, b) {
    if (a.score < b.score)
        return 1;
    if (a.score > b.score)
        return -1;
    return 0;
}

function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

$(document).ready(function () {
    $('.noPlaceReservation').css('display', 'block');
    $('.hideBeforeLoad').css('opacity', 0).animate({
        opacity: 1
    }, 200);
});

function strip(html) {
    var tmp = document.createElement("DIV");
    tmp.innerHTML = html;
    return tmp.textContent || tmp.innerText || "";
}

var fixedPosWindows = null;
var currentWindowScrollPos;

function FixWindowPos(kwin) {
    if (fixedPosWindows === null) {
        fixedPosWindows = [];
        currentWindowScrollPos = $(window).scrollTop();
        $(window).scroll(function () {
            var top = $(window).scrollTop();
            var diff = top - currentWindowScrollPos;
            for (var i = 0; i < fixedPosWindows.length; i++) {
                var w = fixedPosWindows[i].parent();
                w.css("top", parseInt(w.css("top"), 10) + diff + "px");
            }
            currentWindowScrollPos = top;
        });
    }
    fixedPosWindows.push(kwin);
}
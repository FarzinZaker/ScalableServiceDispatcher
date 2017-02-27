/**
 * Created by farzin on 08/12/2014.
 */

var oldKendoFormat = kendo.format;
var oldKendoFormatDate = kendo.formatDate;
//var objectToString = {}.toString;
//var formatsSequence = ["G", "g", "d", "F", "D", "y", "m", "T", "t"];
//var isArray = $.isArray;
//var ARIA_SELECTED = "aria-selected",
//    ARIA_EXPANDED = "aria-expanded",
//    ARIA_HIDDEN = "aria-hidden",
//    ARIA_DISABLED = "aria-disabled",
//    ARIA_READONLY = "aria-readonly",
//    ARIA_ACTIVEDESCENDANT = "aria-activedescendant",
//    ID = "id";


kendo.format = function (fmt) {
    switch (fmt) {
        case 'yyyy/MM/dd':
            return formatPersianDate.apply(this, Array.prototype.slice.call(arguments, 0));
        default:
            return oldKendoFormat.apply(this, Array.prototype.slice.call(arguments, 0));
    }
};

kendo.formatDate = function (value, fmt, culture) {
    return oldKendoFormatDate.apply(this, Array.prototype.slice.call(arguments, 0));
};

function formatPersianDate(format, value) {
    if (value == '')
        value = new Date();
    if (typeof(value) == 'string')
        value = new Date(value);
    var persianDate;
    if (typeof new JalaliDate().gregorianToJalali == 'function')
        persianDate = new JalaliDate().gregorianToJalali(value.getFullYear(), (value.getMonth() == 12 ? value.getMonth() : value.getMonth() + 1), value.getDate());
    else
        persianDate = new JalaliDate().gregorian_to_jalali(value.getFullYear(), (value.getMonth() == 12 ? value.getMonth() : value.getMonth() + 1), value.getDate());

    return (persianDate[0] + "/" + persianDate[1] + "/" + persianDate[2]);
}

function getJalaliDate(value) {
    if (value == '')
        value = new Date();
    if (typeof(value) == 'string')
        value = new Date(value);
    var persianDate;
    if (typeof new JalaliDate().gregorianToJalali == 'function')
        persianDate = new JalaliDate().gregorianToJalali(value.getFullYear(), (value.getMonth() == 12 ? value.getMonth() : value.getMonth() + 1), value.getDate());
    else
        persianDate = new JalaliDate().gregorian_to_jalali(value.getFullYear(), (value.getMonth() == 12 ? value.getMonth() : value.getMonth() + 1), value.getDate());

    return (persianDate[0] + "/" + (persianDate[1] < 10 ? '0' : '') + persianDate[1] + "/" + persianDate[2]);
}

//kendo.toString = function(value, fmt, culture) {
//    if (fmt) {
//        if (objectToString.call(value) === "[object Date]" || value instanceof JalaliDate) {
//            return kendo.formatDate(value, fmt, culture);
//        } else if (typeof value === NUMBER) {
//            return kendo.formatNumber(value, fmt, culture);
//        }
//    }
//
//    return value !== undefined ? value : "";
//};
//
//var oldParseDate = kendo.parseDate;
//
//kendo.parseDate = function(value, formats, culture) {
//    if (objectToString.call(value) === "[object Date]" || value instanceof JalaliDate) {
//        return value;
//    }
//
//    if (value !== null && value !== "" && value != undefined && formats == "yyyy/MM/dd") {
//        return JalaliDate.parse(value);
//    }
//
//    return oldParseDate(value, formats, culture);
//};
//
//(function($, undefined) {
//    DATE = JalaliDate;
//})();
//
//
//var calendarPlugin = (function (init) {
//    return kendo.ui.Calendar.extend({
//        options: {
//            name: 'Calendar'
//        },
//        _click: function (link) {
//            var that = this,
//                options = that.options,
//                currentValue = new JalaliDate(+that._current),
//                value = link.attr(kendo.attr(VALUE)).split("/");
//
//            //Safari cannot create correctly date from "1/1/2090"
//            value = new DATE(value[0], value[1], value[2]);
//            adjustDST(value, 0);
//
//            that._view.setDate(currentValue, value);
//
//            that.navigateDown(restrictValue(currentValue, options.min, options.max));
//        }
//    });
//
//})(kendo.ui.Calendar.fn.init);
//kendo.ui.plugin(calendarPlugin);
//
//function normalize(options) {
//    var parseFormats = options.parseFormats,
//        format = options.format;
//
//    kendo.calendar.normalize(options);
//
//    parseFormats = $.isArray(parseFormats) ? parseFormats : [parseFormats];
//    if ($.inArray(format, parseFormats) === -1) {
//        parseFormats.splice(0, 0, options.format);
//    }
//
//    options.parseFormats = parseFormats;
//}
//
//var datepickerPlugin = (function (init) {
//    return kendo.ui.DatePicker.extend({
//        options: {
//            name: 'DatePicker'
//        },
//        init: function (element, options) {
//            var that = this,
//                disabled,
//                div,
//                Widget = kendo.ui.Widget,
//                parse = kendo.parseDate;
//
//            Widget.fn.init.call(that, element, options);
//            element = that.element;
//            options = that.options;
//
//            options.min = parse(element.attr("min")) || parse("1278/11/01", "yyyy/MM/dd"); // || parse(options.min);
//            options.max = parse(element.attr("max")) || parse("1478/11/01", "yyyy/MM/dd"); // || parse(options.max);
//
//            normalize(options);
//
//            that._wrapper();
//
//            that.dateView = new kendo.DateView($.extend({}, options, {
//                id: element.attr(ID),
//                anchor: that.wrapper,
//                change: function() {
//                    // calendar is the current scope
//                    that._change(this.value());
//                    that.close();
//                },
//                close: function(e) {
//                    if (that.trigger("close")) {
//                        e.preventDefault();
//                    } else {
//                        element.attr(ARIA_EXPANDED, false);
//                        div.attr(ARIA_HIDDEN, true);
//                    }
//                },
//                open: function(e) {
//                    var options = that.options,
//                        date;
//
//                    if (that.trigger("open")) {
//                        e.preventDefault();
//                    } else {
//                        if (that.element.val() !== that._oldText) {
//                            date = parse(element.val(), options.parseFormats, options.culture);
//
//                            that.dateView[date ? "current" : "value"](date);
//                        }
//
//                        element.attr(ARIA_EXPANDED, true);
//                        div.attr(ARIA_HIDDEN, false);
//
//                        that._updateARIA(date);
//
//                    }
//                }
//            }));
//            div = that.dateView.div;
//
//            that._icon();
//
//            try {
//                element[0].setAttribute("type", "text");
//            } catch(e) {
//                element[0].type = "text";
//            }
//
//            element
//                .addClass("k-input")
//                .attr({
//                    role: "combobox",
//                    "aria-expanded": false,
//                    "aria-owns": that.dateView._dateViewID
//                });
//
//            that._reset();
//            that._template();
//
//            disabled = element.is("[disabled]");
//            if (disabled) {
//                that.enable(false);
//            } else {
//                that.readonly(element.is("[readonly]"));
//            }
//
//            that._old = that._update(options.value || that.element.val());
//            that._oldText = element.val();
//
//            kendo.notify(that);
//        }
//    });
//
//})(kendo.ui.DatePicker.fn.init);
//kendo.ui.plugin(datepickerPlugin);

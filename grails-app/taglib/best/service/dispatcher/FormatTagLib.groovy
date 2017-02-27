package best.service.dispatcher

import fi.joensuu.joyds1.calendar.JalaliCalendar

class FormatTagLib {
    static defaultEncodeAs = [taglib: 'none']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "format"

    def jalaliDate = { attrs, body ->
        if (attrs.date) {
            def cal = Calendar.getInstance()
            cal.setTime(attrs.date)

            def jc = new JalaliCalendar(cal)
            if ((attrs.hm && Boolean.parseBoolean(attrs.hm?.toString())) || (attrs.timeOnly && Boolean.parseBoolean(attrs.timeOnly?.toString())))
                out << String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
            if ((attrs.hm && Boolean.parseBoolean(attrs.hm?.toString())) && !(attrs.timeOnly && Boolean.parseBoolean(attrs.timeOnly?.toString())))
                out << ' '
            if (!(attrs.timeOnly && Boolean.parseBoolean(attrs.timeOnly?.toString())))
                out << String.format("%04d/%02d/%02d", jc.getYear(), jc.getMonth(), jc.getDay())
        }
    }

    def html = { attrs, body ->
        out << attrs.value
    }

    def twit = { attrs, body ->
        def text = attrs.value
        text = text.replace('#', "<i class='fa fa-hashtag'></i>")
        text = text.replace('@', "<i class='fa fa-at'></i>")
        out << text.replace('../', '/')
    }
}

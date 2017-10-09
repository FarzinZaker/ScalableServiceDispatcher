<div class="clear-fix"></div>

<a href="#" class="backToTop"><i class="fa fa-chevron-up"></i></a>

<div class="footerContainer">
    <div class="footer footer1">
        <div class="container">
            <div class="row">
                <div class="col-sm-12 quickLinks">
                    <h2><g:message code="quickLinks.title"/></h2>
                    <ul>
                        <li>
                            <a href="!"><g:message
                                    code="quickLinks.sample"/></a>
                        </li>
                    </ul>

                    <div style="float:left;line-height:34px;">
                        <g:message code="version"/>
                        <g:meta name="info.app.version"/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="footer footer2">
        <div class="container">
            <div class="row">
                <div class="col-sm-12 footer-link-list first">
                    <h2><g:message code="about"/></h2>
                    <ul>
                        <li>
                            <a href="#"><g:message
                                    code="about.sample"/></a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 footer-link-list">
                    <h2><g:message code="support"/></h2>
                    <ul>
                        <li>
                            <a href="#"><g:message code="support.sample"/></a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-3">

                    <div class="row">
                        <div class="col-sm-12 linkList">
                            <h2><g:message code="rules.title"/></h2>
                            <ul>
                                <li>
                                    <a href="#"><g:message
                                            code="rules.sample"/></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="col-sm-9 thirdParties">
                    <h3><g:message code="thirdParty.list"/></h3>

                    <div>
                        <div class="col-sm-2">
                            <a href="#" target="_blank">
                                <span><g:message code="thirdParty.sample"/></span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="footer footer3">
        <div class="container">
            <div class="row">
                <div class="col-sm-9">
                    <asset:image src="khavarmiane-white-logo.png"/>
                    <span style="display: inline-block;margin-right:10px;"><g:message code="copyright"/></span>
                </div>

                <div class="col-sm-3 socialLinks">
                    <a href="https://www.facebook.com/sharer/sharer.php?u=${createLink(uri: '/', absolute: true)?.toString()?.encodeAsURL()}"
                       target="_blank">
                        <i class="fa fa-facebook"></i>
                    </a>
                    <a href="https://twitter.com/home?status=${createLink(uri: '/', absolute: true)?.toString()?.encodeAsURL()}"
                       target="_blank">
                        <i class="fa fa-twitter"></i>
                    </a>
                    <a href="https://plus.google.com/share?url=${createLink(uri: '/', absolute: true)?.toString()?.encodeAsURL()}"
                       target="_blank">
                        <i class="fa fa-google"></i>
                    </a>
                    <a href="https://www.linkedin.com/shareArticle?mini=true&url=${createLink(uri: '/', absolute: true)?.toString()?.encodeAsURL()}&title=${message(code: 'site.title')?.encodeAsURL()}&summary=&source="
                       target="_blank">
                        <i class="fa fa-linkedin"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script language="javascript" type="text/javascript">
    jQuery('.backToTop').click(function () {
        jQuery('body,html').animate({
            scrollTop: 0
        }, 800);
        return false;
    });

    //    $(document).ready(function () {
    //        (function (i, s, o, g, r, a, m) {
    //            i['GoogleAnalyticsObject'] = r;
    //            i[r] = i[r] || function () {
    //                        (i[r].q = i[r].q || []).push(arguments)
    //                    }, i[r].l = 1 * new Date();
    //            a = s.createElement(o),
    //                    m = s.getElementsByTagName(o)[0];
    //            a.async = 1;
    //            a.src = g;
    //            m.parentNode.insertBefore(a, m)
    //        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
    //
    //        ga('create', 'UA-80642019-1', 'auto');
    //        ga('send', 'pageview');
    //    })
</script>

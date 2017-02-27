
<div class="captchaContainer">
    <form:textBox name="${name}" id="${id}" validation="required serverResponseError" style="width: ${width - 250}px"/>
    <img src="${createLink(controller: 'login', action: 'captcha', id: new Date().time, params: [type: type])}"
         id="${id}Image"/>
    <form:button onclick="refreshCaptcha('${id}')">
        <i class='fa fa-refresh'></i>
    </form:button>
</div>

<script language="javascript">
    function refreshCaptcha(id) {
        $('#' + id + 'Image').attr('src', '${createLink(controller: 'login', action: 'captcha')}/' + new Date().getTime() + '?type=${type}');
    }
</script>
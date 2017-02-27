<div id="confirmDialog" class="k-rtl">
    <img src="${resource(dir: 'images', file: 'question.png')}"/>
    <div class="message">message</div>

    <div class="toolbar">
        <input type="button" class="k-button btn-cancel" value="${message(code: 'confirm.dialog.cancel')}"/>
        <input type="button" class="k-button btn-ok" value="${message(code: 'confirm.dialog.ok')}"/>
    </div>
</div>

<script>
    var oldConfirm = window.confirm;
    var confirmDialog = $("#confirmDialog");
    var useOldConfirm;

    function resetConfirm(){
        useOldConfirm = true;
        window.confirm = oldConfirm;
    }
    $(document).ready(function () {

        if (!confirmDialog.data("kendoWindow")) {
            confirmDialog.kendoWindow({
                width: "400px",
                %{--title: "${message(code:'confirm.dialog.title')}",--}%
                modal: true,
                actions: [
                    "Close"
                ]
            });
        }

        if(!useOldConfirm) {

            window.confirm = function (message, callback, cancelCallback) {
                confirmDialog.find(".message").html(message);
                confirmDialog.find(".btn-cancel").click(function () {
                    confirmDialog.data("kendoWindow").close();
                    if (cancelCallback)
                        cancelCallback();
                });

                confirmDialog.parent().find('.k-window-action').click(function () {
                    if (cancelCallback)
                        cancelCallback();
                });

                confirmDialog.find(".btn-ok").unbind().click(function () {
                    confirmDialog.data("kendoWindow").close();
                    if (callback)
                        callback();
                });
                confirmDialog.data("kendoWindow").center().open();
            }
        }
    });
</script>
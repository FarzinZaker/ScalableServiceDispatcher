
<div id="alertDialog" class="k-rtl">
    <img src="${resource(dir: 'images', file: 'error.png')}"/>
    <div class="message">message</div>
    <div class="toolbar">
        <input type="button" class="k-button btn-ok" value="${message(code:'alert.dialog.ok')}"/>
    </div>
</div>

<script>
    var alertDialog = $("#alertDialog");
    $(document).ready(function() {

        if (!alertDialog.data("kendoWindow")) {
            alertDialog.kendoWindow({
                width: "400px",
                modal: true,
                actions: [
                    "Close"
                ]
            });
        }

        window.alert = function(message){
            alertDialog.find(".message").html(message);
            alertDialog.find(".btn-ok").click(function(){
                alertDialog.data("kendoWindow").close();
            });
            alertDialog.data("kendoWindow").center().open();
        }
    });
</script>
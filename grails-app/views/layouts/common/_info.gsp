
<div id="infoDialog" class="k-rtl">
    <img src="${resource(dir: 'images', file: 'info.png')}"/>
    <div class="message">message</div>
    <div class="toolbar">
        <input type="button" class="k-button btn-ok" value="${message(code:'info.dialog.ok')}"/>
    </div>
</div>

<script>
    var infoDialog = $("#infoDialog");
    $(document).ready(function() {

        if (!infoDialog.data("kendoWindow")) {
            infoDialog.kendoWindow({
                width: "400px",
                modal: true,
                actions: [
                    "Close"
                ]
            });
        }

        window.info = function(message){
            infoDialog.find(".message").html(message);
            infoDialog.find(".btn-ok").click(function(){
                infoDialog.data("kendoWindow").close();
            });
            infoDialog.data("kendoWindow").center().open();
        }
    });
</script>
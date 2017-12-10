<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 8/14/14
  Time: 4:48 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="menu.services.serviceDefinition.parameters" args="${[serviceDefinition?.name]}"/></title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: message(code: 'menu.services'), url: '#'],
                    [text: message(code: 'menu.services.serviceDefinition'), url: createLink(controller: 'serviceDefinition', action: 'list')],
                    [text: message(code: 'menu.services.serviceDefinition.parameters', args: [serviceDefinition?.name]), url: createLink(action: 'list', id: params.id)]
            ]}"/>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">

            <div class="k-rtl">
                <div id="grid"></div>
            </div>
        </div>
    </div>
</div>

<div class="hidden k-rtl">
    <div id="formWindow"></div>
</div>

<div class="hidden" id="formWindowLoading">
    <div>
        <span class="loading" style="display: block">
            <img src="${resource(dir: 'images', file: 'loading.gif')}"/>
            <span><g:message code="wait"/></span>
        </span>
    </div>
</div>


<script>
    $(document).ready(function () {
        $("#grid").kendoGrid({

            dataSource: {
                transport: {
                    type: 'odata',
                    read: {
                        url: "${createLink(action: 'jsonList', params:[serviceDefinition: params.id])}",
                        dataType: "json",
                        type: "POST"

                    },
                    parameterMap: function (data, action) {
                        if (action === "read") {
                            data.parent = $('#parent').val();
                            return data;
                        } else {
                            return data;
                        }
                    }
                },
                schema: {
                    model: {
                        fields: {
                            id: {type: "number"},
                            name: {type: "string"},
                            required: {type: "boolean"},
                            type: {type: "string"},
                            lastUpdated: {type: "string"}
                        }
                    },
                    data: "data",
                    total: "total"
                },
                pageSize: 10,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                sort: {field: "name", dir: "asc"}
            },
            filterable: false,
            sortable: true,
            pageable: true,
            groupable: false,
            columns: [
                {
                    field: "id",
                    title: "${message(code:'serviceParameter.id.label')}",
                    filterable: false
                },
                {
                    field: "name",
                    title: "${message(code:'serviceParameter.name.label')}",
                    filterable: false
                },
                {
                    field: "type",
                    title: "${message(code:'serviceParameter.type.label')}",
                    filterable: false
                },
                {
                    field: "required",
                    title: "${message(code:'serviceParameter.required.label')}",
                    filterable: false,
                    template: "<i class=\"fa #: required ? 'fa-thumbs-up' : '' #\"></i>",
                    width: "80px",
                    attributes: {style: "text-align: center"},
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    field: "systemValue",
                    title: "${message(code:'serviceParameter.systemValue.label')}",
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "${message(code:'serviceParameter.displayName.label')}",
                    filterable: false
                },
                {
                    field: "useForSignatureCheckWithCore",
                    title: "${message(code:'serviceParameter.useForSignatureCheckWithCore.label')}",
                    filterable: false,
                    template: "<i class=\"fa #: useForSignatureCheckWithCore ? 'fa-thumbs-up' : '' #\"></i>",
                    attributes: {style: "text-align: center"},
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    field: "useAsAmountToCheckWithCore",
                    title: "${message(code:'serviceParameter.useAsAmountToCheckWithCore.label')}",
                    filterable: false,
                    template: "<i class=\"fa #: useAsAmountToCheckWithCore ? 'fa-thumbs-up' : '' #\"></i>",
                    attributes: {style: "text-align: center"},
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    field: "displayForSignature",
                    title: "${message(code:'serviceParameter.displayForSignature.label')}",
                    filterable: false,
                    template: "<i class=\"fa #: displayForSignature ? 'fa-thumbs-up' : '' #\"></i>",
                    attributes: {style: "text-align: center"},
                    headerAttributes: {style: "text-align: center"}
                },
                %{--{--}%
                    %{--field: "aggregateField",--}%
                    %{--title: "${message(code:'serviceParameter.aggregateField.label')}",--}%
                    %{--filterable: false--}%
                %{--},--}%
                {
                    field: "lastUpdated",
                    title: "${message(code:'serviceParameter.lastUpdated.label')}",
                    filterable: false
                },
                {
                    command: {text: "${message(code:'edit')}", click: editGridItem},
                    title: "",
                    width: "85px",
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    command: {text: "${message(code:'remove')}", click: removeGridItem},
                    title: "",
                    width: "85px",
                    headerAttributes: {style: "text-align: center"}
                }
            ],
            toolbar: [
                {
                    template: "<a class='k-button k-button-icontext k-grid-add' href='javascript:addGridItem();'>${message(code: 'serviceParameter.add')}</a>"
                }
            ]
        });
    });

    function addGridItem() {
        $('#formWindow').html($('#formWindowLoading').html())
            .kendoWindow({
                width: '820px',
                content: '${createLink(action: 'create', params:[serviceDefinition:params.id])}',
                modal: true,
                close: function (e) {
                }
            }).data('kendoWindow').title('${message(code:'serviceParameter.add')}').center().open().bind("refresh", function () {
            $('#formWindow').data('kendoWindow').center().open();
        });
    }

    function editGridItem(e) {
        $('#formWindow').html($('#formWindowLoading').html())
            .kendoWindow({
                width: '820px',
                content: '${createLink(action: 'edit')}/' + this.dataItem($(e.currentTarget).closest("tr")).id,
                modal: true,
                close: function (e) {
                }
            }).data('kendoWindow').title('${message(code:'serviceParameter.edit')}').center().open().bind("refresh", function () {
            $('#formWindow').data('kendoWindow').center().open();
        });
    }

    function saveItem() {
        $.ajax({
            url: "${createLink(action: 'save')}",
            dataType: "json",
            data: $('#itemForm').serialize(),
            type: "POST",
            success: function (response) {
                if (response == '1') {
                    var documentListView = $('#grid').data('kendoGrid');
                    documentListView.dataSource.read();
                    documentListView.refresh();
                    $('#formWindow').data('kendoWindow').close();
                }
                else
                    window.alert('${message(code:'serviceParameter.save.error')}');

            },
            error: function () {
                window.alert('${message(code:'serviceParameter.save.error')}');
            }
        });
    }

    function cancelEdit() {
        $('#formWindow').data('kendoWindow').close();
    }

    var idForDelete = 0;
    function removeGridItem(e) {
        if (idForDelete == 0) {
            idForDelete = this.dataItem($(e.currentTarget).closest("tr")).id;
            confirm('${message(code:'serviceParameter.delete.confirm')}', deleteItem, cancelDelete);
        }

    }

    function cancelDelete() {
        idForDelete = 0;
    }

    function deleteItem() {
        if (idForDelete > 0) {
            $.ajax({
                type: "POST",
                url: '${createLink(action: 'delete')}',
                data: {id: idForDelete}
            }).done(function (response) {
                if (response == "1") {
                    var documentListView = $('#grid').data('kendoGrid');
                    documentListView.dataSource.read();
                    documentListView.refresh();
                }
                else {
                    window.alert('${message(code:'serviceParameter.delete.error')}');
                }
            });
            idForDelete = 0;
        }
    }
</script>
</body>
</html>
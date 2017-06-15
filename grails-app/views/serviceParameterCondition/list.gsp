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
    <title><g:message code="menu.customers.services.parameterLimits.${params.type}" args="${[limit?.name]}"/></title>
    <form:datePickerResources/>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: message(code: 'menu.customers'), url: createLink(controller: 'customer', action: 'list')],
                    [text: message(code: 'menu.customers.services', args: [limit?.customerService?.customer?.name]), url: createLink(controller: 'customerService', action: 'list', id: limit?.customerService?.customer?.id)],
                    [text: message(code: 'menu.customers.services.parameterLimits', args: [limit?.customerService?.service?.name]), url: createLink(controller: 'serviceParameterLimit', action: 'list', id: limit?.customerService?.id)],
                    [text: message(code: "menu.customers.services.parameterLimits.${params.type}", args: [limit?.name]), url: createLink(action: 'list', id: params.id, params: [type: params.type])]
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
                        url: "<format:html value="${createLink(action: 'jsonList', params:[limit:params.id, type:params.type])}"/>",
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
                            parameter: {type: "string"},
                            count: {type: "number"},
                            unit: {type: "string"},
                            operator: {type: "string"},
                            value: {type: "string"},
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
                sort: {field: "lastUpdated", dir: "desc"}
            },
            filterable: false,
            sortable: true,
            pageable: true,
            groupable: false,
            columns: [
                {
                    field: "id",
                    title: "${message(code:'serviceParameterCondition.id.label')}",
                    filterable: false
                },
                {
                    field: "parameter",
                    title: "${message(code:'serviceParameterCondition.parameter.label')}",
                    filterable: false
                },
                {
                    field: "count",
                    title: "${message(code:'serviceParameterCondition.count.label')}",
                    filterable: false
                },
                {
                    field: "unit",
                    title: "${message(code:'serviceParameterCondition.unit.label')}",
                    filterable: false
                },
                {
                    field: "operator",
                    title: "${message(code:'serviceParameterCondition.operator.label')}",
                    filterable: false
                },
                {
                    field: "value",
                    title: "${message(code:'serviceParameterCondition.value.label')}",
                    filterable: false
                },
                {
                    field: "lastUpdated",
                    title: "${message(code:'serviceParameterCondition.lastUpdated.label')}",
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
                    template: "<a class='k-button k-button-icontext k-grid-add' href='javascript:addGridItem();'>${message(code: 'serviceParameterCondition.add.pre')}</a>"
                }
            ]
        });
    });

    function addGridItem() {
        $('#formWindow').html($('#formWindowLoading').html())
            .kendoWindow({
                width: '820px',
                content: '<format:html value="${createLink(action: 'create', params:[limit:params.id, type:params.type])}"/>',
                modal: true,
                close: function (e) {
                }
            }).data('kendoWindow').title('${message(code:'serviceParameterCondition.add.pre')}').center().open().bind("refresh", function () {
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
            }).data('kendoWindow').title('${message(code:'serviceParameterCondition.edit')}').center().open().bind("refresh", function () {
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
                    window.alert('${message(code:'serviceParameterCondition.save.error')}');

            },
            error: function () {
                window.alert('${message(code:'serviceParameterCondition.save.error')}');
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
            confirm('${message(code:'serviceParameterCondition.delete.confirm')}', deleteItem, cancelDelete);
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
                    window.alert('${message(code:'serviceParameterCondition.delete.error')}');
                }
            });
            idForDelete = 0;
        }
    }
</script>
</body>
</html>
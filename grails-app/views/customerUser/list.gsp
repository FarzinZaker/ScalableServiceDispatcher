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
    <title><g:message code="menu.customers.users" args="${[customer?.name]}"/></title>
    <form:datePickerResources/>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: message(code: 'menu.customers'), url: createLink(controller: 'customer', action: 'list')],
                    [text: message(code: 'menu.customers.users', args: [customer?.name]), url: createLink(action: 'list', id: params.id)]
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
                        url: "${createLink(action: 'jsonList', params:[customer: params.id])}",
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
                            user: {type: "string"},
                            startDate: {type: "string"},
                            endDate: {type: "string"},
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
                    title: "${message(code:'customerUser.id.label')}",
                    filterable: false
                },
                {
                    field: "user",
                    title: "${message(code:'customerUser.user.label')}",
                    filterable: false
                },
                {
                    field: "lastUpdated",
                    title: "${message(code:'customerUser.lastUpdated.label')}",
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
                    template: "<a class='k-button k-button-icontext k-grid-add' href='javascript:addGridItem();'>${message(code: 'customerUser.add')}</a>"
                }
            ]
        });
    });

    function addGridItem() {
        $('#formWindow').html($('#formWindowLoading').html())
            .kendoWindow({
                width: '820px',
                content: '${createLink(action: 'create', params:[customer:params.id])}',
                modal: true,
                close: function (e) {
                }
            }).data('kendoWindow').title('${message(code:'customerUser.add')}').center().open().bind("refresh", function () {
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
            }).data('kendoWindow').title('${message(code:'customerUser.edit')}').center().open().bind("refresh", function () {
            $('#formWindow').data('kendoWindow').center().open();
        });
    }

    function viewLimits(e) {
        window.location.href = '${createLink(controller: 'userRateLimit', action: 'list')}/' + this.dataItem($(e.currentTarget).closest("tr")).id;
    }

    function viewParameterLimits(e) {
        window.location.href = '${createLink(controller: 'userParameterLimit', action: 'list')}/' + this.dataItem($(e.currentTarget).closest("tr")).id;
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
                    window.alert('${message(code:'customerUser.save.error')}');

            },
            error: function () {
                window.alert('${message(code:'customerUser.save.error')}');
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
            confirm('${message(code:'customerUser.delete.confirm')}', deleteItem, cancelDelete);
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
                    window.alert('${message(code:'customerUser.delete.error')}');
                }
            });
            idForDelete = 0;
        }
    }
</script>
</body>
</html>